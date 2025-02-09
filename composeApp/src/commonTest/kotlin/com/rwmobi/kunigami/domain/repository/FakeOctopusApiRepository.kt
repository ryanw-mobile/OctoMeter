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
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import com.rwmobi.kunigami.domain.model.consumption.LiveConsumption
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import kotlinx.datetime.Instant

class FakeOctopusApiRepository : OctopusApiRepository {

    var setSimpleProductTariffResponse: Result<Tariff>? = null
    override suspend fun getTariff(
        tariffCode: String,
    ): Result<Tariff> {
        return setSimpleProductTariffResponse ?: throw RuntimeException("Fake result setSimpleProductTariffResponse not defined")
    }

    var setProductsResponse: Result<List<ProductSummary>>? = null
    override suspend fun getProducts(postcode: String): Result<List<ProductSummary>> {
        return setProductsResponse ?: throw RuntimeException("Fake result setProductsResponse not defined")
    }

    var setProductDetailsResponse: Result<ProductDetails>? = null
    override suspend fun getProductDetails(productCode: String, postcode: String): Result<ProductDetails> {
        return setProductDetailsResponse ?: throw RuntimeException("Fake result setProductDetailsResponse not defined")
    }

    var setStandardUnitRatesResponse: Result<List<Rate>>? = null
    override suspend fun getStandardUnitRates(
        tariffCode: String,
        period: ClosedRange<Instant>,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        return setStandardUnitRatesResponse ?: throw RuntimeException("Fake result setStandardUnitRatesResponse not defined")
    }

    var setStandingChargesResponse: Result<List<Rate>>? = null
    override suspend fun getStandingCharges(
        tariffCode: String,
        paymentMethod: PaymentMethod,
        period: ClosedRange<Instant>?,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        return setStandingChargesResponse ?: throw RuntimeException("Fake result setStandingChargesResponse not defined")
    }

    var setDayUnitRatesResponse: Result<List<Rate>>? = null
    override suspend fun getDayUnitRates(
        tariffCode: String,
        period: ClosedRange<Instant>?,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        return setDayUnitRatesResponse ?: throw RuntimeException("Fake result setDayUnitRatesResponse not defined")
    }

    var setNightUnitRatesResponse: Result<List<Rate>>? = null
    override suspend fun getNightUnitRates(
        tariffCode: String,
        period: ClosedRange<Instant>?,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        return setNightUnitRatesResponse ?: throw RuntimeException("Fake result setNightUnitRatesResponse not defined")
    }

    var setConsumptionResponse: Result<List<Consumption>>? = null
    override suspend fun getConsumption(
        apiKey: String,
        meterSerialNumber: String,
        deviceId: String,
        mpan: String,
        period: ClosedRange<Instant>,
        groupBy: ConsumptionTimeFrame,
    ): Result<List<Consumption>> {
        return setConsumptionResponse ?: throw RuntimeException("Fake result setConsumptionResponse not defined")
    }

    var setAccountResponse: Result<Account?>? = null
    override suspend fun getAccount(
        accountNumber: String,
    ): Result<Account?> {
        return setAccountResponse ?: throw RuntimeException("Fake result setAccountResponse not defined")
    }

    var setSmartMeterLiveConsumptionResponse: Result<List<LiveConsumption>>? = null
    override suspend fun getSmartMeterLiveConsumption(meterDeviceId: String, start: Instant, end: Instant): Result<List<LiveConsumption>> {
        return setSmartMeterLiveConsumptionResponse ?: throw RuntimeException("Fake result setSmartMeterLiveConsumptionResponse not defined")
    }

    var setClearCacheException: Throwable? = null
    override suspend fun clearCache() {
        setClearCacheException?.let { throw it }
    }
}
