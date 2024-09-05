package com.example.lahwibu.ui.episode

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lahwibu.R
import com.example.lahwibu.adapter.EpisodeListAdapter
import com.example.lahwibu.data.response.EpisodeListItem
import com.example.lahwibu.data.response.VideoListItem
import com.example.lahwibu.databinding.ActivityEpisodeDetailBinding
import com.example.lahwibu.ui.detail.DetailAnimeActivity
import com.example.lahwibu.utils.Result
import com.example.lahwibu.utils.ViewModelFactory
import com.example.lahwibu.viewmodel.EpisodeDetailViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EpisodeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEpisodeDetailBinding
    private val viewModel: EpisodeDetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var exoPlayer: ExoPlayer? = null
    private var isFullScreen: Boolean = false
    private var videoListAnime: List<VideoListItem?>? = null
    private var selectedVideoIndex: Int = 0


    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEpisodeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        observeViewModel()
        setupOnBackPressed()
    }

    override fun onStart() {
        super.onStart()
        if (binding.playerView.player != null) {
            binding.playerView.player!!.playWhenReady = true
        }
    }

    override fun onStop() {
        super.onStop()
        binding.playerView.player?.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.playerView.player?.release()
    }

    private fun setupUI() {
        binding.playerView.setFullscreenButtonClickListener {
            if (isFullScreen) exitFullScreen() else enterFullScreen()
        }
        binding.btnDownload.setOnClickListener { selectDownloadQuality() }
        binding.btnQuality.setOnClickListener {
            selectVideoQuality()
        }
        binding.btnShare.setOnClickListener { }
    }

    private fun observeViewModel() {
        val animeCode = intent.getStringExtra(ANIME_CODE).toString()
        val animeId = intent.getStringExtra(ANIME_ID).toString()
        val epsNumber = intent.getStringExtra(EPS).toString()

        viewModel.getDetailEpisode(animeCode, animeId, epsNumber).observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    result.data.let { animeDetail ->
                        videoListAnime = animeDetail.videoList
                        binding.animeTitle.text = animeDetail.title
                        val url = animeDetail.videoList?.getOrNull(2)?.url
                        setExoplayerVideo(url)
                    }
                }

                is Result.Error -> showLoading(false)
            }
        }

        viewModel.getDetailAnime(animeCode, animeId).observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    val episodeList = result.data.episodeList
                    setEpisodeList(episodeList)
                }

                is Result.Error -> showLoading(false)
            }
        }
    }

    private fun setupOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (exoPlayer != null) {
                    exoPlayer!!.stop()
                    exoPlayer!!.release()
                    exoPlayer = null
                }
                finish()
            }
        })
    }

    private fun changeQualityPlayer() {
        binding.playerView.player?.clearMediaItems()
    }

    private fun setExoplayerVideo(url: String?) {
        if (url != null) {
            val videoItem = MediaItem.fromUri(url)
            val player = ExoPlayer.Builder(this).build().also { exoPlayer ->
                exoPlayer.setMediaItem(videoItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
            }
            binding.playerView.player = player
        }

    }

    private fun enterFullScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.playerView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        binding.playerView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        isFullScreen = true
    }

    private fun exitFullScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window,
            binding.playerView
        ).show(WindowInsetsCompat.Type.systemBars())

        binding.playerView.layoutParams.height = resources.getDimension(R.dimen.height_exo).toInt()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        isFullScreen = false
    }

    private fun selectDownloadQuality() {
        showQualitySelectionDialog { selectedUrl ->
            Intent(Intent.ACTION_VIEW, Uri.parse(selectedUrl)).also { intent ->
                startActivity(intent)
            }
        }
    }

    private fun selectVideoQuality() {
        showQualitySelectionDialog { selectedUrl ->
            changeQualityPlayer()
            setExoplayerVideo(selectedUrl)
        }
    }

    private fun showQualitySelectionDialog(onQualitySelected: (String) -> Unit) {
        val videoSizes = videoListAnime?.map { it?.size }?.toTypedArray() ?: arrayOf()
        val videoUrls = videoListAnime?.map { it?.url }?.toTypedArray() ?: arrayOf()

        MaterialAlertDialogBuilder(this)
            .setTitle("Select Quality")
            .setSingleChoiceItems(videoSizes, selectedVideoIndex) { _, which ->
                selectedVideoIndex = which
            }
            .setPositiveButton("Ok") { _, _ ->
                val selectedUrl =
                    videoUrls.getOrNull(selectedVideoIndex) ?: return@setPositiveButton
                onQualitySelected(selectedUrl)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setEpisodeList(data: List<EpisodeListItem?>?) {
        val mAdapter = EpisodeListAdapter().apply {
            submitList(data)
            setOnItemClickCallback(object :
                EpisodeListAdapter.OnItemClickCallback {
                override fun onItemClicked(data: EpisodeListItem) {
                    val animeCode = intent.getStringExtra(DetailAnimeActivity.ANIME_CODE).toString()
                    val animeId = intent.getStringExtra(DetailAnimeActivity.ANIME_ID).toString()
                    val intent =
                        Intent(this@EpisodeDetailActivity, EpisodeDetailActivity::class.java)
                    intent.putExtra(ANIME_CODE, animeCode)
                    intent.putExtra(ANIME_ID, animeId)
                    intent.putExtra(EPS, data.episodeNumber)
                    startActivity(intent)
                }
            })
        }
        binding.episodeRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@EpisodeDetailActivity)
            isNestedScrollingEnabled = false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
            playerView.visibility = if (isLoading) View.GONE else View.VISIBLE
            btnDownload.visibility = if (isLoading) View.GONE else View.VISIBLE
            animeTitle.visibility = if (isLoading) View.GONE else View.VISIBLE
            btnQuality.visibility = if (isLoading) View.GONE else View.VISIBLE
            btnShare.visibility = if (isLoading) View.GONE else View.VISIBLE
            titleEpisode.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }


    companion object {
        const val ANIME_CODE = "anime_code"
        const val ANIME_ID = "anime_id"
        const val EPS = "eps"
    }
}