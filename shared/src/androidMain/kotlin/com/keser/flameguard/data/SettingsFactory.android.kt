package com.keser.flameguard.data

import android.content.Context
import com.keser.flameguard.AndroidContextHolder
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

actual fun createSettings(): Settings {
    val preferences = AndroidContextHolder.appContext.getSharedPreferences(
        "flameguard_settings",
        Context.MODE_PRIVATE,
    )
    return SharedPreferencesSettings(preferences)
}
