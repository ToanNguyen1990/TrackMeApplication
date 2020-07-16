package net.toannt.hacore.utils.noti

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.NotificationTarget
import java.util.*


object HMNotification {

    fun getNotificationBuilder(
        context: Context, @StringRes chanelIdRes: Int, title: String, content: String,
        @DrawableRes smallIconRes: Int? = null, @DrawableRes largeIconRes: Int? = null) : NotificationCompat.Builder {

        return NotificationCompat.Builder(context, context.getString(chanelIdRes)).apply {
            setContentTitle(title)
            setContentText(content)
            setSmallIcon(smallIconRes ?: android.R.drawable.ic_notification_overlay)
            priority = NotificationCompat.PRIORITY_HIGH
            setCategory(NotificationCompat.CATEGORY_MESSAGE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun registerNotificationChannel(context: Context, @StringRes channelIdRes: Int, @StringRes channelNameRes: Int, important: Int = NotificationManager.IMPORTANCE_DEFAULT) {
        val notificationManager = getNotificationManager(context)
        val channelId   = context.getString(channelIdRes)
        val channelName = context.getString(channelNameRes)

        val notificationChannel = NotificationChannel(channelId, channelName, important)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    fun notify(context: Context, builder: NotificationCompat.Builder, id: Int) {
        this@HMNotification.getNotificationManager(context).notify(id, builder.build())
    }

    fun getNotificationManager(context: Context): NotificationManager {
        return context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun cancel(context: Context, notificationId: Int) {
        this@HMNotification.getNotificationManager(context).cancel(notificationId)
    }

    fun createHeadsUpNotification(context: Context, channelId: String, channelName: String, smallIconRes: Int, contentIntent: PendingIntent, remoteView: RemoteViews, timeout: Long = 5000) {
        val notificationManager = when {
             Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                context.getSystemService(NotificationManager::class.java)
            }
            else -> {
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIconRes)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setChannelId(channelId)
            .setAutoCancel(true)
            .setTimeoutAfter(timeout)
            .setContent(remoteView)
            .setFullScreenIntent(contentIntent, true)
            .setCustomHeadsUpContentView(remoteView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        } else {
            builder.priority = NotificationCompat.PRIORITY_MAX
        }

        val notification = builder.build()
        notification.flags = notification.flags or android.app.Notification.FLAG_AUTO_CANCEL
        val notifyId = ((Date().time / 1000L) % Integer.MAX_VALUE).toInt()
        notificationManager.notify(notifyId, notification)
    }

}