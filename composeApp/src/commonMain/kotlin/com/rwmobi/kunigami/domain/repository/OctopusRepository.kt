/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.domain.repository

import com.rwmobi.kunigami.domain.model.Account
import com.rwmobi.kunigami.domain.model.Consumption
import com.rwmobi.kunigami.domain.model.Product
import com.rwmobi.kunigami.domain.model.Rate

interface OctopusRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getStandardUnitRates(productCode: String, tariffCode: String): Result<List<Rate>>
    suspend fun getStandingCharges(productCode: String, tariffCode: String): Result<List<Rate>>
    suspend fun getDayUnitRates(productCode: String, tariffCode: String): Result<List<Rate>>
    suspend fun getNightUnitRates(productCode: String, tariffCode: String): Result<List<Rate>>
    suspend fun getConsumption(apiKey: String, mpan: String, meterSerialNumber: String): Result<List<Consumption>>
    suspend fun getAccount(apiKey: String, accountNumber: String): Result<List<Account>>
}
