/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.domain.repository

import com.rwmobi.roctopus.domain.model.Consumption
import com.rwmobi.roctopus.domain.model.Product
import com.rwmobi.roctopus.domain.model.Rate

interface OctopusRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getStandardUnitRates(productCode: String, tariffCode: String): Result<List<Rate>>
    suspend fun getStandingCharges(productCode: String, tariffCode: String): Result<List<Rate>>
    suspend fun getDayUnitRates(productCode: String, tariffCode: String): Result<List<Rate>>
    suspend fun getNightUnitRates(productCode: String, tariffCode: String): Result<List<Rate>>
    suspend fun getConsumption(apiKey: String, mpan: String, meterSerialNumber: String): Result<List<Consumption>>
}
