/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigame.ui.destinations.onboarding

import com.rwmobi.kunigame.ui.model.ErrorMessage

data class OnboardingUIState(
    val isLoading: Boolean = true,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
