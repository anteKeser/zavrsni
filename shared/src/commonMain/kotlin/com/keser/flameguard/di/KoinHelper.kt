package com.keser.flameguard.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

object KoinHelper {
  fun initKoin(appDeclaration: KoinApplication.() -> Unit = {}) {
    startKoin {
      appDeclaration()
      modules(allModules)
    }
  }
}