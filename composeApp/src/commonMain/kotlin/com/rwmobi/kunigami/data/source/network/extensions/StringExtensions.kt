/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.extensions

import io.ktor.utils.io.core.toByteArray
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
internal fun encodeApiKey(apiKey: String): String {
    return Base64.encode("$apiKey:".toByteArray())
}

internal fun String.capitalizeWords(): String {
    return lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
