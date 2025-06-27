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
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock

@OptIn(ExperimentalEncodingApi::class)
class TokenTest {

    private fun createMockTokenPayload(exp: Long): String {
        val header = """{"alg":"RS256","typ":"JWT"}"""
        val payload = """{"exp":$exp}"""
        val signature = "signature"

        return listOf(header, payload, signature)
            .joinToString(".") { it.encodeToByteArray().base64() }
    }

    private fun ByteArray.base64(): String = Base64.encode(this)

    private val refreshToken = "refreshToken123"

    @Test
    fun `fromObtainKrakenToken should create Token object correctly`() {
        val exp = 1656365615L
        val tokenString = createMockTokenPayload(exp)

        val obtainKrakenToken = ObtainKrakenTokenMutation.ObtainKrakenToken(
            token = tokenString,
            refreshToken = refreshToken,
            refreshExpiresIn = 1656368615,
            possibleErrors = emptyList(),
        )

        val token = Token.fromObtainKrakenToken(obtainKrakenToken)

        assertEquals(token.token, tokenString)
        assertEquals(token.expiresIn, exp)
        assertEquals(token.refreshToken, refreshToken)
        assertEquals(token.refreshExpiresIn, 1656368615)
    }

    @Test
    fun `getTokenState should return VALID when token is not expired`() {
        val exp = Clock.System.now().epochSeconds + 3600
        val tokenString = createMockTokenPayload(exp)

        val token = Token(
            token = tokenString,
            expiresIn = exp,
            refreshToken = refreshToken,
            refreshExpiresIn = exp + 3600,
        )

        assertEquals(token.getTokenState(), TokenState.VALID)
    }

    @Test
    fun `getTokenState should return REFRESH when token is expired but refresh token is still valid`() {
        val exp = Clock.System.now().epochSeconds - 3600
        val refreshExp = Clock.System.now().epochSeconds + 3600
        val tokenString = createMockTokenPayload(exp)

        val token = Token(
            token = tokenString,
            expiresIn = exp,
            refreshToken = refreshToken,
            refreshExpiresIn = refreshExp,
        )

        assertEquals(token.getTokenState(), TokenState.REFRESH)
    }

    @Test
    fun `getTokenState should return EXPIRED when both token and refresh token are expired`() {
        val exp = Clock.System.now().epochSeconds - 3600
        val refreshExp = Clock.System.now().epochSeconds - 1800
        val tokenString = createMockTokenPayload(exp)

        val token = Token(
            token = tokenString,
            expiresIn = exp,
            refreshToken = refreshToken,
            refreshExpiresIn = refreshExp,
        )

        assertEquals(token.getTokenState(), TokenState.EXPIRED)
    }
}
