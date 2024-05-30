/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.product

enum class ExitFeesType {
    UNKNOWN,
    NONE,
    ;

    companion object {
        fun fromApiValue(value: String?): ExitFeesType = try {
            value?.let {
                ExitFeesType.valueOf(it.uppercase())
            } ?: UNKNOWN
        } catch (e: IllegalArgumentException) {
            UNKNOWN
        }
    }
}
