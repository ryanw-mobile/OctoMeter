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

import com.rwmobi.kunigami.data.source.local.cache.InMemoryCacheDataSource
import com.rwmobi.kunigami.data.source.network.dto.auth.Token
import com.rwmobi.kunigami.data.source.network.graphql.FakeGraphQLEndpoint
import com.rwmobi.kunigami.data.source.network.graphql.interfaces.GraphQLEndpoint
import com.rwmobi.kunigami.domain.repository.FakeUserPreferencesRepository
import com.rwmobi.kunigami.domain.repository.TokenRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Clock

@Suppress("TooManyFunctions")
class GraphQLTokenRepositoryTest {
    private lateinit var tokenRepository: TokenRepository
    private lateinit var graphQLEndpoint: GraphQLEndpoint
    private lateinit var inMemoryCacheDataSource: InMemoryCacheDataSource
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    @BeforeTest
    fun setupRepository() {
        inMemoryCacheDataSource = InMemoryCacheDataSource()
        userPreferencesRepository = FakeUserPreferencesRepository()
        graphQLEndpoint = FakeGraphQLEndpoint()

        tokenRepository = GraphQLTokenRepository(
            inMemoryCacheDataSource = inMemoryCacheDataSource,
            graphQLEndpointProvider = { graphQLEndpoint },
            userPreferencesRepository = userPreferencesRepository,
        )
    }

    @Test
    fun `getToken should return null if apiKey is not present`() = runBlocking {
        val token = tokenRepository.getToken(forceRefresh = false)
        assertNull(token)
    }

    @Test
    fun `getToken should return cached token if valid and forceRefresh is false`() = runBlocking {
        userPreferencesRepository.setApiKey("testApiKey")
        val cachedToken = Token(
            token = "cachedToken",
            expiresIn = Clock.System.now().epochSeconds + 3600,
            refreshToken = "refreshToken",
            refreshExpiresIn = 7200,
        )
        inMemoryCacheDataSource.cacheToken("testApiKey", cachedToken)

        val token = tokenRepository.getToken(forceRefresh = false)
        assertEquals(cachedToken, token)
    }

    @Test
    fun `getToken should request a new token if forceRefresh is true`() = runBlocking {
        userPreferencesRepository.setApiKey("testApiKey")

        val token = tokenRepository.getToken(forceRefresh = true)
        assertEquals("newToken", token?.token)
    }

    @Test
    fun `getToken should request a new token if cached token is null`() = runBlocking {
        userPreferencesRepository.setApiKey("testApiKey")

        val token = tokenRepository.getToken(forceRefresh = false)
        assertEquals("newToken", token?.token)
    }

    @Test
    fun `getToken should refresh token if cached token is in REFRESH state and has refreshToken`() = runBlocking {
        userPreferencesRepository.setApiKey("testApiKey")
        val expiredToken = Token(
            token = "expiredToken",
            expiresIn = Clock.System.now().epochSeconds - 3600,
            refreshToken = "refreshToken",
            refreshExpiresIn = Clock.System.now().epochSeconds + 3600,
        )
        inMemoryCacheDataSource.cacheToken("testApiKey", expiredToken)

        val token = tokenRepository.getToken(forceRefresh = false)
        assertEquals("refreshedToken", token?.token)
    }

    @Test
    fun `getToken should return new token if cached token is in EXPIRED state`() = runBlocking {
        userPreferencesRepository.setApiKey("testApiKey")
        val expiredToken = Token(
            token = "expiredToken",
            expiresIn = Clock.System.now().epochSeconds - 7200,
            refreshToken = null,
            refreshExpiresIn = Clock.System.now().epochSeconds - 3600,
        )
        inMemoryCacheDataSource.cacheToken("testApiKey", expiredToken)

        val token = tokenRepository.getToken(forceRefresh = false)
        assertEquals("newToken", token?.token)
    }
}
