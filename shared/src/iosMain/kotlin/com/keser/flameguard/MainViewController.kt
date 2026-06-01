package com.keser.flameguard

import androidx.compose.ui.window.ComposeUIViewController
import com.keser.flameguard.di.KoinHelper

fun MainViewController() = ComposeUIViewController { App() }

fun initialize() {
    KoinHelper.initKoin()
}

