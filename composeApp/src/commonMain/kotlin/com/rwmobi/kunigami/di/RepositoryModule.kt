/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.rwmobi.kunigami.data.repository.DemoOctopusApiRepository
import com.rwmobi.kunigami.data.repository.OctopusRestApiRepository
import com.rwmobi.kunigami.data.repository.OctopusUserPreferencesRepository
import com.rwmobi.kunigami.domain.repository.OctopusApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<OctopusApiRepository>(named("production")) {
        OctopusRestApiRepository(
            productsEndpoint = get(),
            electricityMeterPointsEndpoint = get(),
            accountEndpoint = get(),
            inMemoryCacheDataSource = get(),
            databaseDataSource = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    single<OctopusApiRepository>(named("demo")) {
        DemoOctopusApiRepository()
    }

    // Set the "production" instance as the default for RestApiRepository
    single<OctopusApiRepository> { get(named("production")) }

    factory<UserPreferencesRepository> {
        OctopusUserPreferencesRepository(
            preferencesStore = get(),
            dispatcher = get(named("IoDispatcher")),
        )
    }
}
