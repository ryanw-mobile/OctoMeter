/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.model.rate

import androidx.compose.runtime.Immutable

@Immutable
enum class PaymentMethod {
    UNKNOWN,
    DIRECT_DEBIT,
    NON_DIRECT_DEBIT,
    ;

    companion object {
        fun fromValue(value: String?): PaymentMethod = try {
            value?.let {
                valueOf(it.uppercase())
            } ?: UNKNOWN
        } catch (e: IllegalArgumentException) {
            UNKNOWN
        }
    }
}
