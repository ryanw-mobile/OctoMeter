/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.data.repository.mapper

import com.rwmobi.roctopus.data.source.network.dto.PropertyDto
import com.rwmobi.roctopus.domain.model.Account

fun PropertyDto.toAccount() = Account(
    id = id,
    movedInAt = movedInAt,
    movedOutAt = movedOutAt,
    addressLine1 = addressLine1,
    addressLine2 = addressLine2,
    addressLine3 = addressLine3,
    town = town,
    county = county,
    postcode = postcode,
)
