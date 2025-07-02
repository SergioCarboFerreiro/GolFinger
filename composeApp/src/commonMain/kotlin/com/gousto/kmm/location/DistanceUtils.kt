package com.gousto.kmm.location

import kotlin.math.*

fun haversine(start: GeoPoint, end: GeoPoint): Double {
    val R = 6371000.0
    val lat1 = start.latitude.toRadians()
    val lat2 = end.latitude.toRadians()
    val dLat = (end.latitude - start.latitude).toRadians()
    val dLon = (end.longitude - start.longitude).toRadians()
    val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
    val c = 2 * asin(sqrt(a))
    return R * c
}

private fun Double.toRadians() = Math.toRadians(this)

