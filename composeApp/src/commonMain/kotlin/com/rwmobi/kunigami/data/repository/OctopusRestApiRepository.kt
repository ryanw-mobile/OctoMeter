/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository

import com.rwmobi.kunigami.data.repository.mapper.toAccount
import com.rwmobi.kunigami.data.repository.mapper.toConsumption
import com.rwmobi.kunigami.data.repository.mapper.toProduct
import com.rwmobi.kunigami.data.repository.mapper.toRate
import com.rwmobi.kunigami.data.repository.mapper.toTariff
import com.rwmobi.kunigami.data.source.network.AccountEndpoint
import com.rwmobi.kunigami.data.source.network.ElectricityMeterPointsEndpoint
import com.rwmobi.kunigami.data.source.network.ProductsEndpoint
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.domain.model.Tariff
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataGroup
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataOrder
import com.rwmobi.kunigami.domain.model.product.Product
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException

class OctopusRestApiRepository(
    private val productsEndpoint: ProductsEndpoint,
    private val electricityMeterPointsEndpoint: ElectricityMeterPointsEndpoint,
    private val accountEndpoint: AccountEndpoint,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : RestApiRepository {
    override suspend fun getSimpleProductTariff(
        productCode: String,
        tariffCode: String,
    ): Result<Tariff> {
        return withContext(dispatcher) {
            runCatching {
                val apiResponse = productsEndpoint.getProduct(
                    productCode = productCode,
                )

                apiResponse?.toTariff(tariffCode = tariffCode) ?: throw IllegalArgumentException("Cannot parse tariff")
            }.except<CancellationException, _>()
        }
    }

    // Don't forget that if it returns more than 100 records, you will have to look at page=2 for the subsequent entries.
    override suspend fun getProducts(): Result<List<Product>> {
        return withContext(dispatcher) {
            runCatching {
                val apiResponse = productsEndpoint.getProducts()
                apiResponse?.results?.map { it.toProduct() } ?: emptyList()
            }.except<CancellationException, _>()
        }
    }

    override suspend fun getStandardUnitRates(
        productCode: String,
        tariffCode: String,
        periodFrom: Instant?,
        periodTo: Instant?,
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                val apiResponse = productsEndpoint.getStandardUnitRates(
                    productCode = productCode,
                    tariffCode = tariffCode,
                    periodFrom = periodFrom,
                    periodTo = periodTo,
                )
                apiResponse?.results?.map { it.toRate() } ?: emptyList()
            }.except<CancellationException, _>()
        }
    }

    override suspend fun getStandingCharges(
        productCode: String,
        tariffCode: String,
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                val apiResponse = productsEndpoint.getStandingCharges(
                    productCode = productCode,
                    tariffCode = tariffCode,
                )
                apiResponse?.results?.map { it.toRate() } ?: emptyList()
            }.except<CancellationException, _>()
        }
    }

    override suspend fun getDayUnitRates(
        productCode: String,
        tariffCode: String,
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                val apiResponse = productsEndpoint.getDayUnitRates(
                    productCode = productCode,
                    tariffCode = tariffCode,
                )
                apiResponse?.results?.map { it.toRate() } ?: emptyList()
            }.except<CancellationException, _>()
        }
    }

    override suspend fun getNightUnitRates(
        productCode: String,
        tariffCode: String,
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                val apiResponse = productsEndpoint.getNightUnitRates(
                    productCode = productCode,
                    tariffCode = tariffCode,
                )
                apiResponse?.results?.map { it.toRate() } ?: emptyList()
            }.except<CancellationException, _>()
        }
    }

    override suspend fun getConsumption(
        apiKey: String,
        mpan: String,
        meterSerialNumber: String,
        periodFrom: Instant?,
        periodTo: Instant?,
        orderBy: ConsumptionDataOrder,
        groupBy: ConsumptionDataGroup,
    ): Result<List<Consumption>> {
        return withContext(dispatcher) {
            runCatching {
                val apiResponse = electricityMeterPointsEndpoint.getConsumption(
                    apiKey = apiKey,
                    mpan = mpan,
                    periodFrom = periodFrom,
                    periodTo = periodTo,
                    meterSerialNumber = meterSerialNumber,
                    orderBy = orderBy.apiValue,
                    groupBy = groupBy.apiValue,
                )
                apiResponse?.results?.map { it.toConsumption() } ?: emptyList()
            }.except<CancellationException, _>()
        }
    }

    override suspend fun getAccount(
        apiKey: String,
        accountNumber: String,
    ): Result<List<Account>> {
        return withContext(dispatcher) {
            runCatching {
                val apiResponse = accountEndpoint.getAccount(
                    apiKey = apiKey,
                    accountNumber = accountNumber,
                )
                apiResponse?.properties?.map { it.toAccount(accountNumber = accountNumber) } ?: emptyList()
            }.except<CancellationException, _>()
        }
    }
}
