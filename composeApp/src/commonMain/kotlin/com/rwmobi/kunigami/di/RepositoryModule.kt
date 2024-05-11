/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.rwmobi.kunigami.data.repository.OctopusRestApiRepository
import com.rwmobi.kunigami.domain.repository.OctopusRepository
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
