package com.gousto.kmm.location

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual class LocationService : NSObject(), CLLocationManagerDelegateProtocol {
    private val manager = CLLocationManager()
    private var continuation: kotlinx.coroutines.CancellableContinuation<GeoPoint>? = null

    actual suspend fun getLocation(): GeoPoint = suspendCancellableCoroutine { cont ->
        continuation = cont
        manager.delegate = this
        manager.requestWhenInUseAuthorization()
        manager.requestLocation()
    }

    override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus) {
        if (didChangeAuthorizationStatus == kCLAuthorizationStatusDenied || didChangeAuthorizationStatus == kCLAuthorizationStatusRestricted) {
            continuation?.resumeWithException(Exception("Location permission denied"))
            continuation = null
        }
    }

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        val loc = (didUpdateLocations.lastOrNull() as? CLLocation)
        val point = GeoPoint(loc?.coordinate?.latitude ?: 0.0, loc?.coordinate?.longitude ?: 0.0)
        continuation?.resume(point)
        continuation = null
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        continuation?.resumeWithException(Exception(didFailWithError.localizedDescription))
        continuation = null
    }
}
