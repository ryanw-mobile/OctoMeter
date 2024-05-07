/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.di

import com.rwmobi.roctopus.data.source.network.ElectricityMeterPointsEndpoint
import com.rwmobi.roctopus.data.source.network.ProductsEndpoint
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.dsl.module

const val BASE_URL = "https://api.octopus.energy"

@OptIn(ExperimentalSerializationApi::class)
val ktorModule = module {
    single { CIO.create() }
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
        )

        ElectricityMeterPointsEndpoint(
            baseUrl = BASE_URL,
            httpClient = get(),
        )
    }
}
