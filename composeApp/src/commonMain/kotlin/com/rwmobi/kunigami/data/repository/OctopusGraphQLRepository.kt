/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository

import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.data.repository.mapper.toAccount
import com.rwmobi.kunigami.data.repository.mapper.toConsumption
import com.rwmobi.kunigami.data.repository.mapper.toConsumptionEntity
import com.rwmobi.kunigami.data.repository.mapper.toProductDetails
import com.rwmobi.kunigami.data.repository.mapper.toProductSummary
import com.rwmobi.kunigami.data.repository.mapper.toRate
import com.rwmobi.kunigami.data.repository.mapper.toRateEntity
import com.rwmobi.kunigami.data.repository.mapper.toTariff
import com.rwmobi.kunigami.data.source.local.cache.InMemoryCacheDataSource
import com.rwmobi.kunigami.data.source.local.database.entity.coversRange
import com.rwmobi.kunigami.data.source.local.database.interfaces.DatabaseDataSource
import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.data.source.network.dto.auth.Token
import com.rwmobi.kunigami.data.source.network.dto.auth.TokenState
import com.rwmobi.kunigami.data.source.network.graphql.GraphQLEndpoint
import com.rwmobi.kunigami.data.source.network.restapi.AccountEndpoint
import com.rwmobi.kunigami.data.source.network.restapi.ElectricityMeterPointsEndpoint
import com.rwmobi.kunigami.data.source.network.restapi.ProductsEndpoint
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.extensions.getHalfHourlyTimeSlotCount
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataOrder
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.domain.repository.OctopusApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException

