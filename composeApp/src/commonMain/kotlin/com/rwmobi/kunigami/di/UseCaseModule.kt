/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

package com.rwmobi.kunigami.di

import com.rwmobi.kunigami.domain.usecase.account.ClearCacheUseCase
import com.rwmobi.kunigami.domain.usecase.account.GetDefaultPostcodeUseCase
import com.rwmobi.kunigami.domain.usecase.account.InitialiseAccountUseCase
import com.rwmobi.kunigami.domain.usecase.account.SyncUserProfileUseCase
import com.rwmobi.kunigami.domain.usecase.account.UpdateMeterPreferenceUseCase
import com.rwmobi.kunigami.domain.usecase.consumption.GetConsumptionAndCostUseCase
import com.rwmobi.kunigami.domain.usecase.product.GetFilteredProductsUseCase
import com.rwmobi.kunigami.domain.usecase.product.GetLatestProductByKeywordUseCase
import com.rwmobi.kunigami.domain.usecase.product.GetStandardUnitRateUseCase
import com.rwmobi.kunigami.domain.usecase.product.GetTariffRatesUseCase
import com.rwmobi.kunigami.domain.usecase.product.GetTariffUseCase
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
