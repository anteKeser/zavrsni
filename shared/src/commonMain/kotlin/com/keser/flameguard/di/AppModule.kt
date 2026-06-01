package com.keser.flameguard.di

import com.keser.flameguard.data.AuthRepository
import com.keser.flameguard.data.FirebaseSensorRepositoryImpl
import com.keser.flameguard.data.SensorRepository
import com.keser.flameguard.ui.account.AccountViewModel
import com.keser.flameguard.ui.dashboard.DashboardViewModel
import com.keser.flameguard.ui.home.HomeViewModel
import com.keser.flameguard.ui.login.LoginViewModel
import com.keser.flameguard.ui.login.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  single { AuthRepository() }
  viewModel { LoginViewModel(get()) }
  viewModel { HomeViewModel(get(), get()) }
  viewModel { DashboardViewModel(get()) }
  viewModel { AccountViewModel(get()) }
  viewModel { RegisterViewModel(get()) }
}

val repositoryModule = module { single<SensorRepository> { FirebaseSensorRepositoryImpl() } }

val allModules = listOf(viewModelModule, repositoryModule)
