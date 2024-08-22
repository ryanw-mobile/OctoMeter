/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
// In the :presentation module under ui/model package
package com.rwmobi.kunigami.ui.tools.interfaces

import org.jetbrains.compose.resources.StringResource

interface StringResourceProvider {
    suspend fun getString(resource: StringResource, vararg formatArgs: Any): String
}
