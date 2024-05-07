/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.roctopus.data.repository

import com.rwmobi.roctopus.data.repository.mapper.toConsumption
import com.rwmobi.roctopus.data.repository.mapper.toProduct
import com.rwmobi.roctopus.data.repository.mapper.toRate
import com.rwmobi.roctopus.data.source.network.ElectricityMeterPointsEndpoint
import com.rwmobi.roctopus.data.source.network.ProductsEndpoint
import com.rwmobi.roctopus.data.source.network.model.ConsumptionGrouping
import com.rwmobi.roctopus.data.source.network.model.ConsumptionOrdering
import com.rwmobi.roctopus.domain.exceptions.except
import com.rwmobi.roctopus.domain.model.Consumption
import com.rwmobi.roctopus.domain.model.Product
import com.rwmobi.roctopus.domain.model.Rate
import com.rwmobi.roctopus.domain.repository.OctopusRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class OctopusRestApiRepository(
    private val productsEndpoint: ProductsEndpoint,
    private val electricityMeterPointsEndpoint: ElectricityMeterPointsEndpoint,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : OctopusRepository {

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
    ): Result<List<Rate>> {
        return withContext(dispatcher) {
            runCatching {
                val apiResponse = productsEndpoint.getStandardUnitRates(
                    productCode = productCode,
                    tariffCode = tariffCode,
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
    ): Result<List<Consumption>> {
        return withContext(dispatcher) {
            runCatching {
                val apiResponse = electricityMeterPointsEndpoint.getConsumption(
                    apiKey = apiKey,
                    mpan = mpan,
                    meterSerialNumber = meterSerialNumber,
                    orderBy = ConsumptionOrdering.PERIOD,
                    groupBy = ConsumptionGrouping.HALF_HOURLY,
                )
                apiResponse?.results?.map { it.toConsumption() } ?: emptyList()
            }.except<CancellationException, _>()
        }
    }
}
