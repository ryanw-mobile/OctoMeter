/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.ui.destinations.onboarding

import com.rwmobi.roctopus.ui.model.ErrorMessage

data class OnboardingUIState(
    val isLoading: Boolean = true,
    val errorMessages: List<ErrorMessage> = emptyList(),
)