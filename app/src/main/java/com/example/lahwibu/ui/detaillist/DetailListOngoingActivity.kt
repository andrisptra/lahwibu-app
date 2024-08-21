package com.example.lahwibu.ui.detaillist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lahwibu.data.response.DataItemOngoing
import com.example.lahwibu.databinding.ActivityDetailListOngoingBinding
import com.example.lahwibu.ui.detail.DetailAnimeActivity
import com.example.lahwibu.utils.ViewModelFactory

class DetailListOngoingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailListOngoingBinding
    private val viewModel: DetailListOngoingViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailListOngoingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getList()

    }

    private fun getList() {
        val adapter = DetailOngoingListAdapter()
        binding.animeList.adapter = adapter
        val layoutManager = GridLayoutManager(this, 3)
        binding.animeList.layoutManager = layoutManager
        viewModel.getAllOngoingList("popular").observe(this) {
            adapter.submitData(lifecycle, it)
            adapter.setOnItemClickCallback(object : DetailOngoingListAdapter.OnItemCLickCallback{
                override fun onItemClicked(data: DataItemOngoing) {
                    val intent = Intent(this@DetailListOngoingActivity, DetailAnimeActivity::class.java)
                    intent.putExtra(DetailAnimeActivity.ANIME_CODE, data.animeCode)
                    intent.putExtra(DetailAnimeActivity.ANIME_ID, data.animeId)
                    startActivity(intent)
                }

            })
        }
    }
}