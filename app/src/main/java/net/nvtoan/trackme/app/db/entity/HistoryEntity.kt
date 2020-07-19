package net.nvtoan.trackme.app.db.entity

import android.location.Location
import androidx.room.Entity
import androidx.room.TypeConverters
import net.nvtoan.trackme.app.db.converter.DataConverter
import net.toannt.hacore.entities.HMModel

@Entity(tableName = "history_entity")
@TypeConverters(DataConverter::class)
data class HistoryEntity(var distance: Double = 0.0,  var currentSpeed: Float = 0F,
                         var speedList: List<Float> = arrayListOf(),
                         var durationTime: Int = 0, var locations: List<String> = arrayListOf(),
                         var isRecording: Boolean = false): HMModel()