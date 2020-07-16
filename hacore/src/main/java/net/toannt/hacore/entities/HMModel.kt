package net.toannt.hacore.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.io.Serializable
import java.util.*

@Entity
abstract class HMModel : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "localId")
    @Expose(serialize = false, deserialize = false)
    var localId: Long = 0

    var timeCreated: Long? = null

    var timeSynced: Long? = null
}