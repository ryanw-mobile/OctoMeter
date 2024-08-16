/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.repository

import co.touchlab.kermit.Logger
import com.rwmobi.kunigami.data.source.local.cache.InMemoryCacheDataSource
import com.rwmobi.kunigami.data.source.network.dto.auth.Token
import com.rwmobi.kunigami.data.source.network.dto.auth.TokenState
import com.rwmobi.kunigami.data.source.network.graphql.interfaces.GraphQLEndpoint
import com.rwmobi.kunigami.domain.repository.TokenRepository
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository

class GraphQLTokenRepository(
    private val inMemoryCacheDataSource: InMemoryCacheDataSource,
    private val graphQLEndpointProvider: () -> GraphQLEndpoint,
    private val userPreferencesRepository: UserPreferencesRepository,
) : TokenRepository {
    // Workaround for cyclic dependency as GraphQLEndpoint -> apolloClient -> GraphQLTokenRepository
    private val graphQLEndpoint: GraphQLEndpoint by lazy { graphQLEndpointProvider() }

    /**
     * Get the cached token associated to the apiKey, or get a new one.
     * If forceRefresh is true and still getting a null token, caller should fail the request.
     */
    override suspend fun getToken(forceRefresh: Boolean): Token? {
        val apiKey = userPreferencesRepository.getApiKey() ?: return null
        val cachedToken = inMemoryCacheDataSource.getToken(apiKey = apiKey)

        return when {
            forceRefresh ||
                cachedToken == null ||
                (cachedToken.getTokenState() == TokenState.REFRESH && cachedToken.refreshToken == null)
            -> {
                // We need a new token
                Logger.v(tag = "getToken", messageString = "requesting a new token")
                return graphQLEndpoint.getAuthorizationToken(apiKey = apiKey).getOrNull()?.also {
                    inMemoryCacheDataSource.cacheToken(apiKey = apiKey, token = it)
                }
            }

            cachedToken.getTokenState() == TokenState.REFRESH && cachedToken.refreshToken != null -> {
                Logger.v(tag = "getToken", messageString = "refreshing the token")
                return graphQLEndpoint.refreshAuthorizationToken(refreshToken = cachedToken.refreshToken).getOrNull()?.also {
                    inMemoryCacheDataSource.cacheToken(apiKey = apiKey, token = it)
                }
            }

            else -> {
                // Should be a valid token. If it doesn't work, caller should generate a new token
                Logger.v(tag = "getToken", messageString = "returning the cached token")
                cachedToken
            }
        }
    }
}
