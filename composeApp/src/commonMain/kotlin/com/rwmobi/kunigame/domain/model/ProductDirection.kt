/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.domain.model

enum class ProductDirection {
    UNKNOWN,
    IMPORT,
    EXPORT,
    ;

    companion object {
        fun fromApiValue(value: String?): ProductDirection = try {
            value?.let {
                valueOf(it.uppercase())
            } ?: UNKNOWN
        } catch (e: IllegalArgumentException) {
            UNKNOWN
        }
    }
}
