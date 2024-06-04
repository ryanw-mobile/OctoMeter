/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.ui.previewsampledata

import com.rwmobi.kunigami.domain.model.account.UserProfile

internal object FakeDemoUserProfile {
    val flexibleOctopusRegionADirectDebit = UserProfile(
        selectedMpan = "9900000999999",
        selectedMeterSerialNumber = "99A9999999",
        account = AccountSamples.account928,
        tariffSummary = TariffSamples.var221101,
    )
}
