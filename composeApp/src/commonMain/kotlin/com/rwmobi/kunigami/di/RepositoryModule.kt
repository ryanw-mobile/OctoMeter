/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.rwmobi.kunigami.data.repository.DemoOctopusApiRepository
import com.rwmobi.kunigami.data.repository.GraphQLTokenRepository
import com.rwmobi.kunigami.data.repository.OctopusGraphQLRepository
import com.rwmobi.kunigami.data.repository.OctopusUserPreferencesRepository
import com.rwmobi.kunigami.data.source.network.graphql.interfaces.GraphQLEndpoint
import com.rwmobi.kunigami.domain.repository.OctopusApiRepository
import com.rwmobi.kunigami.domain.repository.TokenRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<OctopusApiRepository>(named("graphql")) {
        OctopusGraphQLRepository(
            productsEndpoint = get(),
            electricityMeterPointsEndpoint = get(),
            inMemoryCacheDataSource = get(),
            databaseDataSource = get(),
            graphQLEndpoint = get(),
            dispatcher = get(named("DefaultDispatcher")),
        )
    }

    single<OctopusApiRepository>(named("demo")) {
        DemoOctopusApiRepository()
    }

    // Set the "production" instance as the default for RestApiRepository
    single<OctopusApiRepository> { get(named("graphql")) }

    factory<UserPreferencesRepository> {
        OctopusUserPreferencesRepository(
            preferencesStore = get(),
            dispatcher = get(named("IoDispatcher")),
        )
    }

    single<TokenRepository> {
        GraphQLTokenRepository(
            inMemoryCacheDataSource = get(),
            graphQLEndpointProvider = { get<GraphQLEndpoint>() },
            userPreferencesRepository = get(),
        )
    }
}
