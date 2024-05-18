/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.exceptions

class NoLinkedAccountException(message: String = "No account associated with this API key") : Throwable(message)
