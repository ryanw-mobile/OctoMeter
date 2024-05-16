/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleProductApiResponse(
    @SerialName("code") val code: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("description") val description: String,
    @SerialName("is_variable") val isVariable: Boolean,
    @SerialName("is_green") val isGreen: Boolean,
    @SerialName("is_tracker") val isTracker: Boolean,
    @SerialName("is_prepay") val isPrepay: Boolean,
    @SerialName("is_business") val isBusiness: Boolean,
    @SerialName("is_restricted") val isRestricted: Boolean,
    @SerialName("term") val term: Int?,
    @SerialName("available_from") val availableFrom: Instant,
    @SerialName("available_to") val availableTo: Instant?,
    @SerialName("tariffs_active_at") val tariffsActiveAt: Instant,
    @SerialName("single_register_electricity_tariffs") val singleRegisterElectricityTariffs: Map<String, TariffDetailsDto>,
    @SerialName("dual_register_electricity_tariffs") val dualRegisterElectricityTariffs: Map<String, TariffDetailsDto>,
    @SerialName("brand") val brand: String,
    @SerialName("links") val links: List<LinkDto>,
    @SerialName("sample_quotes") val sampleQuotes: Map<String, SampleQuotesDto>? = null,
    @SerialName("sample_consumption") val sampleConsumption: SampleConsumptionDto,
)
