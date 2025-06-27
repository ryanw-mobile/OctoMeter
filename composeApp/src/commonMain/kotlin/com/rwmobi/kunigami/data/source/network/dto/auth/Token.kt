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

package com.rwmobi.kunigami.data.source.network.dto.auth

import com.rwmobi.kunigami.graphql.ObtainKrakenTokenMutation
import io.ktor.util.decodeBase64Bytes
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Clock

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
            val bytes = payload.decodeBase64Bytes()
            val decodedPayload = jsonFormat.decodeFromString<TokenPayload>(bytes.decodeToString(0, 0 + bytes.size))

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
