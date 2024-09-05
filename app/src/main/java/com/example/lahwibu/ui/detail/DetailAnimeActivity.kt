package com.example.lahwibu.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.lahwibu.adapter.EpisodeListAdapter
import com.example.lahwibu.data.response.DetailAnimeResponse
import com.example.lahwibu.data.response.EpisodeListItem
import com.example.lahwibu.databinding.ActivityDetailAnimeBinding
import com.example.lahwibu.ui.episode.EpisodeDetailActivity
import com.example.lahwibu.utils.Result
import com.example.lahwibu.utils.ViewModelFactory
import com.example.lahwibu.viewmodel.DetailAnimeViewModel

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
        getDetailAnime(animeCode, animeId)
        setBackIconButton()

    }

    private fun getDetailAnime(animeCode: String, animeId: String) {
        viewModel.getDetailAnime(animeCode, animeId).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        setLoadingProgress(true)
                    }

                    is Result.Success -> {
                        setLoadingProgress(false)
                        val anime = result.data
                        setAnimeDetail(anime)
                    }

                    is Result.Error -> {
                        setLoadingProgress(false)
                    }
                }
            }
        }
    }

    private fun setAnimeDetail(anime: DetailAnimeResponse) {
        val genre = anime.genres?.joinToString(separator = ", ")
        binding.topAppBar.title = anime.title
        with(binding.content) {
            tvScore.text = anime.score
            tvRatings.text = anime.ratings
            tvGenre.text = genre
            tvType.text = anime.type
            tvStatus.text = anime.status
            descSinopsis.text = anime.synopsis
            descStudio.text = anime.studio
        }
        Glide.with(binding.root)
            .load(anime.image)
            .into(binding.coverImage)
        setEpisodeList(anime.episodeList)
    }

    private fun setEpisodeList(data: List<EpisodeListItem?>?) {
        val adapter = EpisodeListAdapter()
        adapter.submitList(data)
        val layoutManager = LinearLayoutManager(this)
        with(binding.content) {
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

    private fun setLoadingProgress(loading: Boolean) {
        if (loading) {
            binding.content.constraintLayout.visibility = View.GONE
            binding.loadingIndicator.visibility = View.VISIBLE

        } else {
            binding.content.constraintLayout.visibility = View.VISIBLE
            binding.loadingIndicator.visibility = View.GONE
        }

    }

    private fun setBackIconButton() {
        binding.materialToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }


    companion object {
        const val ANIME_CODE = "anime_code"
        const val ANIME_ID = "anime_id"
    }
}