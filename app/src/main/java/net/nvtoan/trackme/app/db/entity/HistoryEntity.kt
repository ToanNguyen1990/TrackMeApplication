package net.nvtoan.trackme.app.db.entity

import androidx.room.Entity
import net.toannt.hacore.entities.HMModel

@Entity(tableName = "history_entity")
data class HistoryEntity(var name: String = ""): HMModel()