package com.example.lahwibu.data.response

import com.google.gson.annotations.SerializedName

data class DetailAnimeResponse(

	@field:SerializedName("studio")
	val studio: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("totalEpisodes")
	val totalEpisodes: String? = null,

	@field:SerializedName("release")
	val release: String? = null,

	@field:SerializedName("alternativeTitle")
	val alternativeTitle: String? = null,

	@field:SerializedName("adaptation")
	val adaptation: String? = null,

	@field:SerializedName("enthusiast")
	val enthusiast: String? = null,

	@field:SerializedName("synopsis")
	val synopsis: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("quality")
	val quality: String? = null,

	@field:SerializedName("demographic")
	val demographic: String? = null,

	@field:SerializedName("duration")
	val duration: String? = null,

	@field:SerializedName("score")
	val score: String? = null,

	@field:SerializedName("eksplisit")
	val eksplisit: String? = null,

	@field:SerializedName("genres")
	val genres: List<String?>? = null,

	@field:SerializedName("ratings")
	val ratings: String? = null,

	@field:SerializedName("season")
	val season: String? = null,

	@field:SerializedName("episodeList")
	val episodeList: List<EpisodeListItem?>? = null,

	@field:SerializedName("theme")
	val theme: String? = null,

	@field:SerializedName("credit")
	val credit: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class EpisodeListItem(

	@field:SerializedName("episode_number")
	val episodeNumber: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
