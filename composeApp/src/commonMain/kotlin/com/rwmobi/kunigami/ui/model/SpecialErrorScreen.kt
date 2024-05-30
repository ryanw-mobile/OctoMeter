/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.model

sealed interface SpecialErrorScreen {
    data object NoData : SpecialErrorScreen
    data object UnknownError : SpecialErrorScreen
    data object NetworkError : SpecialErrorScreen
    data class HttpError(val statusCode: Int) : SpecialErrorScreen
}
