/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.extensions

import io.kotest.matchers.longs.shouldBeExactly
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test

class InstantExtensionsTest {

    private val timeZone = TimeZone.currentSystemDefault()

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
