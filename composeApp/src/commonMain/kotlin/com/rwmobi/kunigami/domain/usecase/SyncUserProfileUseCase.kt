/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.exceptions.IncompleteCredentialsException
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.domain.model.product.TariffSummary
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class SyncUserProfileUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val restApiRepository: RestApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(): Result<UserProfile?> {
        return withContext(dispatcher) {
            runCatching {
                if (userPreferencesRepository.isDemoMode()) {
                    throw IncompleteCredentialsException()
                }

                val apiKey = userPreferencesRepository.getApiKey()
                val accountNumber = userPreferencesRepository.getAccountNumber()

                // isDemoMode should have rejected null cases. This is to avoid using !! below
                checkNotNull(value = apiKey, lazyMessage = { "Expect API Key but null" })
                checkNotNull(value = accountNumber, lazyMessage = { "Expect Account Number but null" })

                var selectedAccount: Account? = null
                var selectedMpan = userPreferencesRepository.getMpan()
                var selectedMeterSerialNumber = userPreferencesRepository.getMeterSerialNumber()

                restApiRepository.getAccount(
                    apiKey = apiKey,
                    accountNumber = accountNumber,
                ).fold(
                    onSuccess = { accounts ->
                        // If no mpan and meter serial number is selected, we default to the first account and record
                        if (selectedMpan == null || selectedMeterSerialNumber == null) {
                            selectedAccount = accounts.firstOrNull()
                            selectedMpan = accounts.firstOrNull()?.getDefaultMpan()?.also {
                                userPreferencesRepository.setMpan(mpan = it)
                            }
                            selectedMeterSerialNumber = accounts.firstOrNull()?.getDefaultMeterSerialNumber()?.also {
                                userPreferencesRepository.setMeterSerialNumber(meterSerialNumber = it)
                            }
                        } else {
                            selectedAccount = accounts.firstOrNull { account ->
                                account.electricityMeterPoints.firstOrNull { electricityMeterPoint ->
                                    electricityMeterPoint.mpan == selectedMpan &&
                                        electricityMeterPoint.meterSerialNumbers.contains(selectedMeterSerialNumber)
                                } != null
                            }
                        }
                    },
                    onFailure = { throwable ->
                        throw throwable
                    },
                )

                var selectedTariffSummary: TariffSummary? = null
                (selectedAccount?.getTariffCode(selectedMpan))?.let { tariffCode ->
                    restApiRepository.getSimpleProductTariff(
                        productCode = TariffSummary.extractProductCode(tariffCode = tariffCode) ?: "",
                        tariffCode = tariffCode,
                    ).fold(
                        onSuccess = { tariff ->
                            selectedTariffSummary = tariff
                        },
                        onFailure = { throwable ->
                            throw throwable
                        },
                    )
                }

                // If any of the information is missing, we are not comfortable to proceed.
                // Caller making use of UserProfile should consider activating demo mode.
                if (selectedAccount == null ||
                    selectedMpan == null ||
                    selectedMeterSerialNumber == null ||
                    selectedTariffSummary == null
                ) {
                    null
                } else {
                    UserProfile(
                        selectedMpan = selectedMpan,
                        selectedMeterSerialNumber = selectedMeterSerialNumber,
                        account = selectedAccount,
                        tariffSummary = selectedTariffSummary,
                    )
                }
            }.except<CancellationException, _>()
        }
    }
}
