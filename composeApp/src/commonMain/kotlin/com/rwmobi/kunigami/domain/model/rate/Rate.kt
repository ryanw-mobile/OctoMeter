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

package com.rwmobi.kunigami.domain.model.rate

import androidx.compose.runtime.Immutable
import kotlin.time.Instant

@Immutable
data class Rate(
    val vatInclusivePrice: Double,
    val validity: ClosedRange<Instant>, // Caller should end using Instant.DISTANT_FUTURE if API returns null
    val paymentMethod: PaymentMethod,
) {
    fun isActive(referencePoint: Instant): Boolean {
        return validity.contains(referencePoint)
    }
}
