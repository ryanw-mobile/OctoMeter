/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.usecase

import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class UpdateMeterPreferenceUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        mpan: String,
        meterSerialNumber: String,
    ): Result<Unit> {
        return withContext(dispatcher) {
            runCatching {
                Logger.v("mpan = $mpan, meterSerialNumber = $meterSerialNumber")
                userPreferencesRepository.setMpan(mpan = mpan)
                userPreferencesRepository.setMeterSerialNumber(meterSerialNumber = meterSerialNumber)
            }.except<CancellationException, _>()
        }
    }
}
