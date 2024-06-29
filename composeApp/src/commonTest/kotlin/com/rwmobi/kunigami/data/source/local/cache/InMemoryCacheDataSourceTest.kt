/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.cache

import com.rwmobi.kunigami.ui.previewsampledata.AccountSamples
import kotlinx.datetime.Clock
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration

class InMemoryCacheDataSourceTest {

    private lateinit var cacheDataSource: InMemoryCacheDataSource

    @BeforeTest
    fun setup() {
        cacheDataSource = InMemoryCacheDataSource()
    }

    @AfterTest
    fun teardown() {
        cacheDataSource.clear()
    }

    @Test
    fun `cacheProfile should cache the profile`() {
        val account = AccountSamples.account928
        cacheDataSource.cacheProfile(account, Clock.System.now())

        val cachedAccount = cacheDataSource.getProfile(accountNumber = AccountSamples.account928.accountNumber)
        assertEquals(expected = account, actual = cachedAccount)
    }

    @Test
    fun `getProfile should return null if account number does not match`() {
        val account = AccountSamples.account928
        cacheDataSource.cacheProfile(account, Clock.System.now())

        val cachedAccount = cacheDataSource.getProfile("another-account-number")
        assertNull(cachedAccount)
    }

    @Test
    fun `getProfile should return null if cached date is not the same day`() {
        val account = AccountSamples.account928
        val yesterday = Clock.System.now() - Duration.parse("2d")
        cacheDataSource.cacheProfile(account, yesterday)

        val cachedAccount = cacheDataSource.getProfile(accountNumber = AccountSamples.account928.accountNumber)
        assertNull(cachedAccount)
    }

    @Test
    fun `clear should clear the cache`() {
        val account = AccountSamples.account928
        cacheDataSource.cacheProfile(account, Clock.System.now())

        cacheDataSource.clear()
        val cachedAccount = cacheDataSource.getProfile(accountNumber = AccountSamples.account928.accountNumber)
        assertNull(cachedAccount)
    }
}
