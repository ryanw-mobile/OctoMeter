/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.rwmobi.kunigami.domain.usecase.GetConsumptionUseCase
import com.rwmobi.kunigami.domain.usecase.GetFilteredProductsUseCase
import com.rwmobi.kunigami.domain.usecase.GetUserAccountUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val userCaseModule = module {
    factory {
        GetFilteredProductsUseCase(
            octopusRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        GetUserAccountUseCase(
            userPreferencesRepository = get(),
            octopusRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        GetConsumptionUseCase(
            userPreferencesRepository = get(),
            octopusRepository = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }
}
