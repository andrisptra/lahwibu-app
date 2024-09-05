package com.example.lahwibu.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys_complete")
data class RemoteKeysComplete(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)