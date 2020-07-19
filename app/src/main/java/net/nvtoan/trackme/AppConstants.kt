package net.nvtoan.trackme

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION

object AppConstants {

    val permissionList = listOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)
    const val LOCATION_PROVIDER_CHANGED = "android.location.PROVIDERS_CHANGED"
    const val LOCATION_REQUEST = 1000
    const val GPS_REQUEST = 1001
}