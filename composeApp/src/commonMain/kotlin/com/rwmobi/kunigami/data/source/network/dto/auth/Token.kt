/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.network.dto.auth

import com.rwmobi.kunigami.graphql.ObtainKrakenTokenMutation
import io.ktor.util.decodeBase64Bytes
import io.ktor.utils.io.core.String
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

data class Token(
    val token: String,
    val expiresIn: Long,
    val refreshToken: String?,
    val refreshExpiresIn: Long?,
) {
    companion object {
        private val jsonFormat = Json { ignoreUnknownKeys = true }

        fun fromObtainKrakenToken(obtainKrakenToken: ObtainKrakenTokenMutation.ObtainKrakenToken): Token {
            val payload = obtainKrakenToken.token.split(".")[1]
            val decodedPayload = jsonFormat.decodeFromString<TokenPayload>(String(payload.decodeBase64Bytes()))

            return Token(
                token = obtainKrakenToken.token,
                expiresIn = decodedPayload.exp,
                refreshToken = obtainKrakenToken.refreshToken,
                refreshExpiresIn = obtainKrakenToken.refreshExpiresIn?.toLong(),
            )
        }
    }

    fun getTokenState(): TokenState {
        val currentTime = Clock.System.now().epochSeconds

        return when {
            currentTime <= expiresIn -> {
                TokenState.VALID
            }

            refreshExpiresIn != null && currentTime <= refreshExpiresIn -> {
                TokenState.REFRESH
            }

            else -> {
                TokenState.EXPIRED
            }
        }
    }
}

enum class TokenState {
    VALID,
    REFRESH,
    EXPIRED,
}

@Serializable
private data class TokenPayload(val exp: Long)
