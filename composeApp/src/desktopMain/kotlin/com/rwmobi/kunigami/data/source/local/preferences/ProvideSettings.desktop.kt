/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */

package com.rwmobi.kunigami.data.source.local.preferences

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

fun provideSettings(): Settings {
    val preferences = Preferences.userRoot().node("encrypted_prefs")
    return PreferencesSettings(preferences)
}
