/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.apollographql.apollo.ApolloClient
import com.rwmobi.kunigami.data.source.network.graphql.GraphQLEndpoint
import org.koin.core.qualifier.named
import org.koin.dsl.module

val graphQLModule = module {
    single {
        ApolloClient.Builder()
            .serverUrl("https://api.octopus.energy/v1/graphql/")
            .build()
    }

    factory {
        GraphQLEndpoint(
            apolloClient = get(),
            dispatcher = get(named("IoDispatcher")),
        )
    }
}
