/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.graphql

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.rwmobi.kunigami.data.source.network.dto.auth.Token
import com.rwmobi.kunigami.domain.exceptions.except
import com.rwmobi.kunigami.graphql.ObtainKrakenTokenMutation
import com.rwmobi.kunigami.graphql.type.ObtainJSONWebTokenInput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class GraphQLEndpoint(
    private val apolloClient: ApolloClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend fun getAuthorizationToken(apiKey: String): Result<Token> {
        return withContext(dispatcher) {
            runCatching {
                val input = ObtainJSONWebTokenInput(APIKey = Optional.present(apiKey))
                val response = apolloClient.mutation(ObtainKrakenTokenMutation(input)).execute()

                response.data?.obtainKrakenToken?.let {
                    Token.fromObtainKrakenToken(obtainKrakenToken = it)
                } ?: throw IllegalArgumentException("failed")
            }
        }.except<CancellationException, _>()
    }

    suspend fun refreshAuthorizationToken(refreshToken: String): Result<Token> {
        return withContext(dispatcher) {
            runCatching {
                val input = ObtainJSONWebTokenInput(refreshToken = Optional.present(refreshToken))
                val response = apolloClient.mutation(ObtainKrakenTokenMutation(input)).execute()

                response.data?.obtainKrakenToken?.let {
                    Token.fromObtainKrakenToken(obtainKrakenToken = it)
                } ?: throw IllegalArgumentException("failed")
            }
        }.except<CancellationException, _>()
    }
}
