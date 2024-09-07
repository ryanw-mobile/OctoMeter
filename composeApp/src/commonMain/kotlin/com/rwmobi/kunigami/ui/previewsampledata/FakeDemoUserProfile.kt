/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
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
