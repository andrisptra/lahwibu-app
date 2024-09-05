package com.example.lahwibu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.lahwibu.data.response.DataItemSearch
import com.example.lahwibu.databinding.CardListAnimeBinding

class SearchListAdapter: ListAdapter<DataItemSearch, SearchListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val binding = CardListAnimeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val animeList = getItem(position)
        holder.bind(animeList)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(animeList) }
    }

    class MyViewHolder(private val binding: CardListAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(anime: DataItemSearch) {
            with(binding) {
                titleAnime.text = anime.title
                Glide.with(binding.root)
                    .load(anime.image)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
                    .into(coverImageAnime)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItemSearch)
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemSearch>() {
            override fun areItemsTheSame(
                oldItem: DataItemSearch,
                newItem: DataItemSearch
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItemSearch,
                newItem: DataItemSearch
            ): Boolean {
                return oldItem == newItem
            }
        }
    }



}

