package net.nvtoan.trackme.app.ui.home

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import net.nvtoan.trackme.app.db.TrackMeDatabase
import net.nvtoan.trackme.app.db.entity.HistoryEntity
import net.toannt.hacore.HMViewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit


class TrainingViewModel: HMViewModel() {

    private val durationTimeLiveData = MutableLiveData(0)
    private val recordEnumTypeLiveData = MutableLiveData(RecordEnumType.STARTED)
    private val database = TrackMeDatabase.get()
    var currentHistoryEntity: HistoryEntity? = null

    fun setRecordEnumType(recordEnumType: RecordEnumType) {
        this.recordEnumTypeLiveData.postValue(recordEnumType)
    }

    fun getRecordEnumType(): MutableLiveData<RecordEnumType> {
        return recordEnumTypeLiveData
    }

    fun setDurationTime(duration: Int) {
        durationTimeLiveData.postValue(duration)
        currentHistoryEntity?.let {
            val currentSpeed = calculateCurrentSpeed(it.distance, duration)
            val newSpeedList = arrayListOf<Float>()
            newSpeedList.addAll(it.speedList)
            newSpeedList.add(currentSpeed)
            it.durationTime = duration
            it.currentSpeed = currentSpeed
            it.speedList = newSpeedList
            database.historyDao().update(it)
        }
    }

    fun getDurationTime(): MutableLiveData<Int> {
        return durationTimeLiveData
    }

    fun insertHistoryEntityIntoDB(): Long {
        var localId = 0L
        currentHistoryEntity = HistoryEntity(isRecording = true)
        currentHistoryEntity?.let {
            localId = database.historyDao().insert(it)
            it.localId = localId
        }
        return localId
    }

    fun updateHistoryEntity(newLocation: Location, isRecording: Boolean = true) {
        currentHistoryEntity?.let {
            val newLocations = arrayListOf<String>()
            val locationTemp = "${newLocation.latitude},${newLocation.longitude}"
            newLocations.addAll(it.locations)
            if (newLocations.isEmpty() || newLocations[newLocations.size - 1] != locationTemp) {
                newLocations.add(locationTemp)
            }
            val points = arrayListOf<LatLng>()
            it.locations.forEach {
                val location = it.split(",")
                points.add(LatLng(location[0].toDouble(), location[1].toDouble()))
            }
            val distance = getDistance(points)
            it.distance = distance
            it.isRecording = isRecording
            it.locations = newLocations
            val currentSpeed = calculateCurrentSpeed(it.distance, it.durationTime)
            val newSpeedList = arrayListOf<Float>()
            newSpeedList.addAll(it.speedList)
            newSpeedList.add(currentSpeed)
            it.currentSpeed = currentSpeed
            it.speedList = newSpeedList
            database.historyDao().update(it)
        }
    }

    // current speed km/h
    private fun calculateCurrentSpeed(distance: Double, duration: Int): Float {
        if (distance == 0.0) return 0F
        return (distance.toFloat() / duration) * 3.6F
    }

    fun getDurationString(duration: Int): String {
        val hours = duration / 3600
        val minutes = (duration % 3600) / 60
        val seconds = duration % 60
        val minuteStr = getTimeString(minutes)
        val secondStr =  getTimeString(seconds)
        return when {
            hours > 0 -> {
                val hourStr = getTimeString(hours)
                "$hourStr:$minuteStr:$secondStr"
            }
            else -> "$minuteStr:$secondStr"
        }
    }

    private fun getTimeString(time: Int): String {
        return when {
            time < 10 -> "0$time"
            else -> "$time"
        }
    }

    fun convertDistanceToString(distance: Double): String {
        return String.format("%.1f", distance)
    }

    fun convertSpeedToString(speed: Float): String {
        return String.format("%.1f", speed)
    }

    private fun getDistance(points: List<LatLng>): Double {
        var total = 0.0
        for (i in 0 until points.size - 1) {
            total += distanceBetweenTwoPoint(points[i], points[i+1])
        }
        return total
    }

    fun distanceBetweenTwoPoint(oldPoint: LatLng, newPoint: LatLng): Double {
        return SphericalUtil.computeDistanceBetween(oldPoint, newPoint)
    }

    enum class RecordEnumType {
        STARTED,
        RECORDED,
        PAUSED
    }

}