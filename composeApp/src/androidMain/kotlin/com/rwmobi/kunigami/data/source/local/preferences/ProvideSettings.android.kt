/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import javax.crypto.AEADBadTagException

fun provideSettings(context: Context): Settings {
    val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    val sharedPreferencesName = "encrypted_prefs"

    try {
        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            sharedPreferencesName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
        return SharedPreferencesSettings(sharedPreferences)
    } catch (ex: AEADBadTagException) {
        // Delete the corrupted encrypted shared preferences
        context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE).edit().clear().apply()

        // Retry creating EncryptedSharedPreferences
        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            sharedPreferencesName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
        return SharedPreferencesSettings(sharedPreferences)
    }
}
