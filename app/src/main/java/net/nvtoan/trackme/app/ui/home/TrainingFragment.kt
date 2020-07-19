package net.nvtoan.trackme.app.ui.home

import android.content.Context
import android.graphics.Color
import android.location.Location
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_training.*
import net.nvtoan.trackme.R
import net.nvtoan.trackme.app.db.TrackMeDatabase
import net.nvtoan.trackme.app.db.entity.HistoryEntity
import net.nvtoan.trackme.app.ui.popup.NotifyDialogFragment
import net.nvtoan.trackme.app.utils.GooglePlayServiceUtil
import net.nvtoan.trackme.databinding.FragmentTrainingBinding
import net.toannt.hacore.datasources.HMViewModelSource
import net.toannt.hacore.ui.fragment.HMBindingFragment
import net.toannt.hacore.utils.ResourceUtil
import timber.log.Timber
import java.util.concurrent.TimeUnit

class TrainingFragment: HMBindingFragment<FragmentTrainingBinding>(R.layout.fragment_training),
    OnMapReadyCallback, HMViewModelSource<TrainingViewModel> {

    private var googleMap: GoogleMap? = null
    private var startLocation: Location? = null
    private var increaseTimeDisposable: Disposable? = null
    private lateinit var presenter: TrainingPresenter
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val database = TrackMeDatabase.get()
    private var startMarker: Marker? = null
    private var currentMarker: Marker? = null

    override val viewModel: TrainingViewModel by lazy {
        ViewModelProviders.of(this)[TrainingViewModel::class.java]
    }

    override fun executeOnBackPressed() {
        findNavController().popBackStack()
    }

    override fun initUI(parentView: View) {
        // init data binding
        presenter = TrainingPresenter(parentView.context)
        dataBinding.presenter = presenter

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(parentView.context)
        if (!GooglePlayServiceUtil.isGooglePlayServicesAvailable(parentView.context)) {
            Toast.makeText(parentView.context, parentView.context.resources.getString(R.string.device_no_support_google_play_service), Toast.LENGTH_LONG).show()
            findNavController().popBackStack()
            return
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.googleMapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val startDisposable = RxView.clicks(btnStart)
            .throttleFirst(2000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val localId = viewModel.insertHistoryEntityIntoDB()
                resetTimeDuration()
                viewModel.setRecordEnumType(TrainingViewModel.RecordEnumType.RECORDED)
                presenter.initLocation()
                presenter.resetObserveHistoryEntity(localId)
            }
        compositeDisposable.add(startDisposable)

        val pauseDisposable = RxView.clicks(btnPause)
            .throttleFirst(2000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                removeTimeDurationDisposable()
                viewModel.setRecordEnumType(TrainingViewModel.RecordEnumType.PAUSED)
            }
        compositeDisposable.add(pauseDisposable)

        val resumeDisposable = RxView.clicks(btnResume)
            .throttleFirst(2000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewModel.setRecordEnumType(TrainingViewModel.RecordEnumType.RECORDED)
                resetTimeDuration()
            }
        compositeDisposable.add(resumeDisposable)

        val finishDisposable = RxView.clicks(btnFinish)
            .throttleFirst(2000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                presenter.finishRecording()
            }
        compositeDisposable.add(finishDisposable)
    }

    override fun registerObservables() {
        viewModel.getDurationTime().observe(viewLifecycleOwner, Observer { duration ->
            val durationStr = viewModel.getDurationString(duration)
            txtDuration.text = durationStr
        })

        viewModel.getRecordEnumType().observe(viewLifecycleOwner, Observer { recordEnumType ->
            presenter.recordEnumObservable.set(recordEnumType)
        })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        Timber.i("onMapReady")
        MapsInitializer.initialize(context)
        this.googleMap = googleMap
        this.googleMap?.setMinZoomPreference(CAMERA_ZOOM_DEFAULT_LEVEL)
        this.googleMap?.setMaxZoomPreference(CAMERA_ZOOM_DEFAULT_LEVEL)
        this.googleMap?.uiSettings?.isZoomGesturesEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.removeLocation()
    }

    private fun resetTimeDuration() {
        removeTimeDurationDisposable()
        increaseTimeDisposable = Observable.interval(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val duration = viewModel.getDurationTime().value ?: 0
                viewModel.setDurationTime(duration + 1)
            }

        increaseTimeDisposable?.let {
            compositeDisposable.add(it)
        }
    }

    private fun removeTimeDurationDisposable() {
        increaseTimeDisposable?.let {
            compositeDisposable.remove(it)
        }
    }

    inner class TrainingPresenter(var context: Context) {
        private val locationRequest = LocationRequest()
        private var locationCallback: LocationCallback? = null
        val recordEnumObservable = ObservableField(TrainingViewModel.RecordEnumType.STARTED)

        fun initLocation() {
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = LOCATION_REQUEST_INTERVAL
            locationRequest.fastestInterval = LOCATION_FAST_INTERVAL
            locationRequest.smallestDisplacement = 1f

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                startLocation = location
                val startLatLng = LatLng(location.latitude, location.longitude)
                val markerOptions = MarkerOptions().position(startLatLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(ResourceUtil.getBitmap(context, R.drawable.ic_my_location_24px)))
                    .anchor(0.5f,0.5f)
                currentMarker = googleMap?.addMarker(markerOptions)
                googleMap?.animateCamera(CameraUpdateFactory.newLatLng(startLatLng))
                viewModel.updateHistoryEntity(location)
            }

            val locationSettings = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
            val settingsClient = LocationServices.getSettingsClient(context)
            settingsClient.checkLocationSettings(locationSettings)
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    if (recordEnumObservable.get() == TrainingViewModel.RecordEnumType.RECORDED) {
                        locationResult.lastLocation?.let {
                            viewModel.updateHistoryEntity(it)
                        }
                    }
                }
            }
            LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(locationRequest, locationCallback, null)
        }

        private fun drawSessionOnMap(historyEntity: HistoryEntity) {
            try {
                val points = arrayListOf<LatLng>()
                historyEntity.locations.forEach {
                    val location = it.split(",")
                    points.add(LatLng(location[0].toDouble(), location[1].toDouble()))
                }
                val line: Polyline = googleMap?.addPolyline(PolylineOptions().width(5f)?.color(Color.RED)) ?: return
                line.points = points
                txtDistance.text = viewModel.convertDistanceToString(historyEntity.distance / 1000)
                txtSpeed.text = viewModel.convertSpeedToString(historyEntity.currentSpeed)

                if (points.size < 1) return

                if (startMarker == null && historyEntity.distance > 30) {
                    val markerOptions = MarkerOptions().position(points[0])
                        .icon(BitmapDescriptorFactory.fromBitmap(ResourceUtil.getBitmap(context, R.drawable.ic_circle_green)))
                        .anchor(0.5f,0.5f)
                    startMarker = googleMap?.addMarker(markerOptions)
                }

                currentMarker?.position = points[points.size - 1]
            } catch (e: Exception) {
                Timber.i("drawLines exception: $e")
            }
        }

        fun removeLocation() {
            locationCallback?.let {
                LocationServices.getFusedLocationProviderClient(context).removeLocationUpdates(it)
            }
            locationCallback = null
        }

        fun finishRecording() {
            Timber.i("finishRecording")
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                viewModel.updateHistoryEntity(location, false)
            }
            removeLocation()
            findNavController().popBackStack()
        }

        fun resetObserveHistoryEntity(localId: Long) {
            database.historyDao().getHistoryEntityLiveDataBy(localId =  localId).observe(viewLifecycleOwner, Observer { historyEntity ->
                historyEntity?.let {
                    drawSessionOnMap(it)
                }
            })
        }
    }

    companion object {
        const val CAMERA_ZOOM_DEFAULT_LEVEL = 18f
        const val LOCATION_FAST_INTERVAL = 2000L
        const val LOCATION_REQUEST_INTERVAL = 10000L
    }
}