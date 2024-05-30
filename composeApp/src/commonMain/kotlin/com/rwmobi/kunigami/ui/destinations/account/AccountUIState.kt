/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.destinations.account

import androidx.compose.runtime.Immutable
import com.rwmobi.kunigami.domain.model.account.UserProfile
import com.rwmobi.kunigami.ui.model.ErrorMessage

@Immutable
data class AccountUIState(
    val isLoading: Boolean = true,
    val requestedScreenType: AccountScreenType? = null,
    val requestedLayout: AccountScreenLayout = AccountScreenLayout.Compact,
    val userProfile: UserProfile? = null,
    val requestScrollToTop: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
)
