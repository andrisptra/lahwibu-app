package com.example.lahwibu.ui.episode

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.lahwibu.databinding.ActivityEpisodeDetailBinding
import com.example.lahwibu.utils.Result
import com.example.lahwibu.utils.ViewModelFactory

class EpisodeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEpisodeDetailBinding
    private val viewModel: EpisodeDetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var exoPlayer: ExoPlayer? = null
//    private var playWhenReady = true
//    private var currentWindow = 0
//    private var playbackPosition = 0L


    //    private val animeCode = intent.getStringExtra(ANIME_CODE).toString()
//    private val animeId = intent.getStringExtra(ANIME_ID).toString()
//    private val epsNumber = intent.getStringExtra(EPS).toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEpisodeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getEpisodeDetail()
        hideSystemUI()
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


    private fun getEpisodeDetail() {
        val animeCode = intent.getStringExtra(ANIME_CODE).toString()
        val animeId = intent.getStringExtra(ANIME_ID).toString()
        val epsNumber = intent.getStringExtra(EPS).toString()
        viewModel.getDetailEpisode(animeCode, animeId, epsNumber).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {

                    }

                    is Result.Success -> {
                        val animeDetail = result.data
                        val url = animeDetail.videoList?.first()?.url.toString()
                        val videoItem = MediaItem.fromUri(url)
                        val player = ExoPlayer.Builder(this).build().also { exoPlayer ->
                            exoPlayer.setMediaItem(videoItem)
                            exoPlayer.prepare()
                        }
                        binding.playerView.player = player
                    }

                    is Result.Error -> {

                    }
                }
            }
        }
    }


    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.playerView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }


    companion object {
        const val ANIME_CODE = "anime_code"
        const val ANIME_ID = "anime_id"
        const val EPS = "eps"
    }
}