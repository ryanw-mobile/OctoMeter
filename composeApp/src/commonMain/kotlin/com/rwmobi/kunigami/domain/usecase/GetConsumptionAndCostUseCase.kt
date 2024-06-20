/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.model.account.Agreement
import com.rwmobi.kunigami.domain.model.account.ElectricityMeterPoint
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataOrder
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionWithCost
import com.rwmobi.kunigami.domain.model.product.TariffSummary
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import com.rwmobi.kunigami.ui.previewsampledata.FakeDemoUserProfile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException

class GetConsumptionAndCostUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val restApiRepository: RestApiRepository,
    private val demoRestApiRepository: RestApiRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    /***
     * Pull consumption data, then maps with the corresponding tariff and rate.
     * Currently cost calculation is limited to half-hourly only.
     * All other options will have null costs.
     * If the use case failed to retrieve the account and tariff but consumption,
     * it will still return the consumption but with null costs.
     */
    suspend operator fun invoke(
        period: ClosedRange<Instant>,
        groupBy: ConsumptionTimeFrame,
    ): Result<List<ConsumptionWithCost>> {
        return withContext(dispatcher) {
            runCatching {
                val isDemoMode = userPreferencesRepository.isDemoMode()
                if (isDemoMode) {
                    generateDemoResponse(
                        period = period,
                        groupBy = groupBy,
                    )
                } else {
                    val apiKey = userPreferencesRepository.getApiKey()
                    val mpan = userPreferencesRepository.getMpan()
                    val meterSerialNumber = userPreferencesRepository.getMeterSerialNumber()
                    val accountNumber = userPreferencesRepository.getAccountNumber()

                    requireNotNull(value = apiKey, lazyMessage = { "Assertion failed: API Key is null" })
                    requireNotNull(value = mpan, lazyMessage = { "Assertion failed: MPAN is null" })
                    requireNotNull(value = meterSerialNumber, lazyMessage = { "Assertion failed: Meter Serial Number is null" })
                    requireNotNull(value = accountNumber, lazyMessage = { "Assertion failed: Account is null" })

                    var unitRates: List<Rate> = emptyList()
                    if (groupBy == ConsumptionTimeFrame.HALF_HOURLY) {
                        // We need all the agreements covering the requested period to get the correct unit rates
                        val account = restApiRepository.getAccount(
                            apiKey = apiKey,
                            accountNumber = accountNumber,
                        ).getOrNull()

                        if (account != null) {
                            // find the tariffs in the requested period
                            val agreements = getAgreements(
                                electricityMeterPoint = account.getElectricityMeterPoint(mpan = mpan),
                                period = period,
                            )
                            unitRates = getUnitRates(
                                agreements = agreements,
                                period = period,
                            )
                        }
                    }

                    restApiRepository.getConsumption(
                        apiKey = apiKey,
                        mpan = mpan,
                        meterSerialNumber = meterSerialNumber,
                        period = period,
                        orderBy = ConsumptionDataOrder.PERIOD,
                        groupBy = groupBy,
                    ).fold(
                        onSuccess = { consumption ->
                            consumption.sortedBy {
                                it.interval.start
                            }.map {
                                ConsumptionWithCost(
                                    consumption = it,
                                    vatInclusiveCost = calculateConsumptionCost(
                                        consumption = it,
                                        unitRates = unitRates,
                                    ),
                                )
                            }
                        },
                        onFailure = { throw it },
                    )
                }
            }.except<CancellationException, _>()
        }
    }

    private suspend fun generateDemoResponse(period: ClosedRange<Instant>, groupBy: ConsumptionTimeFrame): List<ConsumptionWithCost> {
        // Since the user profile is completely fake, we are able to assign a variable tariff covering the entire period for cost calculations
        val unitRates = FakeDemoUserProfile.flexibleOctopusRegionADirectDebit.getSelectedElectricityMeterPoint()?.agreements?.let { agreements ->
            getUnitRates(
                agreements = agreements,
                period = period,
            )
        } ?: emptyList()

        return demoRestApiRepository.getConsumption(
            apiKey = "",
            mpan = "",
            meterSerialNumber = "",
            period = period,
            orderBy = ConsumptionDataOrder.PERIOD,
            groupBy = groupBy,
        ).fold(
            onSuccess = { consumption ->
                consumption.sortedBy {
                    it.interval.start
                }.map {
                    ConsumptionWithCost(
                        consumption = it,
                        vatInclusiveCost = calculateConsumptionCost(
                            consumption = it,
                            unitRates = unitRates,
                        ),
                    )
                }
            },
            onFailure = { throw it },
        )
    }

    private fun getAgreements(electricityMeterPoint: ElectricityMeterPoint?, period: ClosedRange<Instant>): List<Agreement> {
        return electricityMeterPoint?.lookupAgreements(period = period) ?: emptyList()
    }

    /***
     * To simplify processing, we assume tariffs do not overlap
     * It is not possible in the real world, plus the current implementation,
     * We only deal with half-hourly won't even have more than one tariffs.
     */
    private suspend fun getUnitRates(agreements: List<Agreement>, period: ClosedRange<Instant>): List<Rate> {
        val unitRates = mutableListOf<Rate>()
        agreements.forEach { agreement ->
            val effectiveQueryStartDate = maxOf(agreement.period.start, period.start)
            val effectiveQueryEndDate = minOf(agreement.period.endInclusive, period.endInclusive)
            val productCode = TariffSummary.extractProductCode(tariffCode = agreement.tariffCode)

            unitRates.addAll(
                restApiRepository.getStandardUnitRates(
                    productCode = productCode!!,
                    tariffCode = agreement.tariffCode,
                    period = effectiveQueryStartDate..effectiveQueryEndDate,
                ).getOrNull() ?: emptyList(),
            )
        }
        return unitRates
    }

    private fun calculateConsumptionCost(
        consumption: Consumption,
        unitRates: List<Rate>,
    ): Double? {
        if (unitRates.isEmpty()) return null

        val effectiveUnitRate = unitRates.firstOrNull {
            it.validity.start <= consumption.interval.start &&
                it.validity.endInclusive >= consumption.interval.endInclusive
        }

        return effectiveUnitRate?.let { rate ->
            rate.vatInclusivePrice * consumption.kWhConsumed
        }
    }
}
