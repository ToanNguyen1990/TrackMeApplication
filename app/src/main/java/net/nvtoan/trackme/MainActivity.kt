package net.nvtoan.trackme

import android.app.Activity
import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import net.nvtoan.trackme.app.utils.GpsUtil
import net.nvtoan.trackme.app.utils.PermissionUtil
import net.nvtoan.trackme.databinding.ActivityMainBinding
import net.toannt.hacore.ui.activity.HMBindingActivity
import timber.log.Timber

class MainActivity : HMBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            AppConstants.GPS_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    navController.navigate(R.id.goToTrainingFragment)
                }
            }
        }
    }

    override fun initUI() {
        super.initUI()
        navController = findNavController(R.id.navHostFragment)
        PermissionUtil.requestPermissionList(this, AppConstants.permissionList) { hasGranted: Boolean ->
            Timber.i("request location permission: $hasGranted")
        }
    }
}
