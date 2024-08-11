/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.test

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher

// https://proandroiddev.com/test-jetpack-compose-layouts-easily-with-role-semanticproperty-dcf19f64130f
internal fun withRole(role: Role) =
    SemanticsMatcher("${SemanticsProperties.Role.name} contains '$role'") {
        val roleProperty = it.config.getOrNull(SemanticsProperties.Role) ?: false
        roleProperty == role
    }
