package net.nvtoan.trackme.app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes

object NotificationUtil {

    fun getNotificationManager(context: Context): NotificationManager {
        return context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun registerNotificationChannel(context: Context, @StringRes channelIdRes: Int, @StringRes channelNameRes: Int, important: Int = NotificationManager.IMPORTANCE_DEFAULT) {
        val notificationManager = this.getNotificationManager(context)
        val channelId   = context.getString(channelIdRes)
        val channelName = context.getString(channelNameRes)

        val notificationChannel = NotificationChannel(channelId, channelName, important)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}