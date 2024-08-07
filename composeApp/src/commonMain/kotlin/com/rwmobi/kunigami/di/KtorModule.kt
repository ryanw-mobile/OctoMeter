/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.di

import com.rwmobi.kunigami.data.source.network.restapi.AccountEndpoint
import com.rwmobi.kunigami.data.source.network.restapi.ElectricityMeterPointsEndpoint
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
        ElectricityMeterPointsEndpoint(
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