class OctopusGraphQLRepository(
    private val productsEndpoint: ProductsEndpoint,
    private val electricityMeterPointsEndpoint: ElectricityMeterPointsEndpoint,
    private val accountEndpoint: AccountEndpoint,
    private val inMemoryCacheDataSource: InMemoryCacheDataSource,
    private val databaseDataSource: DatabaseDataSource,
    private val graphQLEndpoint: GraphQLEndpoint,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : OctopusApiRepository {
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

                getRatesFromDatabase(
                    tariffCode = tariffCode,
                    rateType = RateType.STANDARD_UNIT_RATE,
                    validity = period,
                    paymentMethod = PaymentMethod.UNKNOWN, // TODO: Not work for flexible tariffs
                ) ?: run {
                    Logger.v(tag = "getStandardUnitRates", messageString = "DB Cache misses for $period")
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

                        // cache the results
                        apiResponse?.results?.map {
                            it.toRateEntity(tariffCode = tariffCode, rateType = RateType.STANDARD_UNIT_RATE)
                        }?.let {
                            databaseDataSource.insertRates(rateEntity = it)
                        }

                        page = apiResponse?.getNextPageNumber()
                    } while (page != null)

                    combinedList
                }
            }.except<CancellationException, _>()
        }
    }

    /**
     * Since rates do not necessary to be in half-hourly,
     * we have to loop through the data set to see if it covers the entire range
     */
    private suspend fun getRatesFromDatabase(
        tariffCode: String,
        rateType: RateType,
        validity: ClosedRange<Instant>,
        paymentMethod: PaymentMethod,
    ): List<Rate>? {
        val cachedEntries = databaseDataSource.getRates(
            tariffCode = tariffCode,
            rateType = rateType,
            validity = validity,
            paymentMethod = paymentMethod,
        )

        val isCompleteDataSet = cachedEntries.coversRange(validFrom = validity.start, validTo = validity.endInclusive)

        return if (isCompleteDataSet) {
            cachedEntries.map { it.toRate() }
        } else {
            null
        }
    }

    /***
     * This API supports paging. Every page contains at most 100 records.
     * Supply `requestedPage` to specify a page.
     * Otherwise, this function will retrieve all possible data the backend can provide.
     */
    override suspend fun getStandingCharges(
        tariffCode: String,
        paymentMethod: PaymentMethod,
        period: ClosedRange<Instant>?,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                val productCode = Tariff.extractProductCode(tariffCode = tariffCode)
                requireNotNull(productCode) { "Unable to resolve product code for $tariffCode" }

                getRatesFromDatabase(
                    tariffCode = tariffCode,
                    rateType = RateType.STANDING_CHARGE,
                    validity = period ?: Instant.DISTANT_PAST..Instant.DISTANT_FUTURE,
                    paymentMethod = paymentMethod,
                ) ?: run {
                    Logger.v(tag = "getStandingCharges", messageString = "DB Cache misses for $period")
                    val combinedList = mutableListOf<Rate>()
                    var page: Int? = requestedPage
                    do {
                        val apiResponse = productsEndpoint.getStandingCharges(
                            productCode = productCode,
                            tariffCode = tariffCode,
                            periodFrom = period?.start,
                            periodTo = period?.endInclusive,
                            page = page,
                        )
                        combinedList.addAll(apiResponse?.results?.map { it.toRate() } ?: emptyList())

                        // cache the results
                        apiResponse?.results?.map {
                            it.toRateEntity(tariffCode = tariffCode, rateType = RateType.STANDING_CHARGE)
                        }?.let {
                            databaseDataSource.insertRates(rateEntity = it)
                        }

                        page = apiResponse?.getNextPageNumber()
                    } while (page != null)

                    combinedList
                }
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
        period: ClosedRange<Instant>?,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                val productCode = Tariff.extractProductCode(tariffCode = tariffCode)
                requireNotNull(productCode) { "Unable to resolve product code for $tariffCode" }

                getRatesFromDatabase(
                    tariffCode = tariffCode,
                    rateType = RateType.DAY_UNIT_RATE,
                    validity = period ?: Instant.DISTANT_PAST..Instant.DISTANT_FUTURE,
                    paymentMethod = PaymentMethod.UNKNOWN, // TODO: Not work for flexible tariffs
                ) ?: run {
                    Logger.v(tag = "getDayUnitRates", messageString = "DB Cache misses for $period")
                    val combinedList = mutableListOf<Rate>()
                    var page: Int? = requestedPage
                    do {
                        val apiResponse = productsEndpoint.getDayUnitRates(
                            productCode = productCode,
                            tariffCode = tariffCode,
                            periodFrom = period?.start,
                            periodTo = period?.endInclusive,
                            page = page,
                        )
                        combinedList.addAll(apiResponse?.results?.map { it.toRate() } ?: emptyList())

                        // cache the results
                        apiResponse?.results?.map {
                            it.toRateEntity(tariffCode = tariffCode, rateType = RateType.DAY_UNIT_RATE)
                        }?.let {
                            databaseDataSource.insertRates(rateEntity = it)
                        }

                        page = apiResponse?.getNextPageNumber()
                    } while (page != null)

                    combinedList
                }
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
        period: ClosedRange<Instant>?,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                val productCode = Tariff.extractProductCode(tariffCode = tariffCode)
                requireNotNull(productCode) { "Unable to resolve product code for $tariffCode" }

                getRatesFromDatabase(
                    tariffCode = tariffCode,
                    rateType = RateType.NIGHT_UNIT_RATE,
                    validity = period ?: Instant.DISTANT_PAST..Instant.DISTANT_FUTURE,
                    paymentMethod = PaymentMethod.UNKNOWN, // TODO: Not work for flexible tariffs
                ) ?: run {
                    Logger.v(tag = "getNightUnitRates", messageString = "DB Cache misses for $period")
                    val combinedList = mutableListOf<Rate>()
                    var page: Int? = requestedPage
                    do {
                        val apiResponse = productsEndpoint.getNightUnitRates(
                            productCode = productCode,
                            tariffCode = tariffCode,
                            periodFrom = period?.start,
                            periodTo = period?.endInclusive,
                            page = page,
                        )
                        combinedList.addAll(apiResponse?.results?.map { it.toRate() } ?: emptyList())

                        // cache the results
                        apiResponse?.results?.map {
                            it.toRateEntity(tariffCode = tariffCode, rateType = RateType.NIGHT_UNIT_RATE)
                        }?.let {
                            databaseDataSource.insertRates(rateEntity = it)
                        }

                        page = apiResponse?.getNextPageNumber()
                    } while (page != null)

                    combinedList
                }
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
                // cache: if half-hourly, calculate the expected number of entries and see if the cache has them
                getConsumptionsFromDatabase(
                    meterSerialNumber = meterSerialNumber,
                    period = period,
                    groupBy = groupBy,
                ) ?: run {
                    Logger.v(tag = "getConsumption", messageString = "DB Cache misses for $period")
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

                        // cache the results - only for half-hourly
                        if (groupBy == ConsumptionTimeFrame.HALF_HOURLY) {
                            apiResponse?.results?.map { it.toConsumptionEntity(meterSerial = meterSerialNumber) }?.let {
                                databaseDataSource.insertConsumptions(consumptionEntity = it)
                            }
                        }

                        page = apiResponse?.getNextPageNumber()
                    } while (page != null)

                    combinedList
                }
            }.except<CancellationException, _>()
        }
    }

    private suspend fun getConsumptionsFromDatabase(
        meterSerialNumber: String,
        period: ClosedRange<Instant>,
        groupBy: ConsumptionTimeFrame,
    ): List<Consumption>? {
        if (groupBy != ConsumptionTimeFrame.HALF_HOURLY) {
            return null
        }

        val cachedEntries = databaseDataSource.getConsumptions(
            meterSerial = meterSerialNumber,
            interval = period,
        )
        return if (cachedEntries.size == period.getHalfHourlyTimeSlotCount()) {
            cachedEntries.map { it.toConsumption() }
        } else {
            null
        }
    }

    /***
     * API can potentially return more than one property for a given account number.
     * We have no way to tell if this is the case, but for simplicity, we take the first property only.
     */
    override suspend fun getAccount(
        apiKey: String,
        accountNumber: String,
    ): Result<Account?> {
        // inMemoryCacheDataSource.getProfile(accountNumber = accountNumber)?.let { return Result.success(it) }

        return withContext(dispatcher) {
            runCatching {
                val token = getToken(apiKey = apiKey, forceRefresh = false)
                Logger.v("Token = ${token?.token}, ${token?.getTokenState()}")

                val apiResponse = accountEndpoint.getAccount(
                    apiKey = apiKey,
                    accountNumber = accountNumber,
                )
                apiResponse?.properties?.firstOrNull()?.toAccount(accountNumber = accountNumber)?.also {
                    inMemoryCacheDataSource.cacheProfile(account = it, createdAt = Clock.System.now())
                }
            }
        }.except<CancellationException, _>()
    }

    override suspend fun clearCache() {
        inMemoryCacheDataSource.clear()
        databaseDataSource.clear()
    }

    // Token Management

    /**
     * Get the cached token associated to the apiKey, or get a new one.
     * If forceRefresh is true and still getting a null token, caller should fail the request.
     */
    private suspend fun getToken(apiKey: String, forceRefresh: Boolean): Token? {
        val cachedToken = inMemoryCacheDataSource.getToken(apiKey = apiKey)

        return when {
            forceRefresh ||
                cachedToken == null ||
                (cachedToken.getTokenState() == TokenState.REFRESH && cachedToken.refreshToken == null)
            -> {
                // We need a new token
                Logger.v(tag = "getToken", messageString = "requesting a new token")
                return graphQLEndpoint.getAuthorizationToken(apiKey = apiKey).getOrNull()?.also {
                    inMemoryCacheDataSource.cacheToken(apiKey = apiKey, token = it)
                }
            }

            cachedToken.getTokenState() == TokenState.REFRESH && cachedToken.refreshToken != null -> {
                Logger.v(tag = "getToken", messageString = "refreshing the token")
                return graphQLEndpoint.refreshAuthorizationToken(refreshToken = cachedToken.refreshToken).getOrNull()?.also {
                    inMemoryCacheDataSource.cacheToken(apiKey = apiKey, token = it)
                }
            }

            else -> {
                // Should be a valid token. If it doesn't work, caller should generate a new token
                Logger.v(tag = "getToken", messageString = "returning the cached token")
                cachedToken
            }
        }
    }
}
