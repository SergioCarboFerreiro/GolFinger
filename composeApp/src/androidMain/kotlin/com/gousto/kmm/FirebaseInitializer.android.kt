package com.gousto.kmm

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize

actual fun initFirebase() {
    Firebase.initialize(GolfFingerApp.instance)
}