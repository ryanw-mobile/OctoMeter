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
        selectedMpan = AccountSamples.account928.electricityMeterPoints[0].mpan,
        selectedMeterSerialNumber = AccountSamples.account928.electricityMeterPoints[0].meters[0].serialNumber,
        account = AccountSamples.account928,
    )
}
