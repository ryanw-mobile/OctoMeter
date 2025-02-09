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

import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.consumption.Consumption
import com.rwmobi.kunigami.domain.model.consumption.ConsumptionTimeFrame
import com.rwmobi.kunigami.domain.model.consumption.LiveConsumption
import com.rwmobi.kunigami.domain.model.product.ProductDetails
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import com.rwmobi.kunigami.domain.model.product.Tariff
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import com.rwmobi.kunigami.domain.model.rate.Rate
import com.rwmobi.kunigami.domain.repository.OctopusApiRepository
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

class DemoOctopusApiRepository : OctopusApiRepository {
    private val defaultException = NotImplementedError("Disabled in demo mode")

    override suspend fun getTariff(tariffCode: String): Result<Tariff> {
        throw defaultException
    }

    override suspend fun getProducts(postcode: String): Result<List<ProductSummary>> {
        throw defaultException
    }

    override suspend fun getProductDetails(
        productCode: String,
        postcode: String,
    ): Result<ProductDetails> {
        throw defaultException
    }

    override suspend fun getStandardUnitRates(
        tariffCode: String,
        period: ClosedRange<Instant>,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        throw defaultException
    }

    override suspend fun getStandingCharges(
        tariffCode: String,
        paymentMethod: PaymentMethod,
        period: ClosedRange<Instant>?,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        throw defaultException
    }

    override suspend fun getDayUnitRates(
        tariffCode: String,
        period: ClosedRange<Instant>?,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        throw defaultException
    }

    override suspend fun getNightUnitRates(
        tariffCode: String,
        period: ClosedRange<Instant>?,
        requestedPage: Int?,
    ): Result<List<Rate>> {
        throw defaultException
    }

    /**
     * For Usage Screen Demo, we generate random fake data.
     * The only parameters we care are: periodFrom, periodTo, groupBy.
     * Since this piece of code has no practical value, we take it as iss - until it breaks.
     */
    override suspend fun getConsumption(
        accountNumber: String,
        meterSerialNumber: String,
        deviceId: String,
        mpan: String,
        period: ClosedRange<Instant>,
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
                ConsumptionTimeFrame.QUARTER -> throw defaultException
            }

            val intervalDurationMinutes = intervalStart.until(intervalEnd, DateTimeUnit.MINUTE)
            val intervalFactor = intervalDurationMinutes.toDouble() / baseDurationMinutes
            var consumption = generateNormalDistribution(mean, standardDeviation)
            consumption = min(max(consumption, 0.05), 1.5) * intervalFactor

            consumptionList.add(
                Consumption(
                    kWhConsumed = consumption,
                    interval = intervalStart..intervalEnd,
                ),
            )
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

    override suspend fun getSmartMeterLiveConsumption(meterDeviceId: String, start: Instant, end: Instant): Result<List<LiveConsumption>> {
        // Demo mode does not come with meter reading simulation
        throw defaultException
    }

    override suspend fun getAccount(accountNumber: String): Result<Account?> {
        throw defaultException
    }

    override suspend fun clearCache() {
        throw defaultException
    }
}
