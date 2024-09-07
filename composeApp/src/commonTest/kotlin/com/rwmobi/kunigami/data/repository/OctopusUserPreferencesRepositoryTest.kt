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

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.rwmobi.kunigami.data.source.local.preferences.FakePreferencesStore
import com.rwmobi.kunigami.data.source.local.preferences.interfaces.PreferencesStore
import com.rwmobi.kunigami.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("TooManyFunctions")
class OctopusUserPreferencesRepositoryTest {

    private lateinit var fakePreferencesStore: PreferencesStore
    private lateinit var repository: UserPreferencesRepository

    @BeforeTest
    fun setupRepository() {
        fakePreferencesStore = FakePreferencesStore()
        repository = OctopusUserPreferencesRepository(preferencesStore = fakePreferencesStore)
    }

    @Test
    fun `isDemoMode should return true when no credentials are stored`() = runBlocking {
        val result = repository.isDemoMode()
        assertTrue(result)
    }

    @Test
    fun `isDemoMode should return false when all credentials are stored`() = runBlocking {
        fakePreferencesStore.saveStringData("API_KEY", "apiKey")
        fakePreferencesStore.saveStringData("ACCOUNT_NUMBER", "accountNumber")
        fakePreferencesStore.saveStringData("MPAN", "mpan")
        fakePreferencesStore.saveStringData("METER_SERIAL_NUMBER", "meterSerialNumber")

        val result = repository.isDemoMode()
        assertTrue(!result)
    }

    @Test
    fun `getApiKey should return stored API key`() = runBlocking {
        fakePreferencesStore.saveStringData("API_KEY", "testApiKey")
        val apiKey = repository.getApiKey()
        assertEquals("testApiKey", apiKey)
    }

    @Test
    fun `getApiKey should return null when no API key is stored`() = runBlocking {
        val apiKey = repository.getApiKey()
        assertNull(apiKey)
    }

    @Test
    fun `getAccountNumber should return stored account number`() = runBlocking {
        fakePreferencesStore.saveStringData("ACCOUNT_NUMBER", "testAccountNumber")
        val accountNumber = repository.getAccountNumber()
        assertEquals("testAccountNumber", accountNumber)
    }

    @Test
    fun `getAccountNumber should return null when no account number is stored`() = runBlocking {
        val accountNumber = repository.getAccountNumber()
        assertNull(accountNumber)
    }

    @Test
    fun `getMpan should return stored MPAN`() = runBlocking {
        fakePreferencesStore.saveStringData("MPAN", "testMpan")
        val mpan = repository.getMpan()
        assertEquals("testMpan", mpan)
    }

    @Test
    fun `getMpan should return null when no MPAN is stored`() = runBlocking {
        val mpan = repository.getMpan()
        assertNull(mpan)
    }

    @Test
    fun `getMeterSerialNumber should return stored meter serial number`() = runBlocking {
        fakePreferencesStore.saveStringData("METER_SERIAL_NUMBER", "testSerialNumber")
        val serialNumber = repository.getMeterSerialNumber()
        assertEquals("testSerialNumber", serialNumber)
    }

    @Test
    fun `getMeterSerialNumber should return null when no meter serial number is stored`() = runBlocking {
        val serialNumber = repository.getMeterSerialNumber()
        assertNull(serialNumber)
    }

    @Test
    fun `setApiKey should store API key`() = runBlocking {
        repository.setApiKey("newApiKey")
        assertEquals("newApiKey", fakePreferencesStore.getStringData("API_KEY"))
    }

    @Test
    fun `setAccountNumber should store account number`() = runBlocking {
        repository.setAccountNumber("newAccountNumber")
        assertEquals("newAccountNumber", fakePreferencesStore.getStringData("ACCOUNT_NUMBER"))
    }

    @Test
    fun `setMpan should store MPAN`() = runBlocking {
        repository.setMpan("newMpan")
        assertEquals("newMpan", fakePreferencesStore.getStringData("MPAN"))
    }

    @Test
    fun `setMeterSerialNumber should store meter serial number`() = runBlocking {
        repository.setMeterSerialNumber("newSerialNumber")
        assertEquals("newSerialNumber", fakePreferencesStore.getStringData("METER_SERIAL_NUMBER"))
    }

    @Test
    fun `getWindowSize should return stored window size`() = runBlocking {
        fakePreferencesStore.saveFloatData("WINDOW_WIDTH", 800f)
        fakePreferencesStore.saveFloatData("WINDOW_HEIGHT", 600f)

        val windowSize = repository.getWindowSize()
        assertEquals(DpSize(800.dp, 600.dp), windowSize)
    }

    @Test
    fun `getWindowSize should return null when window size is not stored`() = runBlocking {
        val windowSize = repository.getWindowSize()
        assertNull(windowSize)
    }

    @Test
    fun `setWindowSize should store window size`() = runBlocking {
        val newSize = DpSize(1024.dp, 768.dp)
        repository.setWindowSize(newSize)

        assertEquals(1024f, fakePreferencesStore.getFloatData("WINDOW_WIDTH"))
        assertEquals(768f, fakePreferencesStore.getFloatData("WINDOW_HEIGHT"))
    }

    @Test
    fun `clearCredentials should remove all stored credentials`() = runBlocking {
        fakePreferencesStore.saveStringData("API_KEY", "apiKey")
        fakePreferencesStore.saveStringData("ACCOUNT_NUMBER", "accountNumber")
        fakePreferencesStore.saveStringData("MPAN", "mpan")
        fakePreferencesStore.saveStringData("METER_SERIAL_NUMBER", "meterSerialNumber")

        repository.clearCredentials()

        assertNull(fakePreferencesStore.getStringData("API_KEY"))
        assertNull(fakePreferencesStore.getStringData("ACCOUNT_NUMBER"))
        assertNull(fakePreferencesStore.getStringData("MPAN"))
        assertNull(fakePreferencesStore.getStringData("METER_SERIAL_NUMBER"))
    }

    @Test
    fun `clearStorage should clear all stored data`() = runBlocking {
        fakePreferencesStore.saveStringData("API_KEY", "apiKey")
        fakePreferencesStore.saveFloatData("WINDOW_WIDTH", 1024f)

        repository.clearStorage()

        assertNull(fakePreferencesStore.getStringData("API_KEY"))
        assertNull(fakePreferencesStore.getFloatData("WINDOW_WIDTH"))
    }
}
