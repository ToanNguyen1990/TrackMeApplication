package net.nvtoan.trackme.app.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import timber.log.Timber

object PermissionUtil {

    fun hasPermissions(context: Context, permissions: List<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionList(activity: FragmentActivity, permissionList: List<String>, onCallback: ((hasGranted: Boolean)-> Unit)? = null) {
        Dexter.withActivity(activity)
            .withPermissions(permissionList)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.isAnyPermissionPermanentlyDenied) {
                        Timber.i("permission denied: ${report.deniedPermissionResponses.size}")
                        onCallback?.invoke(false)
                        return
                    }

                    if (report.areAllPermissionsGranted()) {
                        onCallback?.invoke(true)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Toast.makeText(activity, "Request Permission Error! ", Toast.LENGTH_SHORT).show()
            }
            .onSameThread().check()
    }
}