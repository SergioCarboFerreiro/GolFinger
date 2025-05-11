package com.gousto.kmm

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.gousto.kmm.navigation.AppRoot
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        AppRoot()
    }
}