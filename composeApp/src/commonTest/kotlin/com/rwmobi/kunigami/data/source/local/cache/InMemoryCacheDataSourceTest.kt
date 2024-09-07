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

package com.rwmobi.kunigami.data.source.local.cache

import com.rwmobi.kunigami.data.source.network.dto.auth.Token
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

    @Test
    fun `cacheToken should cache the token`() {
        val apiKey = "test-api-key"
        val token = Token(
            token = "test-token",
            expiresIn = Clock.System.now().epochSeconds + 3600,
            refreshToken = "test-refresh-token",
            refreshExpiresIn = Clock.System.now().epochSeconds + 7200,
        )
        cacheDataSource.cacheToken(apiKey, token)

        val cachedToken = cacheDataSource.getToken(apiKey)
        assertEquals(expected = token, actual = cachedToken)
    }

    @Test
    fun `getToken should return null if apiKey does not match`() {
        val apiKey = "test-api-key"
        val token = Token(
            token = "test-token",
            expiresIn = Clock.System.now().epochSeconds + 3600,
            refreshToken = "test-refresh-token",
            refreshExpiresIn = Clock.System.now().epochSeconds + 7200,
        )
        cacheDataSource.cacheToken(apiKey, token)

        val cachedToken = cacheDataSource.getToken("another-api-key")
        assertNull(cachedToken)
    }

    @Test
    fun `getToken should return null if token is expired`() {
        val apiKey = "test-api-key"
        val token = Token(
            token = "test-token",
            expiresIn = Clock.System.now().epochSeconds - 3600,
            refreshToken = "test-refresh-token",
            refreshExpiresIn = Clock.System.now().epochSeconds - 1800,
        )
        cacheDataSource.cacheToken(apiKey, token)

        val cachedToken = cacheDataSource.getToken(apiKey)
        assertNull(cachedToken)
    }

    @Test
    fun `getToken should return token if it is valid`() {
        val apiKey = "test-api-key"
        val token = Token(
            token = "test-token",
            expiresIn = Clock.System.now().epochSeconds + 3600,
            refreshToken = "test-refresh-token",
            refreshExpiresIn = Clock.System.now().epochSeconds + 7200,
        )
        cacheDataSource.cacheToken(apiKey, token)

        val cachedToken = cacheDataSource.getToken(apiKey)
        assertEquals(expected = token, actual = cachedToken)
    }

    @Test
    fun `getToken should return token if it is in refresh state`() {
        val apiKey = "test-api-key"
        val token = Token(
            token = "test-token",
            expiresIn = Clock.System.now().epochSeconds - 3600,
            refreshToken = "test-refresh-token",
            refreshExpiresIn = Clock.System.now().epochSeconds + 3600,
        )
        cacheDataSource.cacheToken(apiKey, token)

        val cachedToken = cacheDataSource.getToken(apiKey)
        assertEquals(expected = token, actual = cachedToken)
    }
}
