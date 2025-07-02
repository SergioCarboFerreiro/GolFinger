package com.gousto.kmm.location

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun MapViewComposable(start: GeoPoint?, end: GeoPoint?) {
    val cameraPositionState: CameraPositionState = rememberCameraPositionState()
    GoogleMap(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        cameraPositionState = cameraPositionState
    ) {
        start?.let {
            Marker(state = MarkerState(LatLng(it.latitude, it.longitude)))
        }
        end?.let {
            Marker(state = MarkerState(LatLng(it.latitude, it.longitude)))
        }
    }
    LaunchedEffect(start, end) {
        val focus = end ?: start
        focus?.let {
            cameraPositionState.move(com.google.maps.android.compose.CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 17f))
        }
    }
}
