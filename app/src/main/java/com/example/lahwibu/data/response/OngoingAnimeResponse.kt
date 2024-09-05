package com.example.lahwibu.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lahwibu.ui.home.HomeFragment
import com.google.gson.annotations.SerializedName

data class OngoingAnimeResponse(

    @field:SerializedName("data")
    val data: List<DataItemOngoing> = emptyList(),

    @field:SerializedName("nextPage")
    val nextPage: Boolean? = null,

    @field:SerializedName("prevPage")
    val prevPage: Boolean? = null
)

@Entity(tableName = "ongoing_list")
data class DataItemOngoing(
    @PrimaryKey
    @field:SerializedName("anime_id")
    override val animeId: String,

    @field:SerializedName("image")
    override val image: String? = null,

    @field:SerializedName("anime_code")
    override val animeCode: String? = null,

    @field:SerializedName("episode")
    val episode: String? = null,

    @field:SerializedName("title")
    override val title: String? = null,

    @field:SerializedName("type")
    val type: List<String> = emptyList(),


    ) : HomeFragment.DataItem
