/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.extensions

/***
 * Check if the string has a valid postcode pattern. It does not check if the postcode actually exists.
 * The format for UK postcodes can vary, but generally follows specific patterns such as:
 * 	•	A9 9AA
 * 	•	A99 9AA
 * 	•	AA9 9AA
 * 	•	AA99 9AA
 */
internal fun String.isValidPostcodePattern(): Boolean {
    val cleanedPostcode = filter { it.isLetterOrDigit() }
    val postcodePattern = Regex("^[A-Z]{1,2}[0-9][0-9A-Z]? ?[0-9][A-Z]{2}$")
    return postcodePattern.matches(cleanedPostcode)
}

/***
 * Apply formatting if the string is a valid postcode.
 * Return the original value otherwise
 */
internal fun String.formatPostcode(): String {
    if (!isValidPostcodePattern()) return this

    val cleanedPostcode = filter { it.isLetterOrDigit() }.uppercase()
    return when (cleanedPostcode.length) {
        5 -> cleanedPostcode.substring(0, 2) + " " + cleanedPostcode.substring(2)
        6 -> cleanedPostcode.substring(0, 3) + " " + cleanedPostcode.substring(3)
        7 -> cleanedPostcode.substring(0, 4) + " " + cleanedPostcode.substring(4)
        else -> this
    }
}
