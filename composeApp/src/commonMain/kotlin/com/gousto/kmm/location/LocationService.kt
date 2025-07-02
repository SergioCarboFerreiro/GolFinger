package com.gousto.kmm.location

expect class LocationService {
    suspend fun getLocation(): GeoPoint
}
