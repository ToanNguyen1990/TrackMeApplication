package net.nvtoan.trackme.app.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import net.nvtoan.trackme.app.db.entity.HistoryEntity

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history_entity ORDER BY localId DESC")
    fun getAll(): LiveData<List<HistoryEntity>>

    @Query("SELECT * FROM history_entity WHERE isRecording = :isRecording" )
    fun getTrainingHistoryLiveData(isRecording: Boolean = true): LiveData<HistoryEntity?>

    @Query("SELECT * FROM history_entity WHERE isRecording = :isRecording AND localId = :localId" )
    fun getHistoryEntityLiveDataBy(isRecording: Boolean = true, localId: Long): LiveData<HistoryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(historyEntity: HistoryEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(historyEntity: HistoryEntity)

}