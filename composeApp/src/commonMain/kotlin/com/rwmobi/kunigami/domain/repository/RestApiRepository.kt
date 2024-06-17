/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.repository

import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataOrder
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.domain.model.product.TariffSummary
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlinx.datetime.Instant

interface RestApiRepository {
    suspend fun getSimpleProductTariff(
        productCode: String,
        tariffCode: String,
    ): Result<TariffSummary>

    suspend fun getProducts(): Result<List<ProductSummary>>
    suspend fun getProductDetails(productCode: String): Result<ProductDetails>
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
        orderBy: ConsumptionDataOrder = ConsumptionDataOrder.LATEST_FIRST,
        groupBy: ConsumptionTimeFrame = ConsumptionTimeFrame.HALF_HOURLY,
    ): Result<List<Consumption>>

    suspend fun getAccount(apiKey: String, accountNumber: String): Result<Account?>
}
