/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.rwmobi.kunigami.domain.usecase.GetConsumptionAndCostUseCase
import com.rwmobi.kunigami.domain.usecase.GetFilteredProductsUseCase
import com.rwmobi.kunigami.domain.usecase.GetStandardUnitRateUseCase
import com.rwmobi.kunigami.domain.usecase.GetTariffRatesUseCase
import com.rwmobi.kunigami.domain.usecase.GetTariffSummaryUseCase
import com.rwmobi.kunigami.domain.usecase.InitialiseAccountUseCase
import com.rwmobi.kunigami.domain.usecase.SyncUserProfileUseCase
import com.rwmobi.kunigami.domain.usecase.UpdateMeterPreferenceUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val userCaseModule = module {
    factory {
        GetFilteredProductsUseCase(
            restApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        GetStandardUnitRateUseCase(
            restApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        GetConsumptionAndCostUseCase(
            userPreferencesRepository = get(),
            restApiRepository = get(),
            demoRestApiRepository = get(named("demo")),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        GetTariffRatesUseCase(
            restApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        InitialiseAccountUseCase(
            userPreferencesRepository = get(),
            restApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        UpdateMeterPreferenceUseCase(
            userPreferencesRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        SyncUserProfileUseCase(
            userPreferencesRepository = get(),
            restApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        GetTariffSummaryUseCase(
            restApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }
}
