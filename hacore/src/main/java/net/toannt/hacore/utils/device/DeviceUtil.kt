package net.toannt.hacore.utils.device

import android.bluetooth.BluetoothClass
import android.content.Context
import android.provider.Settings

object DeviceUtil {

    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getVersionCode(context: Context): Long {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.longVersionCode
    }

    const val clickInterVal = 600;

}