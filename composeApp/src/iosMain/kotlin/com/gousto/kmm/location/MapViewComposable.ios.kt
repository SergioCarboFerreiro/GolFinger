package com.gousto.kmm.location

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.kevinnzou.composewebview.compose.webview.WebView
import io.github.kevinnzou.composewebview.compose.webview.rememberWebViewState

@Composable
actual fun MapViewComposable(start: GeoPoint?, end: GeoPoint?) {
    val url = buildString {
        append("https://maps.googleapis.com/maps/api/staticmap?size=600x300")
        start?.let { append("&markers=${it.latitude},${it.longitude}") }
        end?.let { append("&markers=${it.latitude},${it.longitude}") }
    }
    val state = rememberWebViewState(url)
    WebView(state, modifier = Modifier.fillMaxWidth().height(200.dp))
}
