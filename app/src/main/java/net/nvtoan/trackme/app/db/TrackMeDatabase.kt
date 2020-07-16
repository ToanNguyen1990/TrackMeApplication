package net.nvtoan.trackme.app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.nvtoan.trackme.app.db.entity.HistoryEntity

@Database(
    entities = [
        HistoryEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class TrackMeDatabase: RoomDatabase() {

    companion object {
        @Volatile
        private var appDataBase: TrackMeDatabase? = null

        fun initDatabase(context: Context) {
            if (this@Companion.appDataBase != null) return

            this@Companion.appDataBase = Room.databaseBuilder(context.applicationContext,
                TrackMeDatabase::class.java, "${context.applicationContext.packageName}.trackme.sqlite3")
                .enableMultiInstanceInvalidation()
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }

        fun get(): TrackMeDatabase {
            return this@Companion.appDataBase ?: throw Exception("AppDatabase is not init already")
        }
    }
}