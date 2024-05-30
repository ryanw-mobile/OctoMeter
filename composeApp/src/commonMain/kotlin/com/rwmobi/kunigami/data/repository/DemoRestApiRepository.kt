/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository

import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataGroup
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataOrder
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.domain.model.product.TariffSummary
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import kotlinx.datetime.Instant
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.time.Duration

class DemoRestApiRepository : RestApiRepository {
    override suspend fun getSimpleProductTariff(productCode: String, tariffCode: String): Result<TariffSummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getProducts(): Result<List<ProductSummary>> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductDetails(productCode: String): Result<ProductDetails> {
        TODO("Not yet implemented")
    }

    override suspend fun getStandardUnitRates(productCode: String, tariffCode: String, periodFrom: Instant?, periodTo: Instant?): Result<List<Rate>> {
        TODO("Not yet implemented")
    }

    override suspend fun getStandingCharges(productCode: String, tariffCode: String): Result<List<Rate>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDayUnitRates(productCode: String, tariffCode: String): Result<List<Rate>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNightUnitRates(productCode: String, tariffCode: String): Result<List<Rate>> {
        TODO("Not yet implemented")
    }

    /**
     * For Usage Screen Demo, we generate random fake data.
     * The only parameters we care are: periodFrom, periodTo, orderBy, groupBy.
     */
    override suspend fun getConsumption(apiKey: String, mpan: String, meterSerialNumber: String, periodFrom: Instant?, periodTo: Instant?, orderBy: ConsumptionDataOrder, groupBy: ConsumptionDataGroup): Result<List<Consumption>> {
        val consumptionList = mutableListOf<Consumption>()
        var intervalStart = periodFrom!! // Instant.parse("2024-04-01T01:00:00Z")
        val duration = Duration.parse("30m")
        val mean = 0.5 // Midpoint of the range [0.110, 2.000]
        val standardDeviation = 0.3167 // Adjust to control the spread of values

        repeat(48) { // 48 intervals of 30 minutes to cover 24 hours
            val intervalEnd = intervalStart.plus(duration)
            var consumption = generateNormalDistribution(mean, standardDeviation)
            // Clamp the values to be within the range [0.110, 2.000]
            consumption = min(max(consumption, 0.110), 1.800)
            consumptionList.add(Consumption(consumption, intervalStart, intervalEnd))
            intervalStart = intervalEnd
        }

        return Result.success(consumptionList)
    }

    fun generateNormalDistribution(mean: Double, standardDeviation: Double): Double {
        // Box-Muller transform to generate a normally distributed random number
        val u1 = Random.nextDouble()
        val u2 = Random.nextDouble()
        val z0 = sqrt(-2.0 * ln(u1)) * cos(2.0 * PI * u2)
        return z0 * standardDeviation + mean
    }

    override suspend fun getAccount(apiKey: String, accountNumber: String): Result<List<Account>> {
        TODO("Not yet implemented")
    }
}
