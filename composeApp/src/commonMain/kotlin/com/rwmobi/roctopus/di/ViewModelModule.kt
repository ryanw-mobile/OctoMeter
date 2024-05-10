/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.di

import com.rwmobi.roctopus.ui.viewmodels.TariffsViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    factory {
        TariffsViewModel(
            octopusRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }
}
