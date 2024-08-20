package com.example.lahwibu.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lahwibu.data.response.EpisodeListItem
import com.example.lahwibu.databinding.EpisodeListBinding

class EpisodeListAdapter : ListAdapter<EpisodeListItem, EpisodeListAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = EpisodeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val episodeList = getItem(position)
        holder.bind(episodeList)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(episodeList) }
    }

    class MyViewHolder(private val binding: EpisodeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(anime: EpisodeListItem) {
            with(binding) {
                titleEpisode.text = anime.title
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: EpisodeListItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EpisodeListItem>() {
            override fun areItemsTheSame(
                oldItem: EpisodeListItem,
                newItem: EpisodeListItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: EpisodeListItem,
                newItem: EpisodeListItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}