/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.di

import com.rwmobi.kunigame.data.repository.OctopusRestApiRepository
import com.rwmobi.kunigame.domain.repository.OctopusRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<OctopusRepository> {
        OctopusRestApiRepository(
            productsEndpoint = get(),
            electricityMeterPointsEndpoint = get(),
            accountEndpoint = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }
}
