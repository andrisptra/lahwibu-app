package com.example.lahwibu.data.response

import com.google.gson.annotations.SerializedName

data class SearchAnimeResponse(

	@field:SerializedName("data")
	val data: List<DataItemSearch?>? = null,

	@field:SerializedName("nextPage")
	val nextPage: Boolean? = null,

	@field:SerializedName("prevPage")
	val prevPage: Boolean? = null
)

data class DataItemSearch(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("score")
	val score: String? = null,

	@field:SerializedName("anime_code")
	val animeCode: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: List<String?>? = null,

	@field:SerializedName("anime_id")
	val animeId: String? = null
)
