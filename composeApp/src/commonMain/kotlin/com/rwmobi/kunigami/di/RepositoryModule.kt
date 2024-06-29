/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.rwmobi.kunigami.data.repository.DemoRestApiRepository
import com.rwmobi.kunigami.data.repository.OctopusRestApiRepository
import com.rwmobi.kunigami.data.repository.OctopusUserPreferencesRepository
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<RestApiRepository>(named("production")) {
        OctopusRestApiRepository(
            productsEndpoint = get(),
            electricityMeterPointsEndpoint = get(),
            accountEndpoint = get(),
            inMemoryCacheDataSource = get(),
            databaseDataSource = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    single<RestApiRepository>(named("demo")) {
        DemoRestApiRepository()
    }

    // Set the "production" instance as the default for RestApiRepository
    single<RestApiRepository> { get(named("production")) }

    factory<UserPreferencesRepository> {
        OctopusUserPreferencesRepository(
            preferencesStore = get(),
            dispatcher = get(named("IoDispatcher")),
        )
    }
}
