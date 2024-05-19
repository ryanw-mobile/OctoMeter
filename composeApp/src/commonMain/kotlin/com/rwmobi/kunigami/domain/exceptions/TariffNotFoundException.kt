/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.exceptions

class TariffNotFoundException(tariffCode: String, message: String = "rate not found for tariff $tariffCode") : Throwable(message)
