package com.gousto.kmm.location

import androidx.compose.runtime.Composable

@Composable
expect fun MapViewComposable(start: GeoPoint?, end: GeoPoint?)
