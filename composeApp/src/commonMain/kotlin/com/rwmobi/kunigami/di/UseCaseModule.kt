/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.rwmobi.kunigami.domain.usecase.ClearCacheUseCase
import com.rwmobi.kunigami.domain.usecase.GetConsumptionAndCostUseCase
import com.rwmobi.kunigami.domain.usecase.GetDefaultPostcodeUseCase
import com.rwmobi.kunigami.domain.usecase.GetFilteredProductsUseCase
import com.rwmobi.kunigami.domain.usecase.GetLatestProductByKeywordUseCase
import com.rwmobi.kunigami.domain.usecase.GetStandardUnitRateUseCase
import com.rwmobi.kunigami.domain.usecase.GetTariffRatesUseCase
import com.rwmobi.kunigami.domain.usecase.GetTariffUseCase
import com.rwmobi.kunigami.domain.usecase.InitialiseAccountUseCase
import com.rwmobi.kunigami.domain.usecase.SyncUserProfileUseCase
import com.rwmobi.kunigami.domain.usecase.UpdateMeterPreferenceUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val userCaseModule = module {
    factory {
        GetFilteredProductsUseCase(
            octopusApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        GetStandardUnitRateUseCase(
            octopusApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        GetConsumptionAndCostUseCase(
            userPreferencesRepository = get(),
            octopusApiRepository = get(),
            demoOctopusApiRepository = get(named("demo")),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        GetTariffRatesUseCase(
            octopusApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        InitialiseAccountUseCase(
            userPreferencesRepository = get(),
            octopusApiRepository = get(),
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
            octopusApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        GetTariffUseCase(
            octopusApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        GetLatestProductByKeywordUseCase(
            octopusApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        ClearCacheUseCase(
            octopusApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        GetDefaultPostcodeUseCase(
            userPreferencesRepository = get(),
            octopusApiRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }
}
