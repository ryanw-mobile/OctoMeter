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
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlinx.datetime.Instant

interface RestApiRepository {
    suspend fun getTariff(tariffCode: String): Result<Tariff>
    suspend fun getProducts(requestedPage: Int? = null): Result<List<ProductSummary>>
    suspend fun getProductDetails(productCode: String): Result<ProductDetails>

    suspend fun getStandardUnitRates(
        tariffCode: String,
        period: ClosedRange<Instant>,
        requestedPage: Int? = null,
    ): Result<List<Rate>>

    suspend fun getStandingCharges(
        tariffCode: String,
        requestedPage: Int? = null,
    ): Result<List<Rate>>

    suspend fun getDayUnitRates(
        tariffCode: String,
        requestedPage: Int? = null,
    ): Result<List<Rate>>

    suspend fun getNightUnitRates(
        tariffCode: String,
        requestedPage: Int? = null,
    ): Result<List<Rate>>

    suspend fun getConsumption(
        apiKey: String,
        mpan: String,
        meterSerialNumber: String,
        period: ClosedRange<Instant>,
        orderBy: ConsumptionDataOrder = ConsumptionDataOrder.LATEST_FIRST,
        groupBy: ConsumptionTimeFrame = ConsumptionTimeFrame.HALF_HOURLY,
        requestedPage: Int? = null,
    ): Result<List<Consumption>>

    suspend fun getAccount(
        apiKey: String,
        accountNumber: String,
    ): Result<Account?>
}
