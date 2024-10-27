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
import com.rwmobi.kunigami.data.source.network.graphql.interfaces.GraphQLEndpoint
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
    private val inMemoryCacheDataSource: InMemoryCacheDataSource,
    private val databaseDataSource: DatabaseDataSource,
    private val graphQLEndpoint: GraphQLEndpoint,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : OctopusApiRepository {

    /***
     * Get a single tariff.
     * This API supports paging, but we expect only one record,
     * so we do not follow the cursor even when it is available.
     */
    override suspend fun getTariff(
        tariffCode: String,
    ): Result<Tariff> {
        inMemoryCacheDataSource.getTariff(tariffCode)?.let {
            return Result.success(it)
        }

        return withContext(dispatcher) {
            runCatching {
                val productCode = Tariff.extractProductCode(tariffCode = tariffCode)
                requireNotNull(productCode) { "Unable to resolve product code for $tariffCode" }

                val postcode = Tariff.getRetailRegion(tariffCode = tariffCode)?.postcode
                requireNotNull(postcode) { "Unable to resolve retail region for $tariffCode" }

                val response = graphQLEndpoint.getSingleEnergyProduct(
                    productCode = productCode,
                    postcode = postcode,
                )

                val tariff = response.energyProduct?.toTariff(tariffCode = tariffCode) ?: throw IllegalArgumentException("Unable to retrieve base product $productCode")

                inMemoryCacheDataSource.cacheTariff(tariffCode = tariffCode, tariff = tariff)
                tariff
            }.except<CancellationException, _>()
        }
    }

    /***
     * This API supports paging, and will retrieve all possible data the backend can provide.
     * This call has in-memory caching tided to the last postcode provided.
     */
    override suspend fun getProducts(
        postcode: String,
    ): Result<List<ProductSummary>> {
        inMemoryCacheDataSource.getProductSummary(postcode)?.let {
            return Result.success(it)
        }

        return withContext(dispatcher) {
            runCatching {
                val combinedList = mutableListOf<ProductSummary>()
                var afterCursor: String? = null
                do {
                    val response = graphQLEndpoint.getEnergyProducts(
                        postcode = postcode,
                        afterCursor = afterCursor,
                    )
                    combinedList.addAll(
                        response.energyProducts?.edges?.mapNotNull { it?.node?.toProductSummary() }
                            ?: emptyList(),
                    )

                    afterCursor = if (response.energyProducts?.pageInfo?.hasNextPage == true) {
                        response.energyProducts.pageInfo.endCursor
                    } else {
                        null
                    }
                } while (afterCursor != null)

                inMemoryCacheDataSource.cacheProductSummary(
                    postcode = postcode,
                    productSummaries = combinedList,
                )
                combinedList
            }.except<CancellationException, _>()
        }
    }

    override suspend fun getProductDetails(
        productCode: String,
        postcode: String,
    ): Result<ProductDetails> {
        inMemoryCacheDataSource.getProductDetails(
            postcode = postcode,
            productCode = productCode,
        )?.let {
            return Result.success(it)
        }

        return withContext(dispatcher) {
            runCatching {
                val response = graphQLEndpoint.getSingleEnergyProduct(
                    productCode = productCode,
                    postcode = postcode,
                )

                val productDetails = response.energyProduct?.toProductDetails() ?: throw IllegalArgumentException("Unable to retrieve base product $productCode")

                inMemoryCacheDataSource.cacheProductDetails(
                    postcode = postcode,
                    productCode = productCode,
                    productDetails = productDetails,
                )
                productDetails
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
        accountNumber: String,
    ): Result<Account?> {
        inMemoryCacheDataSource.getProfile(accountNumber = accountNumber)?.let { return Result.success(it) }

        return withContext(dispatcher) {
            runCatching {
                val response = graphQLEndpoint.getAccount(
                    accountNumber = accountNumber,
                )

                val preferredName = response.account?.users?.firstOrNull()?.preferredName
                response.properties?.firstOrNull()?.toAccount(
                    accountNumber = accountNumber,
                    preferredName = preferredName,
                )
                    ?.also {
                        inMemoryCacheDataSource.cacheProfile(account = it, createdAt = Clock.System.now())
                    }
            }
        }.except<CancellationException, _>()
    }

    override suspend fun clearCache() {
        inMemoryCacheDataSource.clear()
        databaseDataSource.clear()
    }
}
