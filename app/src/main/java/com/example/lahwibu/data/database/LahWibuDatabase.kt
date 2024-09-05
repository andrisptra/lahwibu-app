package com.example.lahwibu.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lahwibu.data.Converter
import com.example.lahwibu.data.database.dao.AnimeDao
import com.example.lahwibu.data.database.dao.RemoteKeysDao
import com.example.lahwibu.data.database.entity.RemoteKeysComplete
import com.example.lahwibu.data.database.entity.RemoteKeysOngoing
import com.example.lahwibu.data.response.DataItemCompleted
import com.example.lahwibu.data.response.DataItemOngoing

@TypeConverters(Converter::class)
@Database(
    entities = [DataItemOngoing::class, DataItemCompleted::class, RemoteKeysOngoing::class, RemoteKeysComplete::class],
    version = 3,
    exportSchema = false
)
abstract class LahWibuDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: LahWibuDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): LahWibuDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    LahWibuDatabase::class.java, "lahwibu_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}