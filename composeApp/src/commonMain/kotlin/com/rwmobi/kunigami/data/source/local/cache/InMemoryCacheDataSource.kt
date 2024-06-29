/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.cache

import com.rwmobi.kunigami.domain.extensions.toSystemDefaultLocalDate
import com.rwmobi.kunigami.domain.model.account.Account
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class InMemoryCacheDataSource {
    private var cachedProfile: Pair<Account, Instant>? = null

    fun cacheProfile(account: Account, createdAt: Instant) {
        cachedProfile = Pair(account, createdAt)
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

    fun clear() {
        cachedProfile = null
    }
}
