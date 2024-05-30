/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.extensions

/***
 * This is needed to handle Ktor Darwin Engine exceptions which does not exist in CommonMain
 */
actual fun Throwable.mapFromPlatform(): Throwable = this
