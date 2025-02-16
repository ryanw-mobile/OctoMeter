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

import com.rwmobi.kunigami.data.source.network.restapi.AccountEndpoint
import com.rwmobi.kunigami.data.source.network.restapi.ProductsEndpoint
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val BASE_URL = "https://api.octopus.energy"

@OptIn(ExperimentalSerializationApi::class)
val ktorModule = module {
    single {
        HttpClient(engine = get()) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                        allowTrailingComma = true
                    },
                )
            }
        }
    }

    factory {
        ProductsEndpoint(
            baseUrl = BASE_URL,
            httpClient = get(),
            dispatcher = get(named("IoDispatcher")),
        )
    }

    factory {
        AccountEndpoint(
            baseUrl = BASE_URL,
            httpClient = get(),
            dispatcher = get(named("IoDispatcher")),
        )
    }
}
