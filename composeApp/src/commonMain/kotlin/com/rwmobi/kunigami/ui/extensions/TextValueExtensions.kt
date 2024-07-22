/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.extensions

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

internal fun TextFieldValue.polishPostcode(): TextFieldValue {
    var newSelection = selection.start
    val filtered = text.filter { it.isLetterOrDigit() }
    val formatted = StringBuilder(filtered.take(7).uppercase())
    if (filtered.length > 4) {
        formatted.insert(4, ' ')
        if (selection.start > 3) {
            newSelection++
        }
    }
    return copy(
        text = formatted.toString(),
        selection = TextRange(newSelection),
    )
}
