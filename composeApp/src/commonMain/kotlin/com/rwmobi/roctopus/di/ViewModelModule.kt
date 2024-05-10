/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.di

import com.rwmobi.roctopus.ui.viewmodels.AccountViewModel
import com.rwmobi.roctopus.ui.viewmodels.OnboardingViewModel
import com.rwmobi.roctopus.ui.viewmodels.TariffsViewModel
import com.rwmobi.roctopus.ui.viewmodels.UsageViewModel
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
            octopusRepository = get(),
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
