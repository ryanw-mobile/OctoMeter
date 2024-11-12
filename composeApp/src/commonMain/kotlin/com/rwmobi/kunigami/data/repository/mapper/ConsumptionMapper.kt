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

package com.rwmobi.kunigami.data.repository.mapper

import com.rwmobi.kunigami.data.source.local.database.entity.ConsumptionEntity
import com.rwmobi.kunigami.data.source.network.dto.consumption.ConsumptionDto
import com.rwmobi.kunigami.domain.extensions.roundConsumptionToNearestEvenHundredth
import com.rwmobi.kunigami.domain.model.consumption.Consumption

fun ConsumptionDto.toConsumption() = Consumption(
    kWhConsumed = consumption.roundConsumptionToNearestEvenHundredth(),
    interval = intervalStart..intervalEnd,
)

fun ConsumptionDto.toConsumptionEntity(meterSerial: String) = ConsumptionEntity(
    meterSerial = meterSerial,
    intervalStart = intervalStart,
    intervalEnd = intervalEnd,
    kWhConsumed = consumption, // keep raw figures - caller do rounding
)

fun ConsumptionEntity.toConsumption() = Consumption(
    kWhConsumed = kWhConsumed.roundConsumptionToNearestEvenHundredth(),
    interval = intervalStart..intervalEnd,
)
