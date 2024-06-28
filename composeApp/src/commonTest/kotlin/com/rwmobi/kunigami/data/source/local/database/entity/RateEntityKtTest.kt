/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.database.entity

import com.rwmobi.kunigami.data.source.local.database.model.RateType
import com.rwmobi.kunigami.domain.model.rate.PaymentMethod
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RateEntityKtTest {

    @Test
    fun `coversRange should return true for consecutive overlapping rates covering the range`() {
        val rates = listOf(
            RateEntity(
                tariffCode = "tariff1",
                rateType = RateType.STANDING_CHARGE,
                paymentMethod = PaymentMethod.DIRECT_DEBIT,
                validFrom = Instant.parse("2024-01-01T00:00:00Z"),
                validTo = Instant.parse("2024-01-31T23:59:59Z"),
                vatRate = 20.0,
            ),
            RateEntity(
                tariffCode = "tariff1",
                rateType = RateType.STANDING_CHARGE,
                paymentMethod = PaymentMethod.DIRECT_DEBIT,
                validFrom = Instant.parse("2024-01-31T00:00:00Z"),
                validTo = Instant.parse("2024-02-28T23:59:59Z"),
                vatRate = 20.0,
            ),
        )
        val validFrom = Instant.parse("2024-01-01T00:00:00Z")
        val validTo = Instant.parse("2024-02-28T23:59:59Z")

        assertTrue(rates.coversRange(validFrom = validFrom, validTo = validTo))
    }

    @Test
    fun `coversRange should return false for non-consecutive rates`() {
        val rates = listOf(
            RateEntity(
                tariffCode = "tariff1",
                rateType = RateType.STANDING_CHARGE,
                paymentMethod = PaymentMethod.DIRECT_DEBIT,
                validFrom = Instant.parse("2024-01-01T00:00:00Z"),
                validTo = Instant.parse("2024-01-15T23:59:59Z"),
                vatRate = 20.0,
            ),
            RateEntity(
                tariffCode = "tariff1",
                rateType = RateType.STANDING_CHARGE,
                paymentMethod = PaymentMethod.DIRECT_DEBIT,
                validFrom = Instant.parse("2024-02-01T00:00:00Z"),
                validTo = Instant.parse("2024-02-28T23:59:59Z"),
                vatRate = 20.0,
            ),
        )
        val validFrom = Instant.parse("2024-01-01T00:00:00Z")
        val validTo = Instant.parse("2024-02-28T23:59:59Z")

        assertFalse(rates.coversRange(validFrom = validFrom, validTo = validTo))
    }

    @Test
    fun `coversRange should return true for rates ending in distant future`() {
        val rates = listOf(
            RateEntity(
                tariffCode = "tariff1",
                rateType = RateType.STANDING_CHARGE,
                paymentMethod = PaymentMethod.DIRECT_DEBIT,
                validFrom = Instant.parse("2024-01-01T00:00:00Z"),
                validTo = Instant.parse("2024-01-31T23:59:59Z"),
                vatRate = 20.0,
            ),
            RateEntity(
                tariffCode = "tariff1",
                rateType = RateType.STANDING_CHARGE,
                paymentMethod = PaymentMethod.DIRECT_DEBIT,
                validFrom = Instant.parse("2024-01-31T00:00:00Z"),
                validTo = null,
                vatRate = 20.0,
            ),
        )
        val validFrom = Instant.parse("2024-01-01T00:00:00Z")
        val validTo = Instant.parse("2024-02-15T23:59:59Z")

        assertTrue(rates.coversRange(validFrom = validFrom, validTo = validTo))
    }

    @Test
    fun `coversRange should return false for empty rates list`() {
        val rates = emptyList<RateEntity>()
        val validFrom = Instant.parse("2024-01-01T00:00:00Z")
        val validTo = Instant.parse("2024-01-31T23:59:59Z")

        assertFalse(rates.coversRange(validFrom = validFrom, validTo = validTo))
    }

    @Test
    fun `coversRange should return true for single rate covering the range`() {
        val rates = listOf(
            RateEntity(
                tariffCode = "tariff1",
                rateType = RateType.STANDING_CHARGE,
                paymentMethod = PaymentMethod.DIRECT_DEBIT,
                validFrom = Instant.parse("2024-01-01T00:00:00Z"),
                validTo = Instant.parse("2024-12-31T23:59:59Z"),
                vatRate = 20.0,
            ),
        )
        val validFrom = Instant.parse("2024-01-01T00:00:00Z")
        val validTo = Instant.parse("2024-12-31T23:59:59Z")

        assertTrue(rates.coversRange(validFrom = validFrom, validTo = validTo))
    }

    @Test
    fun `coversRange should return true for overlapping rates covering the range`() {
        val rates = listOf(
            RateEntity(
                tariffCode = "tariff1",
                rateType = RateType.STANDING_CHARGE,
                paymentMethod = PaymentMethod.DIRECT_DEBIT,
                validFrom = Instant.parse("2024-01-01T00:00:00Z"),
                validTo = Instant.parse("2024-06-30T23:59:59Z"),
                vatRate = 20.0,
            ),
            RateEntity(
                tariffCode = "tariff1",
                rateType = RateType.STANDING_CHARGE,
                paymentMethod = PaymentMethod.DIRECT_DEBIT,
                validFrom = Instant.parse("2024-06-30T00:00:00Z"),
                validTo = Instant.parse("2024-12-31T23:59:59Z"),
                vatRate = 20.0,
            ),
        )
        val validFrom = Instant.parse("2024-01-01T00:00:00Z")
        val validTo = Instant.parse("2024-12-31T23:59:59Z")

        assertTrue(rates.coversRange(validFrom = validFrom, validTo = validTo))
    }

    @Test
    fun `coversRange should return false if rates do not cover the range completely`() {
        val rates = listOf(
            RateEntity(
                tariffCode = "tariff1",
                rateType = RateType.STANDING_CHARGE,
                paymentMethod = PaymentMethod.DIRECT_DEBIT,
                validFrom = Instant.parse("2024-01-01T00:00:00Z"),
                validTo = Instant.parse("2024-06-30T23:59:59Z"),
                vatRate = 20.0,
            ),
            RateEntity(
                tariffCode = "tariff1",
                rateType = RateType.STANDING_CHARGE,
                paymentMethod = PaymentMethod.DIRECT_DEBIT,
                validFrom = Instant.parse("2024-07-02T00:00:00Z"),
                validTo = Instant.parse("2024-12-31T23:59:59Z"),
                vatRate = 20.0,
            ),
        )
        val validFrom = Instant.parse("2024-01-01T00:00:00Z")
        val validTo = Instant.parse("2024-12-31T23:59:59Z")

        assertFalse(rates.coversRange(validFrom = validFrom, validTo = validTo))
    }
}
