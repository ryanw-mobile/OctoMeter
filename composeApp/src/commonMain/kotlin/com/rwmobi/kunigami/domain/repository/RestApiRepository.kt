/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.repository

import com.rwmobi.kunigami.domain.model.Account
import com.rwmobi.kunigami.domain.model.Consumption
import com.rwmobi.kunigami.domain.model.Product
import com.rwmobi.kunigami.domain.model.Rate
import com.rwmobi.kunigami.domain.model.Tariff
import kotlinx.datetime.Instant

interface RestApiRepository {
    suspend fun getSimpleProductTariff(
        productCode: String,
        tariffCode: String,
    ): Result<Tariff>

    suspend fun getProducts(): Result<List<Product>>
    suspend fun getStandardUnitRates(
        productCode: String,
        tariffCode: String,
        periodFrom: Instant? = null,
        periodTo: Instant? = null,
    ): Result<List<Rate>>

    suspend fun getStandingCharges(productCode: String, tariffCode: String): Result<List<Rate>>
    suspend fun getDayUnitRates(productCode: String, tariffCode: String): Result<List<Rate>>
    suspend fun getNightUnitRates(productCode: String, tariffCode: String): Result<List<Rate>>
    suspend fun getConsumption(
        apiKey: String,
        mpan: String,
        meterSerialNumber: String,
        periodFrom: Instant? = null,
        periodTo: Instant? = null,
    ): Result<List<Consumption>>

    suspend fun getAccount(apiKey: String, accountNumber: String): Result<List<Account>>
}
