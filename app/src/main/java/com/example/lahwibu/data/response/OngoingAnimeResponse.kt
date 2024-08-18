package com.example.lahwibu.data.response

import com.google.gson.annotations.SerializedName

data class OngoingAnimeResponse(

	@field:SerializedName("data")
	val data: List<DataItemOngoing?>? = null,

	@field:SerializedName("nextPage")
	val nextPage: Boolean? = null,

	@field:SerializedName("prevPage")
	val prevPage: Boolean? = null
)

data class DataItemOngoing(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("anime_code")
	val animeCode: String? = null,

	@field:SerializedName("episode")
	val episode: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: List<String?>? = null,

	@field:SerializedName("anime_id")
	val animeId: String? = null
)
