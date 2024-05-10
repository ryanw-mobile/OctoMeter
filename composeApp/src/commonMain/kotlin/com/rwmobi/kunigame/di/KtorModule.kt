/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.di

import com.rwmobi.kunigame.data.source.network.AccountEndpoint
import com.rwmobi.kunigame.data.source.network.ElectricityMeterPointsEndpoint
import com.rwmobi.kunigame.data.source.network.ProductsEndpoint
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
    single { getKtorEngine() }
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
            dispatcher = get(named("IODispatcher")),
        )
    }

    factory {
        ElectricityMeterPointsEndpoint(
            baseUrl = BASE_URL,
            httpClient = get(),
            dispatcher = get(named("IODispatcher")),
        )
    }

    factory {
        AccountEndpoint(
            baseUrl = BASE_URL,
            httpClient = get(),
            dispatcher = get(named("IODispatcher")),
        )
    }
}
