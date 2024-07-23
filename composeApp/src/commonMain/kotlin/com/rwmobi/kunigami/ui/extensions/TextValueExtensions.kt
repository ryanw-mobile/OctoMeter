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
    val originalText = text
    val originalSelection = selection.start

    val filteredText = originalText.filter { it.isLetterOrDigit() }
    val formattedText = StringBuilder(filteredText.take(7).uppercase())

    // Case 1: Typing a new string
    if (originalSelection == originalText.length) {
        if (filteredText.length > 4) {
            formattedText.insert(4, ' ')
        }
        return copy(
            text = formattedText.toString(),
            selection = TextRange(formattedText.length),
        )
    }

    // Case 2: Editing an existing string
    var newSelection = originalSelection
    if (filteredText.length > 4) {
        formattedText.insert(4, ' ')
    }

    // Calculate the number of invalid characters before the cursor position
    val invalidCharsBeforeCursor = originalText.take(originalSelection).count { !it.isLetterOrDigit() }

    // Adjust the new selection based on the invalid characters
    newSelection -= invalidCharsBeforeCursor

    // Further adjust new selection if it's affected by the inserted space
    if (filteredText.length > 4 && newSelection > 4) {
        newSelection++
    }

    // Ensure the new selection is within the bounds of the formatted text
    newSelection = newSelection.coerceIn(0, formattedText.length)

    return copy(
        text = formattedText.toString(),
        selection = TextRange(newSelection),
    )
}
