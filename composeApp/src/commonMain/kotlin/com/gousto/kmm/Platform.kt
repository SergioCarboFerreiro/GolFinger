package com.gousto.kmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform