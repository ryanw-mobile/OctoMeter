/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository

import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionDataOrder
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.domain.model.product.TariffSummary
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.domain.repository.RestApiRepository
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.until
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random

class DemoRestApiRepository : RestApiRepository {
    override suspend fun getSimpleProductTariff(productCode: String, tariffCode: String): Result<TariffSummary> {
        throw NotImplementedError("Disabled in demo mode")
    }

    override suspend fun getProducts(): Result<List<ProductSummary>> {
        throw NotImplementedError("Disabled in demo mode")
    }

    override suspend fun getProductDetails(productCode: String): Result<ProductDetails> {
        throw NotImplementedError("Disabled in demo mode")
    }

    override suspend fun getStandardUnitRates(productCode: String, tariffCode: String, period: ClosedRange<Instant>): Result<List<Rate>> {
        throw NotImplementedError("Disabled in demo mode")
    }

    override suspend fun getStandingCharges(productCode: String, tariffCode: String): Result<List<Rate>> {
        throw NotImplementedError("Disabled in demo mode")
    }

    override suspend fun getDayUnitRates(productCode: String, tariffCode: String): Result<List<Rate>> {
        throw NotImplementedError("Disabled in demo mode")
    }

    override suspend fun getNightUnitRates(productCode: String, tariffCode: String): Result<List<Rate>> {
        throw NotImplementedError("Disabled in demo mode")
    }

    /**
     * For Usage Screen Demo, we generate random fake data.
     * The only parameters we care are: periodFrom, periodTo, groupBy.
     * Since this piece of code has no practical value, we take it as iss - until it breaks.
     */
    override suspend fun getConsumption(
        apiKey: String,
        mpan: String,
        meterSerialNumber: String,
        period: ClosedRange<Instant>,
        orderBy: ConsumptionDataOrder,
        groupBy: ConsumptionTimeFrame,
    ): Result<List<Consumption>> {
        val consumptionList = mutableListOf<Consumption>()
        var intervalStart = period.start
        val mean = 0.2 // Midpoint of the range [0.110, 2.000]
        val standardDeviation = 0.2167 // Adjust to control the spread of values
        val timeZone = TimeZone.currentSystemDefault()
        val baseDurationMinutes = 30L // base duration in minutes for half-hourly intervals

        while (intervalStart < period.endInclusive) {
            val intervalEnd = when (groupBy) {
                ConsumptionTimeFrame.HALF_HOURLY -> intervalStart.plus(DateTimePeriod(minutes = 30), timeZone)
                ConsumptionTimeFrame.DAY -> intervalStart.plus(DateTimePeriod(days = 1), timeZone)
                ConsumptionTimeFrame.WEEK -> intervalStart.plus(DateTimePeriod(days = 7), timeZone)
                ConsumptionTimeFrame.MONTH -> intervalStart.plus(DateTimePeriod(months = 1), timeZone)
                ConsumptionTimeFrame.QUARTER -> throw NotImplementedError("Disabled in demo mode")
            }

            val intervalDurationMinutes = intervalStart.until(intervalEnd, DateTimeUnit.MINUTE)
            val intervalFactor = intervalDurationMinutes.toDouble() / baseDurationMinutes
            var consumption = generateNormalDistribution(mean, standardDeviation)
            consumption = min(max(consumption, 0.05), 1.5) * intervalFactor

            consumptionList.add(Consumption(consumption, intervalStart, intervalEnd))
            intervalStart = intervalEnd
        }

        return Result.success(consumptionList)
    }

    private fun generateNormalDistribution(mean: Double, standardDeviation: Double): Double {
        // Box-Muller transform to generate a normally distributed random number
        val u1 = Random.nextDouble()
        val u2 = Random.nextDouble()
        val z0 = sqrt(-2.0 * ln(u1)) * cos(2.0 * PI * u2)
        return z0 * standardDeviation + mean
    }

    override suspend fun getAccount(apiKey: String, accountNumber: String): Result<Account?> {
        throw NotImplementedError("Disabled in demo mode")
    }
}
