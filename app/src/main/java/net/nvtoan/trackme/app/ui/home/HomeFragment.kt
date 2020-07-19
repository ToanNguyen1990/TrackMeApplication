package net.nvtoan.trackme.app.ui.home

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_home.*
import net.nvtoan.trackme.AppConstants
import net.nvtoan.trackme.R
import net.nvtoan.trackme.app.db.TrackMeDatabase
import net.nvtoan.trackme.app.extension.initWithDecoration
import net.nvtoan.trackme.app.ui.adapter.HistoryItemAdapter
import net.nvtoan.trackme.app.ui.popup.WarningDialogFragment
import net.nvtoan.trackme.app.utils.GpsUtil
import net.nvtoan.trackme.app.utils.PermissionUtil
import net.nvtoan.trackme.databinding.FragmentHomeBinding
import net.toannt.hacore.ui.fragment.HMBindingFragment
import timber.log.Timber
import java.util.concurrent.TimeUnit

class HomeFragment: HMBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private lateinit var gpsUtil: GpsUtil
    private val historyItemAdapter = HistoryItemAdapter()
    private val database = TrackMeDatabase.get()

    override fun executeOnBackPressed() {
        super.executeOnBackPressed()
        onExecuteFinishActivity()
    }

    override fun initUI(parentView: View) {
        gpsUtil = GpsUtil(parentView.context)
        historyRecyclerView.initWithDecoration(parentView.context)
        historyRecyclerView.setHasFixedSize(true)
        historyRecyclerView.isNestedScrollingEnabled = true
        historyRecyclerView.setRecyclerListener(historyItemAdapter.mRecycleListener)
        historyRecyclerView.adapter = historyItemAdapter

        val trainingDisposable = RxView.clicks(btnTraining)
            .throttleFirst(2000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                requestLocationPermission(requireActivity())
            }

        compositeDisposable.add(trainingDisposable)
    }

    override fun registerObservables() {
        database.historyDao().getAll().observe(viewLifecycleOwner, Observer {
            historyRecyclerView.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            txtNoHistory.visibility = if (it.isNotEmpty()) View.GONE else View.VISIBLE
            historyItemAdapter.updateItems(it)
        })
    }

    private fun requestLocationPermission(context: FragmentActivity) {
        PermissionUtil.requestPermissionList(context, AppConstants.permissionList) { hasGranted ->
            when (hasGranted) {
                true -> {
                    gpsUtil.turnGPSOn { hasTurnOn ->
                        Timber.i("hasTurnOn: $hasTurnOn")
                        if (hasTurnOn) {
                            findNavController().navigate(R.id.goToTrainingFragment)
                        }
                    }
                }

                else -> {
                    // show open setting to access enabling permission
                    Timber.i("permission deni")
                    val settingDialog = WarningDialogFragment()

                }
            }
        }
    }
}