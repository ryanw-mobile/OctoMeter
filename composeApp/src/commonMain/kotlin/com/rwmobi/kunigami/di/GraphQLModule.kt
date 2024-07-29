/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.http.HttpInterceptor
import com.rwmobi.kunigami.data.source.network.graphql.AuthorisationInterceptor
import com.rwmobi.kunigami.data.source.network.graphql.GraphQLEndpoint
import org.koin.core.qualifier.named
import org.koin.dsl.module

val graphQLModule = module {
    single {
        ApolloClient.Builder()
            .serverUrl("https://api.octopus.energy/v1/graphql/")
            .addHttpInterceptor(
                AuthorisationInterceptor(
                    tokenRepository = get(),
                ),
            )
            .build()
    }

    single<HttpInterceptor> {
        AuthorisationInterceptor(
            tokenRepository = get(),
        )
    }

    factory {
        GraphQLEndpoint(
            apolloClient = get(),
            dispatcher = get(named("IoDispatcher")),
        )
    }
}
