/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
package com.rwmobi.kunigami.ui.model

import com.rwmobi.kunigami.ui.tools.interfaces.StringResourceProvider
import org.jetbrains.compose.resources.StringResource

class StubStringResourceProvider : StringResourceProvider {
    override suspend fun getString(resource: StringResource, vararg formatArgs: Any): String {
        return "Stubbed string"
    }
}
