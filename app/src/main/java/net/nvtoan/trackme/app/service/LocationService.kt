package net.nvtoan.trackme.app.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.google.android.gms.location.*
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.lang.Exception

class LocationService: Service() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
//        startForeground()


        try {
            val locationRequest = LocationRequest()
            var locationCallback: LocationCallback? = null

            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = LOCATION_REQUEST_INTERVAL
            locationRequest.fastestInterval = LOCATION_FAST_INTERVAL

            val locationSettings = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
            val settingsClient = LocationServices.getSettingsClient(this@LocationService)

            settingsClient.checkLocationSettings(locationSettings)

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    super.onLocationResult(p0)
                    LocationServices.getFusedLocationProviderClient(this@LocationService)
                        .removeLocationUpdates(locationCallback)
                    locationCallback = null

                    val userLocation = p0?.lastLocation
                    Timber.i("userLocation: ${userLocation?.latitude}, ${userLocation?.longitude}")
                }

                override fun onLocationAvailability(p0: LocationAvailability?) {
                    super.onLocationAvailability(p0)

                    if (p0?.isLocationAvailable != true) {
                        try {
                            LocationServices.getFusedLocationProviderClient(this@LocationService)
                                .removeLocationUpdates(locationCallback)
                            locationCallback = null
                            Timber.i("userLocation: null")

                        } catch (nullPointerException: Exception) {
                            Timber.e(nullPointerException)
                        }
                    }
                }
            }

            LocationServices.getFusedLocationProviderClient(this@LocationService)
                .requestLocationUpdates(locationRequest, locationCallback, null)
        } catch (securityException: SecurityException) {
            Timber.e("May be user turned off location...")

        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Timber.e("App is finished, location still running?")
    }

    override fun onBind(p0: Intent?): IBinder?  = null

    override fun onDestroy() {
        Timber.wtf("Location service stopped!")

        this@LocationService.compositeDisposable.clear()
        this@LocationService.stopForeground(true)
        super.onDestroy()
    }

    companion object {
        const val LOCATION_FAST_INTERVAL = 2000L
        const val LOCATION_REQUEST_INTERVAL = 5000L
        const val LOCATION_JOB_NOTIFICATION_ID: Int = 9999

        fun startService(context: Context) {
            context.applicationContext.startService(getIntentService(context))
        }

        fun stopService(context: Context) {
            context.applicationContext.stopService(getIntentService(context))
        }

        private fun getIntentService(context: Context): Intent {
            val intent = Intent(context, LocationService::class.java)
            val componentName       = ComponentName(context.applicationContext.packageName, LocationService::class.java.name)
            intent.component = componentName
            return intent
        }
    }
}