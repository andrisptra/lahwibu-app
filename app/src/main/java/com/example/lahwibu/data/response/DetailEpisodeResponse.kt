package com.example.lahwibu.data.response

import com.google.gson.annotations.SerializedName

data class DetailEpisodeResponse(

	@field:SerializedName("next_episode_number")
	val nextEpisodeNumber: String? = null,

	@field:SerializedName("videoList")
	val videoList: List<VideoListItem?>? = null,

	@field:SerializedName("anime_code")
	val animeCode: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("anime_id")
	val animeId: String? = null
)

data class VideoListItem(

	@field:SerializedName("size")
	val size: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
