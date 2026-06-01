package com.keser.flameguard

import android.app.Application
import com.google.firebase.FirebaseApp
import com.keser.flameguard.di.KoinHelper

class FireGuardApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    try {
      FirebaseApp.initializeApp(this)
    } catch (e: Exception) {
      e.printStackTrace()
    }

    KoinHelper.initKoin()
  }
}
