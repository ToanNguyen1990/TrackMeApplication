package net.nvtoan.trackme.app.utils

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import timber.log.Timber
import java.lang.Exception

object MapUtil {

    fun zoomRoute(map: GoogleMap, lstLatLngRoute: List<LatLng>) {
        if (lstLatLngRoute.isEmpty()) {
            return
        }
        val boundsBuilder = LatLngBounds.Builder()
        for (latLngPoint in lstLatLngRoute) {
            boundsBuilder.include(latLngPoint)
        }
        val latLngBounds = boundsBuilder.build()
        try {
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 7))
        } catch (e: Exception) {
            Timber.i("moveCamera: $e")
        }

    }
}