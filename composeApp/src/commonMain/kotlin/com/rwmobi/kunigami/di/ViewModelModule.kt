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

import com.rwmobi.kunigami.domain.usecase.consumption.GenerateUsageInsightsUseCase
import com.rwmobi.kunigami.ui.tools.MultiplatformStringResourceProvider
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
            getConsumptionAndCostUseCase = get(),
            getTariffUseCase = get(),
            generateUsageInsightsUseCase = GenerateUsageInsightsUseCase(),
            stringResourceProvider = MultiplatformStringResourceProvider(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        AgileViewModel(
            getLatestProductByKeywordUseCase = get(),
            syncUserProfileUseCase = get(),
            getTariffRatesUseCase = get(),
            getStandardUnitRateUseCase = get(),
            getLiveConsumptionUseCase = get(),
            stringResourceProvider = MultiplatformStringResourceProvider(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        TariffsViewModel(
            octopusApiRepository = get(),
            getFilteredProductsUseCase = get(),
            getDefaultPostcodeUseCase = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    factory {
        AccountViewModel(
            userPreferencesRepository = get(),
            initialiseAccountUseCase = get(),
            updateMeterPreferenceUseCase = get(),
            syncUserProfileUseCase = get(),
            clearCacheUseCase = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }
}
