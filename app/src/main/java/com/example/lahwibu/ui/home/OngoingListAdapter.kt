package com.example.lahwibu.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.lahwibu.data.response.DataItemOngoing
import com.example.lahwibu.databinding.CardListAnimeBinding

class OngoingListAdapter :
    ListAdapter<DataItemOngoing, OngoingListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemCLickCallback
    fun setOnItemClickCallback(onItemClickCallback: OnItemCLickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CardListAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val animeList = getItem(position)
        holder.bind(animeList)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(animeList)}
    }

    class MyViewHolder(private val binding: CardListAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(anime: DataItemOngoing) {
            with(binding) {
                titleAnime.text = anime.title
                Glide.with(binding.root)
                    .load(anime.image)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
                    .into(coverImageAnime)

            }
        }
    }

    interface OnItemCLickCallback {
        fun onItemClicked(data: DataItemOngoing)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemOngoing>() {
            override fun areItemsTheSame(
                oldItem: DataItemOngoing,
                newItem: DataItemOngoing
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItemOngoing,
                newItem: DataItemOngoing
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}