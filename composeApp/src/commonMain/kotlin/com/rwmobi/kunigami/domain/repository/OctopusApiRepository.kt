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

package com.rwmobi.kunigami.domain.repository

import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataOrder
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlinx.datetime.Instant

interface OctopusApiRepository {
    suspend fun getTariff(tariffCode: String): Result<Tariff>
    suspend fun getProducts(postcode: String): Result<List<ProductSummary>>

    suspend fun getProductDetails(
        productCode: String,
        postcode: String,
    ): Result<ProductDetails>

    suspend fun getStandardUnitRates(
        tariffCode: String,
        period: ClosedRange<Instant>,
        requestedPage: Int? = null,
    ): Result<List<Rate>>

    suspend fun getStandingCharges(
        tariffCode: String,
        paymentMethod: PaymentMethod = PaymentMethod.UNKNOWN, // Fix and Agile are always unknown; Variable splits
        period: ClosedRange<Instant>? = null,
        requestedPage: Int? = null,
    ): Result<List<Rate>>

    suspend fun getDayUnitRates(
        tariffCode: String,
        period: ClosedRange<Instant>?,
        requestedPage: Int? = null,
    ): Result<List<Rate>>

    suspend fun getNightUnitRates(
        tariffCode: String,
        period: ClosedRange<Instant>?,
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
        accountNumber: String,
    ): Result<Account?>

    suspend fun clearCache()
}
