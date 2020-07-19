package net.nvtoan.trackme.app.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.viewholder_history_item.view.*
import net.nvtoan.trackme.R
import net.nvtoan.trackme.app.db.entity.HistoryEntity
import net.nvtoan.trackme.app.extension.convertToString
import net.nvtoan.trackme.app.extension.convertToTimeString
import net.nvtoan.trackme.app.utils.MapUtil
import net.toannt.hacore.utils.ResourceUtil
import net.toannt.hacore.utils.recycleradapter.HMSingleModelRecyclerAdapter
import timber.log.Timber
import java.util.*


class HistoryItemAdapter:
    HMSingleModelRecyclerAdapter<HistoryEntity, HistoryItemAdapter.HistoryItemViewHolder>(){

    init {
        setHasStableIds(true)
    }

    val mRecycleListener = RecyclerView.RecyclerListener { holder ->
        val mapHolder: HistoryItemViewHolder? = holder as? HistoryItemViewHolder
        if (mapHolder?.googleMap != null) {
            mapHolder.googleMap?.clear()
            mapHolder.googleMap?.mapType = MAP_TYPE_NONE
        }
    }

    override fun getItemId(position: Int): Long {
        return actualItems[position].localId
    }

    override fun onCheckItem(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
        return oldItem.localId == newItem.localId
    }

    override fun onCheckItemContents(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
        return oldItem.durationTime == newItem.durationTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(parent)
    }

    inner class HistoryItemViewHolder(parentView: ViewGroup)
        : HMSingleModelRecyclerAdapter.HMSingleModelViewHolder<HistoryEntity>(parentView, R.layout.viewholder_history_item), OnMapReadyCallback {

        var googleMap: GoogleMap? = null
        var historyEntity: HistoryEntity? = null

        init {
            with(itemView.googleMapView) {
                itemView.googleMapView.onCreate(null)
                itemView.googleMapView.getMapAsync(this@HistoryItemViewHolder)
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bind(item: HistoryEntity, position: Int) {
            super.bind(item, position)
            historyEntity = item
            itemView.txtDistance.text = "${(item.distance / 1000).convertToString()} km"
            itemView.txtAvgSpeed.text = getAverageSpeed(item.speedList)
            itemView.txtDuration.text = item.durationTime.convertToTimeString()
            itemView.googleMapView.tag = item
            drawMapContent()
        }

        override fun onMapReady(googleMap: GoogleMap?) {
            Timber.i("onMapReady")
            MapsInitializer.initialize(itemView.context)
            this.googleMap = googleMap ?: return
            this.googleMap?.uiSettings?.isZoomGesturesEnabled = false
            this.googleMap?.uiSettings?.isMapToolbarEnabled = true
            this.googleMap?.uiSettings?.setAllGesturesEnabled(false)
            drawMapContent()
        }

        private fun drawMapContent() {
            if (googleMap == null || itemView.googleMapView.tag == null) return
            googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            val polylineOptions = PolylineOptions()
            val routeList = arrayListOf<LatLng>()
            historyEntity?.locations?.forEach { locationStr ->
                val location = locationStr.split(",")
                val newLatLng = LatLng(location[0].toDouble(), location[1].toDouble())
                polylineOptions.add(newLatLng)
                routeList.add(newLatLng)
            }
            polylineOptions.width(5f).color(Color.BLUE).geodesic(true)
            this.googleMap?.addPolyline(polylineOptions)

            if (routeList.isEmpty()) return
            val startLatLng = routeList[0]
            googleMap?.addMarker(
                MarkerOptions().position(startLatLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(ResourceUtil.getBitmap(itemView.context, R.drawable.ic_circle_green)))
                    .anchor(0.5f,0.5f)
            )

            val endLatLng = when (routeList.size == 1) {
                true -> startLatLng
                else -> {
                    routeList[routeList.size - 1]
                }
            }

            googleMap?.addMarker(
                MarkerOptions().position(endLatLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(ResourceUtil.getBitmap(itemView.context, R.drawable.ic_circle_red)))
                    .anchor(0.5f,0.5f)
            )
            googleMap?.let {
                val distance = historyEntity?.distance ?: 0.0
                if (distance < 0.15) {
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(endLatLng, 17f))
                } else {
                    MapUtil.zoomRoute(it, routeList)
                }
            }
        }

        private fun getAverageSpeed(speedList: List<Float>): String {
            var totalCount = 0F
            speedList.forEach { speed ->
                totalCount += speed
            }
            return when (totalCount) {
                0F -> {
                    "0 km/h"
                }
                else -> {
                    val avgSpeed = (totalCount / speedList.size)
                    "${avgSpeed.convertToString()} km/h"
                }
            }
        }
    }
}