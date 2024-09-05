package com.example.lahwibu.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lahwibu.data.database.entity.RemoteKeysComplete
import com.example.lahwibu.data.database.entity.RemoteKeysOngoing

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllOngoing(remoteKeys: List<RemoteKeysOngoing>)

    @Query("SELECT * FROM remote_keys_ongoing WHERE id = :id")
    suspend fun getRemoteKeysIdOngoing(id: String): RemoteKeysOngoing?

    @Query("DELETE FROM remote_keys_ongoing")
    suspend fun deleteRemoteKeysOngoing()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllComplete(remoteKeys: List<RemoteKeysComplete>)

    @Query("SELECT * FROM remote_keys_complete WHERE id = :id")
    suspend fun getRemoteKeysIdComplete(id: String): RemoteKeysComplete?

    @Query("DELETE FROM remote_keys_complete")
    suspend fun deleteRemoteKeysComplete()


}