/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository

import com.rwmobi.kunigami.data.repository.mapper.toAccount
import com.rwmobi.kunigami.data.repository.mapper.toConsumption
import com.rwmobi.kunigami.data.repository.mapper.toProductDetails
import com.rwmobi.kunigami.data.repository.mapper.toProductSummary
import com.rwmobi.kunigami.data.repository.mapper.toRate
import com.rwmobi.kunigami.data.repository.mapper.toTariff
import com.rwmobi.kunigami.data.source.network.AccountEndpoint
import com.rwmobi.kunigami.data.source.network.ElectricityMeterPointsEndpoint
import com.rwmobi.kunigami.data.source.network.ProductsEndpoint
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.extensions.toSystemDefaultLocalDate
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataOrder
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException

class OctopusRestApiRepository(
    private val productsEndpoint: ProductsEndpoint,
    private val electricityMeterPointsEndpoint: ElectricityMeterPointsEndpoint,
    private val accountEndpoint: AccountEndpoint,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : RestApiRepository {
    override suspend fun getTariff(
        tariffCode: String,
    ): Result<Tariff> {
        return withContext(dispatcher) {
            runCatching {
                val productCode = Tariff.extractProductCode(tariffCode = tariffCode)
                requireNotNull(productCode) { "Unable to resolve product code for $tariffCode" }

                val apiResponse = productsEndpoint.getProduct(productCode = productCode)
                apiResponse?.toTariff(tariffCode = tariffCode) ?: throw IllegalArgumentException("Unable to retrieve base product $productCode")
            }.except<CancellationException, _>()
        }
    }

    /***
     * This API supports paging. Every page contains at most 100 records.
     * Supply `requestedPage` to specify a page.
     * Otherwise, this function will retrieve all possible data the backend can provide.
     */
    override suspend fun getProducts(
        requestedPage: Int?,
    ): Result<List<ProductSummary>> {
        return withContext(dispatcher) {
            runCatching {
                val combinedList = mutableListOf<ProductSummary>()
                var page: Int? = requestedPage
                do {
                    val apiResponse = productsEndpoint.getProducts(page = page)
                    combinedList.addAll(apiResponse?.results?.map { it.toProductSummary() } ?: emptyList())
                    page = apiResponse?.getNextPageNumber()
                } while (page != null)

                combinedList
            }.except<CancellationException, _>()
        }
    }

    override suspend fun getProductDetails(
        productCode: String,
    ): Result<ProductDetails> {
        return withContext(dispatcher) {
            runCatching {
                val apiResponse = productsEndpoint.getProduct(productCode = productCode)
                apiResponse?.toProductDetails() ?: throw IllegalArgumentException("Unable to retrieve base product $productCode")
            }.except<CancellationException, _>()
        }
    }

    /***
     * This API supports paging. Every page contains at most 100 records.
     * Supply `requestedPage` to specify a page.
     * Otherwise, this function will retrieve all possible data the backend can provide.
     */
    override suspend fun getStandardUnitRates(
        tariffCode: String,
        period: ClosedRange<Instant>,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                val productCode = Tariff.extractProductCode(tariffCode = tariffCode)
                requireNotNull(productCode) { "Unable to resolve product code for $tariffCode" }

                val combinedList = mutableListOf<Rate>()
                var page: Int? = requestedPage
                do {
                    val apiResponse = productsEndpoint.getStandardUnitRates(
                        productCode = productCode,
                        tariffCode = tariffCode,
                        periodFrom = period.start,
                        periodTo = period.endInclusive,
                        page = page,
                    )
                    combinedList.addAll(apiResponse?.results?.map { it.toRate() } ?: emptyList())
                    page = apiResponse?.getNextPageNumber()
                } while (page != null)

                combinedList
            }.except<CancellationException, _>()
        }
    }

    /***
     * This API supports paging. Every page contains at most 100 records.
     * Supply `requestedPage` to specify a page.
     * Otherwise, this function will retrieve all possible data the backend can provide.
     */
    override suspend fun getStandingCharges(
        tariffCode: String,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                val productCode = Tariff.extractProductCode(tariffCode = tariffCode)
                requireNotNull(productCode) { "Unable to resolve product code for $tariffCode" }

                val combinedList = mutableListOf<Rate>()
                var page: Int? = requestedPage
                do {
                    val apiResponse = productsEndpoint.getStandingCharges(
                        productCode = productCode,
                        tariffCode = tariffCode,
                        page = page,
                    )
                    combinedList.addAll(apiResponse?.results?.map { it.toRate() } ?: emptyList())
                    page = apiResponse?.getNextPageNumber()
                } while (page != null)

                combinedList
            }.except<CancellationException, _>()
        }
    }

    /***
     * This API supports paging. Every page contains at most 100 records.
     * Supply `requestedPage` to specify a page.
     * Otherwise, this function will retrieve all possible data the backend can provide.
     */
    override suspend fun getDayUnitRates(
        tariffCode: String,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                val productCode = Tariff.extractProductCode(tariffCode = tariffCode)
                requireNotNull(productCode) { "Unable to resolve product code for $tariffCode" }

                val combinedList = mutableListOf<Rate>()
                var page: Int? = requestedPage
                do {
                    val apiResponse = productsEndpoint.getDayUnitRates(
                        productCode = productCode,
                        tariffCode = tariffCode,
                        page = page,
                    )
                    combinedList.addAll(apiResponse?.results?.map { it.toRate() } ?: emptyList())
                    page = apiResponse?.getNextPageNumber()
                } while (page != null)

                combinedList
            }.except<CancellationException, _>()
        }
    }

    /***
     * This API supports paging. Every page contains at most 100 records.
     * Supply `requestedPage` to specify a page.
     * Otherwise, this function will retrieve all possible data the backend can provide.
     */
    override suspend fun getNightUnitRates(
        tariffCode: String,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                val productCode = Tariff.extractProductCode(tariffCode = tariffCode)
                requireNotNull(productCode) { "Unable to resolve product code for $tariffCode" }

                val combinedList = mutableListOf<Rate>()
                var page: Int? = requestedPage
                do {
                    val apiResponse = productsEndpoint.getNightUnitRates(
                        productCode = productCode,
                        tariffCode = tariffCode,
                        page = page,
                    )
                    combinedList.addAll(apiResponse?.results?.map { it.toRate() } ?: emptyList())
                    page = apiResponse?.getNextPageNumber()
                } while (page != null)

                combinedList
            }.except<CancellationException, _>()
        }
    }

    /***
     * This API supports paging. Every page contains at most 100 records.
     * Supply `requestedPage` to specify a page.
     * Otherwise, this function will retrieve all possible data the backend can provide.
     */
    override suspend fun getConsumption(
        apiKey: String,
        mpan: String,
        meterSerialNumber: String,
        period: ClosedRange<Instant>,
        orderBy: ConsumptionDataOrder,
        groupBy: ConsumptionTimeFrame,
        requestedPage: Int?,
    ): Result<List<Consumption>> {
        return withContext(dispatcher) {
            runCatching {
                val combinedList = mutableListOf<Consumption>()
                var page: Int? = requestedPage
                do {
                    val apiResponse = electricityMeterPointsEndpoint.getConsumption(
                        apiKey = apiKey,
                        mpan = mpan,
                        periodFrom = period.start,
                        periodTo = period.endInclusive,
                        meterSerialNumber = meterSerialNumber,
                        orderBy = orderBy.apiValue,
                        groupBy = groupBy.apiValue,
                        page = page,
                    )
                    combinedList.addAll(apiResponse?.results?.map { it.toConsumption() } ?: emptyList())
                    page = apiResponse?.getNextPageNumber()
                } while (page != null)

                combinedList
            }.except<CancellationException, _>()
        }
    }

    /***
     * API can potentially return more than one property for a given account number.
     * We have no way to tell if this is the case, but for simplicity, we take the first property only.
     */
    private var cachedProfile: Pair<Account, Instant>? = null
    override suspend fun getAccount(
        apiKey: String,
        accountNumber: String,
    ): Result<Account?> {
        cachedProfile?.first?.let { account ->
            if (account.accountNumber == accountNumber &&
                Clock.System.now().toSystemDefaultLocalDate() == cachedProfile?.second?.toSystemDefaultLocalDate()
            ) {
                return Result.success(account)
            }
        }

        return withContext(dispatcher) {
            runCatching {
                val apiResponse = accountEndpoint.getAccount(
                    apiKey = apiKey,
                    accountNumber = accountNumber,
                )
                apiResponse?.properties?.firstOrNull()?.toAccount(accountNumber = accountNumber)?.also {
                    cachedProfile = Pair(it, Clock.System.now())
                }
            }
        }.except<CancellationException, _>()
    }
}
