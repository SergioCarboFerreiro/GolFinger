package com.gousto.kmm.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class LocationService(private val context: Context) {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    actual suspend fun getLocation(): GeoPoint = suspendCancellableCoroutine { cont ->
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            cont.resumeWithException(SecurityException("Location permission not granted"))
            return@suspendCancellableCoroutine
        }

        val token = CancellationTokenSource()
        client.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, token.token)
            .addOnSuccessListener { location ->
                if (location != null) {
                    cont.resume(GeoPoint(location.latitude, location.longitude))
                } else {
                    cont.resume(GeoPoint(0.0, 0.0))
                }
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }
}
