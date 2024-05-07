/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.di

import com.rwmobi.roctopus.data.repository.OctopusRestApiRepository
import com.rwmobi.roctopus.domain.repository.OctopusRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<OctopusRepository> {
        OctopusRestApiRepository(
            productsEndpoint = get(),
        )
    }
}
