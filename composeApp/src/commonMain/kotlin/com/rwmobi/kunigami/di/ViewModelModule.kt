/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.rwmobi.kunigami.ui.viewmodels.AccountViewModel
import com.rwmobi.kunigami.ui.viewmodels.AgileViewModel
import com.rwmobi.kunigami.ui.viewmodels.TariffsViewModel
import com.rwmobi.kunigami.ui.viewmodels.UsageViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    factory {
        UsageViewModel(
            syncUserProfileUseCase = get(),
            getConsumptionUseCase = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        AgileViewModel(
            syncUserProfileUseCase = get(),
            getTariffRatesUseCase = get(),
            getStandardUnitRateUseCase = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        TariffsViewModel(
            restApiRepository = get(),
            getFilteredProductsUseCase = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        AccountViewModel(
            userPreferencesRepository = get(),
            initialiseAccountUseCase = get(),
            updateMeterPreferenceUseCase = get(),
            syncUserProfileUseCase = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }
}
