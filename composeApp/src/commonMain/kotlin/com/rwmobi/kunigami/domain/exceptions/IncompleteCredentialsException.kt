/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.exceptions

class IncompleteCredentialsException(message: String = "Failed to retrieve the required credentials") : Throwable(message)
