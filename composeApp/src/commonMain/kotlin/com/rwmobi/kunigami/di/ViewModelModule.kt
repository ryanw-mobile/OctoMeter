/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.rwmobi.kunigami.ui.viewmodels.AccountViewModel
import com.rwmobi.kunigami.ui.viewmodels.OnboardingViewModel
import com.rwmobi.kunigami.ui.viewmodels.TariffsViewModel
import com.rwmobi.kunigami.ui.viewmodels.UsageViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    factory {
        OnboardingViewModel(
            octopusRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        UsageViewModel(
            octopusRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        TariffsViewModel(
            getFilteredProductsUseCase = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        AccountViewModel(
            octopusRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }
}
