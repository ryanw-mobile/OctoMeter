/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.extensions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TooManyFunctions")
class InstantExtensionsKtTest {

    private val timeZone = TimeZone.currentSystemDefault()

    @Test
    fun `toIso8601WithoutSeconds should format correctly`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 30, second = 15).toInstant(TimeZone.UTC)
        val expected = "2023-05-01T10:30Z"
        assertEquals(expected, instant.toIso8601WithoutSeconds())
    }

    @Test
    fun `atStartOfHour should round down correctly`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 45, second = 15).toInstant(timeZone)
        val expected = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 0, second = 0).toInstant(timeZone)
        assertEquals(expected, instant.atStartOfHour())
    }

    @Test
    fun `atStartOfDay should round down to start of day`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 45, second = 15).toInstant(timeZone)
        val expected = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 0, minute = 0, second = 0).toInstant(timeZone)
        assertEquals(expected, instant.atStartOfDay())
    }

    @Test
    fun `atEndOfDay should round up to end of day`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 45, second = 15).toInstant(timeZone)
        val expected = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 23, minute = 59, second = 59, nanosecond = 999_999_999).toInstant(timeZone)
        assertEquals(expected, instant.atEndOfDay())
    }

    @Test
    fun `getLocalHHMMString should format correctly`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 45, second = 15).toInstant(timeZone)
        val expected = "10:45"
        assertEquals(expected, instant.getLocalHHMMString())
    }

    @Test
    fun `getLocalHourString should format correctly`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 45, second = 15).toInstant(timeZone)
        val expected = "10"
        assertEquals(expected, instant.getLocalHourString())
    }

    // This is not a very good test because it depends on platform AND local settings
    @Test
    fun `toLocalDateTimeString should format correctly`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 45, second = 15).toInstant(timeZone)
        val localDateString = instant.getLocalDateString()
        val expected = "$localDateString 10:45"
        assertEquals(expected, instant.toLocalDateTimeString())
    }

    @Test
    fun `getLocalYear should return correct year`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 45, second = 15).toInstant(timeZone)
        val expected = 2023
        assertEquals(expected, instant.getLocalYear())
    }

    @Test
    fun `getLocalDayOfMonth should return correct day`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 45, second = 15).toInstant(timeZone)
        val expected = 1
        assertEquals(expected, instant.getLocalDayOfMonth())
    }

    @Test
    fun `getLocalEnglishAbbreviatedDayOfWeekName should return correct weekday`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 45, second = 15).toInstant(timeZone)
        val expected = "Mon"
        assertEquals(expected, instant.getLocalEnglishAbbreviatedDayOfWeekName())
    }

    @Test
    fun `getLocalDayOfWeekAndDayString should return correct weekday and day`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 45, second = 15).toInstant(timeZone)
        val expected = "Mon 01"
        assertEquals(expected, instant.getLocalDayOfWeekAndDayString())
    }

    @Test
    fun `getLocalDayMonthString should return correct day and month`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 45, second = 15).toInstant(timeZone)
        val expected = "01 May"
        assertEquals(expected, instant.getLocalDayMonthString())
    }

    @Test
    fun `getLocalMonthString should return correct month`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 45, second = 15).toInstant(timeZone)
        val expected = "May"
        assertEquals(expected, instant.getLocalMonthString())
    }

    @Test
    fun `getLocalMonthYearString should return correct month and year`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 45, second = 15).toInstant(timeZone)
        val expected = "May 2023"
        assertEquals(expected, instant.getLocalMonthYearString())
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis when current time is exactly on the hour`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 0, second = 0, nanosecond = 0).toInstant(timeZone)
        val expected = 30 * 60 * 1000L // 30 minutes in milliseconds
        assertEquals(expected, instant.getNextHalfHourCountdownMillis())
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis when current time is exactly on the half hour`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 30, second = 0, nanosecond = 0).toInstant(timeZone)
        val expected = 30 * 60 * 1000L // 30 minutes in milliseconds
        assertEquals(expected, instant.getNextHalfHourCountdownMillis())
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis when current time is just past the hour`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 1, second = 0, nanosecond = 0).toInstant(timeZone)
        val expected = 29 * 60 * 1000L // 29 minutes in milliseconds
        assertEquals(expected, instant.getNextHalfHourCountdownMillis())
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis when current time is just past the half hour`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 31, second = 0, nanosecond = 0).toInstant(timeZone)
        val expected = 29 * 60 * 1000L // 29 minutes in milliseconds
        assertEquals(expected, instant.getNextHalfHourCountdownMillis())
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis when current time is just before the half hour`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 29, second = 30, nanosecond = 0).toInstant(timeZone)
        val expected = 30 * 1000L // 30 seconds in milliseconds
        assertEquals(expected, instant.getNextHalfHourCountdownMillis())
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis when current time is just before the hour`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 59, second = 30, nanosecond = 0).toInstant(timeZone)
        val expected = 30 * 1000L // 30 seconds in milliseconds
        assertEquals(expected, instant.getNextHalfHourCountdownMillis())
    }

    @Test
    fun `getNextHalfHourCountdownMillis should return correct millis including nanoseconds`() {
        val instant = LocalDateTime(year = 2023, monthNumber = 5, dayOfMonth = 1, hour = 10, minute = 29, second = 59, nanosecond = 999_000_000).toInstant(timeZone)
        val expected = 1L // 1 millisecond
        assertEquals(expected, instant.getNextHalfHourCountdownMillis())
    }

    /***
     * British Summer Time related test cases
     */
    private val londonZone = TimeZone.of("Europe/London")

    @Test
    fun `atStartOfDay should return 00 00 local time on a typical day`() {
        val date = LocalDate(year = 2024, monthNumber = 6, dayOfMonth = 15) // A typical day, not a transition day
        val expectedStartOfDay = LocalDateTime(year = date.year, monthNumber = date.monthNumber, dayOfMonth = date.dayOfMonth, hour = 0, minute = 0, second = 0)
            .toInstant(londonZone)
        val actualStartOfDay = expectedStartOfDay.atStartOfDay()

        assertEquals(expectedStartOfDay, actualStartOfDay)
    }

    @Test
    fun `atEndOfDay should return 23 59 59 local time on a typical day`() {
        val date = LocalDate(year = 2024, monthNumber = 6, dayOfMonth = 15) // A typical day, not a transition day
        val expectedEndOfDay = LocalDateTime(year = date.year, monthNumber = date.monthNumber, dayOfMonth = date.dayOfMonth, hour = 23, minute = 59, second = 59, nanosecond = 999_999_999)
            .toInstant(londonZone)
        val actualEndOfDay = expectedEndOfDay.atEndOfDay()

        assertEquals(expectedEndOfDay, actualEndOfDay)
    }

    @Test
    fun `atStartOfDay should correctly handle the transition from GMT to BST`() {
        // Transition from GMT to BST on March 31, 2024
        val date = LocalDate(year = 2024, monthNumber = 3, dayOfMonth = 31)
        val expectedStartOfDay = LocalDateTime(year = date.year, monthNumber = date.monthNumber, dayOfMonth = date.dayOfMonth, hour = 0, minute = 0, second = 0)
            .toInstant(londonZone)
        val actualStartOfDay = expectedStartOfDay.atStartOfDay()

        assertEquals(expectedStartOfDay, actualStartOfDay)
    }

    @Test
    fun `atEndOfDay should correctly handle the transition from GMT to BST`() {
        // Transition from GMT to BST on March 31, 2024, skipping from 01:00 to 02:00
        val date = LocalDate(year = 2024, monthNumber = 3, dayOfMonth = 31)
        val expectedEndOfDay = LocalDateTime(year = date.year, monthNumber = date.monthNumber, dayOfMonth = date.dayOfMonth, hour = 23, minute = 59, second = 59, nanosecond = 999_999_999)
            .toInstant(londonZone)
        val actualEndOfDay = expectedEndOfDay.atEndOfDay()

        assertEquals(expectedEndOfDay, actualEndOfDay)
    }
}
