package com.example.lahwibu.ui.detaillist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lahwibu.R
import com.example.lahwibu.adapter.DetailOngoingListAdapter
import com.example.lahwibu.adapter.LoadingStateAdapter
import com.example.lahwibu.data.response.DataItemOngoing
import com.example.lahwibu.databinding.ActivityDetailListBinding
import com.example.lahwibu.ui.detail.DetailAnimeActivity
import com.example.lahwibu.utils.ViewModelFactory
import com.example.lahwibu.viewmodel.DetailListOngoingViewModel

class DetailListOngoingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailListBinding
    private val viewModel: DetailListOngoingViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getList("popular")
        getBackIconAction()
        getMenuAction()

    }

    private fun getBackIconAction() {
        binding.appBarLayout.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun getMenuAction() {
        binding.appBarLayout.setOnMenuItemClickListener { menuItem ->
            val ascending = getString(R.string.ascending)
            val descending = getString(R.string.descending)
            val updated = getString(R.string.updated)
            val mostViewed = "most_viewed"
            val popular = getString(R.string.popular)
            val latest = getString(R.string.latest)
            val oldest = getString(R.string.oldest)
            when (menuItem.itemId) {
                R.id.ascending -> {
                    getList(ascending)
                    true
                }

                R.id.descending -> {
                    getList(descending)
                    true
                }

                R.id.updated -> {
                    getList(updated)
                    true
                }

                R.id.most_viewed -> {
                    getList(mostViewed)
                    true
                }

                R.id.popular -> {
                    getList(popular)
                    true
                }

                R.id.latest -> {
                    getList(latest)
                    true
                }

                R.id.oldest -> {
                    getList(oldest)
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun getList(order: String) {
        val adapter = DetailOngoingListAdapter()
        binding.animeList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        setLayoutManager(adapter)
        viewModel.getAllOngoingList(order).observe(this) {
            adapter.submitData(lifecycle, it)
            adapter.setOnItemClickCallback(object : DetailOngoingListAdapter.OnItemCLickCallback {
                override fun onItemClicked(data: DataItemOngoing) {
                    val intent =
                        Intent(this@DetailListOngoingActivity, DetailAnimeActivity::class.java)
                    intent.putExtra(DetailAnimeActivity.ANIME_CODE, data.animeCode)
                    intent.putExtra(DetailAnimeActivity.ANIME_ID, data.animeId)
                    startActivity(intent)
                }

            })
        }
    }

    private fun setLayoutManager(adapter: DetailOngoingListAdapter) {
        val layoutManager = GridLayoutManager(this@DetailListOngoingActivity, 3)
        binding.animeList.layoutManager = layoutManager
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount) {
                    3
                } else
                    1
            }
        }
    }
}