/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.extensions

import io.kotest.matchers.longs.shouldBeExactly
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test

class InstantExtensionsKtTest {

    private val timeZone = TimeZone.currentSystemDefault()

    @Test
    fun `formatInstantWithoutSeconds should format correctly`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 30, 15).toInstant(TimeZone.UTC)
        instant.formatInstantWithoutSeconds() shouldBe "2023-05-01T10:30Z"
    }

    @Test
    fun `roundDownToHour should round down correctly`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 45, 15).toInstant(timeZone)
        val expected = LocalDateTime(2023, 5, 1, 10, 0, 0).toInstant(timeZone)
        instant.roundDownToHour() shouldBe expected
    }

    @Test
    fun `roundDownToDay should round down to start of day`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 45, 15).toInstant(timeZone)
        val expected = LocalDateTime(2023, 5, 1, 0, 0, 0).toInstant(timeZone)
        instant.roundDownToDay() shouldBe expected
    }

    @Test
    fun `roundUpToDayEnd should round up to end of day`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 45, 15).toInstant(timeZone)
        val expected = LocalDateTime(2023, 5, 1, 23, 59, 59, 999_999_999).toInstant(timeZone)
        instant.roundUpToDayEnd() shouldBe expected
    }

    @Test
    fun `toLocalHourMinuteString should format correctly`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 45, 15).toInstant(timeZone)
        instant.toLocalHourMinuteString() shouldBe "10:45"
    }

    @Test
    fun `toLocalHourString should format correctly`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 45, 15).toInstant(timeZone)
        instant.toLocalHourString() shouldBe "10"
    }

    @Test
    fun `toLocalDateTimeString should format correctly`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 45, 15).toInstant(timeZone)
        instant.toLocalDateTimeString() shouldBe "1 May 2023 10:45"
    }

    @Test
    fun `toLocalYear should return correct year`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 45, 15).toInstant(timeZone)
        instant.toLocalYear() shouldBe "2023"
    }

    @Test
    fun `toLocalDay should return correct day`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 45, 15).toInstant(timeZone)
        instant.toLocalDay() shouldBe "1"
    }

    @Test
    fun `toLocalWeekday should return correct weekday`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 45, 15).toInstant(timeZone)
        instant.toLocalWeekday() shouldBe "Mon"
    }

    @Test
    fun `toLocalWeekdayDay should return correct weekday and day`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 45, 15).toInstant(timeZone)
        instant.toLocalWeekdayDay() shouldBe "Mon 01"
    }

    @Test
    fun `toLocalDayMonth should return correct day and month`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 45, 15).toInstant(timeZone)
        instant.toLocalDayMonth() shouldBe "01 May"
    }

    @Test
    fun `toLocalMonth should return correct month`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 45, 15).toInstant(timeZone)
        instant.toLocalMonth() shouldBe "May"
    }

    @Test
    fun `toLocalMonthYear should return correct month and year`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 45, 15).toInstant(timeZone)
        instant.toLocalMonthYear() shouldBe "May 2023"
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis when current time is exactly on the hour`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 0, 0, 0).toInstant(timeZone)
        val millis = instant.getNextHalfHourCountdownMillis()
        val expectedMillis = 30 * 60 * 1000L // 30 minutes in milliseconds
        millis shouldBeExactly expectedMillis
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis when current time is exactly on the half hour`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 30, 0, 0).toInstant(timeZone)
        val millis = instant.getNextHalfHourCountdownMillis()
        val expectedMillis = 30 * 60 * 1000L // 30 minutes in milliseconds
        millis shouldBeExactly expectedMillis
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis when current time is just past the hour`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 1, 0, 0).toInstant(timeZone)
        val millis = instant.getNextHalfHourCountdownMillis()
        val expectedMillis = 29 * 60 * 1000L // 29 minutes in milliseconds
        millis shouldBeExactly expectedMillis
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis when current time is just past the half hour`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 31, 0, 0).toInstant(timeZone)
        val millis = instant.getNextHalfHourCountdownMillis()
        val expectedMillis = 29 * 60 * 1000L // 29 minutes in milliseconds
        millis shouldBeExactly expectedMillis
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis when current time is just before the half hour`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 29, 30, 0).toInstant(timeZone)
        val millis = instant.getNextHalfHourCountdownMillis()
        val expectedMillis = 30 * 1000L // 30 seconds in milliseconds
        millis shouldBeExactly expectedMillis
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis when current time is just before the hour`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 59, 30, 0).toInstant(timeZone)
        val millis = instant.getNextHalfHourCountdownMillis()
        val expectedMillis = 30 * 1000L // 30 seconds in milliseconds
        millis shouldBeExactly expectedMillis
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis including nanoseconds`() {
        val instant = LocalDateTime(2023, 5, 1, 10, 29, 59, 999_000_000).toInstant(timeZone)
        val millis = instant.getNextHalfHourCountdownMillis()
        val expectedMillis = 1L // 1 millisecond
        millis shouldBeExactly expectedMillis
    }
}
