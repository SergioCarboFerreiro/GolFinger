package com.gousto.kmm

import androidx.compose.ui.window.ComposeUIViewController
import com.gousto.kmm.di.initKoin

fun MainViewController() = ComposeUIViewController(configure = { initKoin() }) { App() }