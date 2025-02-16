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
