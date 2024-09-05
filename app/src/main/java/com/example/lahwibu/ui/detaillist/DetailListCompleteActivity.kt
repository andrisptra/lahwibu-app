package com.example.lahwibu.ui.detaillist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lahwibu.R
import com.example.lahwibu.adapter.DetailListCompleteAdapter
import com.example.lahwibu.adapter.LoadingStateAdapter
import com.example.lahwibu.data.response.DataItemCompleted
import com.example.lahwibu.databinding.ActivityDetailListBinding
import com.example.lahwibu.ui.detail.DetailAnimeActivity
import com.example.lahwibu.utils.ViewModelFactory
import com.example.lahwibu.viewmodel.DetailListCompleteViewModel

class DetailListCompleteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailListBinding

    private val viewModel: DetailListCompleteViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBackIconAction()
        getList("popular")
        getMenuAction()


    }
    private fun getBackIconAction(){
        binding.appBarLayout.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun getMenuAction(){
        binding.appBarLayout.setOnMenuItemClickListener { menuItem ->
            val ascending = getString(R.string.ascending)
            val descending = getString(R.string.descending)
            val updated = getString(R.string.updated)
            val mostViewed = "most_viewed"
            val popular = getString(R.string.popular)
            val latest = getString(R.string.latest)
            val oldest = getString(R.string.oldest)
            when(menuItem.itemId){
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
                R.id.popular-> {
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

    private fun getList(order:String) {
        val adapter = DetailListCompleteAdapter()
        binding.animeList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        setLayoutManager(adapter)
        viewModel.getAllCompleteList(order).observe(this) {
            adapter.submitData(lifecycle, it)
            adapter.setOnItemClickCallback(object : DetailListCompleteAdapter.OnItemCLickCallback {
                override fun onItemClicked(data: DataItemCompleted) {
                    val intent =
                        Intent(this@DetailListCompleteActivity, DetailAnimeActivity::class.java)
                    intent.putExtra(DetailAnimeActivity.ANIME_CODE, data.animeCode)
                    intent.putExtra(DetailAnimeActivity.ANIME_ID, data.animeId)
                    startActivity(intent)
                }

            })
        }
    }

    private fun setLayoutManager(adapter: DetailListCompleteAdapter) {
        val layoutManager = GridLayoutManager(this@DetailListCompleteActivity, 3)
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