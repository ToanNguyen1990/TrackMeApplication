package net.nvtoan.trackme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.StrictMode
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import net.nvtoan.trackme.app.db.TrackMeDatabase
import timber.log.Timber
import timber.log.Timber.DebugTree

class TrackMeApplication: MultiDexApplication(), LifecycleObserver {

    var hasAppForegrounded = false

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        val builder = StrictMode.VmPolicy.Builder()
        builder.detectFileUriExposure()
        StrictMode.setVmPolicy(builder.build())
        Timber.plant(DebugTree())

        TrackMeDatabase.initDatabase(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        hasAppForegrounded = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        hasAppForegrounded = true
    }


}