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
import com.rwmobi.kunigami.data.source.network.dto.auth.TokenState
import com.rwmobi.kunigami.domain.extensions.toSystemDefaultLocalDate
import com.rwmobi.kunigami.domain.model.account.Account
import com.rwmobi.kunigami.domain.model.product.ProductSummary
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class InMemoryCacheDataSource {
    private var cachedProfile: Pair<Account, Instant>? = null
    private var cachedToken: Pair<String, Token>? = null
    private var cachedProductSummary: Pair<String, List<ProductSummary>>? = null

    fun cacheProfile(account: Account, createdAt: Instant) {
        cachedProfile = Pair(account, createdAt)
    }

    fun cacheToken(apiKey: String, token: Token) {
        cachedToken = Pair(apiKey, token)
    }

    fun cacheProductSummary(postcode: String, productSummaries: List<ProductSummary>) {
        cachedProductSummary = Pair(postcode, productSummaries)
    }

    /***
     * Return cached profile, if there is an instance kept on the same day
     */
    fun getProfile(accountNumber: String): Account? {
        return cachedProfile?.first?.let { account ->
            if (account.accountNumber == accountNumber &&
                Clock.System.now().toSystemDefaultLocalDate() == cachedProfile?.second?.toSystemDefaultLocalDate()
            ) {
                account
            } else {
                cachedProfile = null // clear the cache
                null
            }
        }
    }

    fun getToken(apiKey: String): Token? {
        with(cachedToken) {
            return if (this == null ||
                first != apiKey ||
                second.getTokenState() == TokenState.EXPIRED
            ) {
                null
            } else {
                this.second
            }
        }
    }

    fun getProductSummary(postcode: String): List<ProductSummary>? {
        return cachedProductSummary?.let {
            if (it.first == postcode) {
                it.second
            } else {
                // invalidate the cache to prevent the cached result staying for too long
                cachedProductSummary = null
                null
            }
        }
    }

    fun clear() {
        cachedProfile = null
        cachedToken = null
    }
}
