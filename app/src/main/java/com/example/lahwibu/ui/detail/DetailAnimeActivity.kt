package com.example.lahwibu.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.lahwibu.data.response.EpisodeListItem
import com.example.lahwibu.databinding.ActivityDetailAnimeBinding
import com.example.lahwibu.ui.episode.EpisodeDetailActivity
import com.example.lahwibu.utils.Result
import com.example.lahwibu.utils.ViewModelFactory

class DetailAnimeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailAnimeBinding
    private val viewModel: DetailAnimeViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAnimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val animeCode = intent.getStringExtra(ANIME_CODE).toString()
        val animeId = intent.getStringExtra(ANIME_ID).toString()
        Log.e("detailACtv",animeCode)
        getDetailAnime(animeCode, animeId)

    }

    private fun getDetailAnime(animeCode: String, animeId: String) {
        viewModel.getDetailAnime(animeCode, animeId).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {

                    }

                    is Result.Success -> {
                        val animeDetail = result.data
                        val episodeList = animeDetail.episodeList
                        with(binding) {
                            titleAnime.text = animeDetail.title
                            tvRating.text = animeDetail.score
                            tvType.text = animeDetail.type
                            tvStatus.text = animeDetail.status
                            descSinopsis.text = animeDetail.synopsis
                            descStudio.text = animeDetail.studio

                            Glide.with(binding.root)
                                .load(animeDetail.image)
                                .into(coverImage)
                        }
                        setEpisodeList(episodeList)

                    }

                    is Result.Error -> {

                    }
                }
            }
        }

    }

    private fun setEpisodeList(data: List<EpisodeListItem?>?) {
        val adapter = EpisodeListAdapter()
        adapter.submitList(data)
        val layoutManager = LinearLayoutManager(this)
        with(binding) {
            rvEpisodeList.layoutManager = layoutManager
            rvEpisodeList.adapter = adapter
            rvEpisodeList.isNestedScrollingEnabled = false
        }
        adapter.setOnItemClickCallback(object : EpisodeListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: EpisodeListItem) {
                val animeCode = intent.getStringExtra(ANIME_CODE).toString()
                val animeId = intent.getStringExtra(ANIME_ID).toString()
                val intent = Intent(this@DetailAnimeActivity, EpisodeDetailActivity::class.java)
                intent.putExtra(EpisodeDetailActivity.ANIME_CODE, animeCode)
                intent.putExtra(EpisodeDetailActivity.ANIME_ID, animeId)
                intent.putExtra(EpisodeDetailActivity.EPS, data.episodeNumber)
                startActivity(intent)
            }

        })
    }


    companion object {
        const val ANIME_CODE = "anime_code"
        const val ANIME_ID = "anime_id"
    }
}