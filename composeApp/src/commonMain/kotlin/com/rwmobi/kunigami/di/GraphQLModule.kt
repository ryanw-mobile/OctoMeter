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

import com.apollographql.adapter.datetime.KotlinxInstantAdapter
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.http.HttpInterceptor
import com.rwmobi.kunigami.data.source.network.graphql.ApolloGraphQLEndpoint
import com.rwmobi.kunigami.data.source.network.graphql.AuthorisationInterceptor
import com.rwmobi.kunigami.data.source.network.graphql.interfaces.GraphQLEndpoint
import com.rwmobi.kunigami.graphql.type.DateTime
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
            .addCustomScalarAdapter(DateTime.type, KotlinxInstantAdapter)
            .build()
    }

    single<HttpInterceptor> {
        AuthorisationInterceptor(
            tokenRepository = get(),
        )
    }

    factory<GraphQLEndpoint> {
        ApolloGraphQLEndpoint(
            apolloClient = get(),
            dispatcher = get(named("IoDispatcher")),
        )
    }
}
