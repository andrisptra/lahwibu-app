package com.example.lahwibu.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lahwibu.data.response.DataItemCompleted
import com.example.lahwibu.data.response.DataItemOngoing
import com.example.lahwibu.databinding.FragmentHomeBinding
import com.example.lahwibu.ui.detail.DetailAnimeActivity
import com.example.lahwibu.ui.detaillist.DetailListOngoingActivity
import com.example.lahwibu.utils.Result
import com.example.lahwibu.utils.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getOngoingAnime()
        getCompletedAnimeList()
        getOngoingAnime()

        binding.btnSeeAllOngoingAnime.setOnClickListener {
            val intent = Intent(requireActivity(), DetailListOngoingActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getOngoingAnime() {
        val order = "popular"
        viewModel.getOngoingAnime(order).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        setLoadingIndicatorOngoing(true)
                    }

                    is Result.Success -> {
                        setLoadingIndicatorOngoing(false)
                        val animeList = result.data.data
                        setAnimeOngoingList(animeList)

                    }

                    is Result.Error -> {
                        setLoadingIndicatorOngoing(false)
                        val errMessage = "gagal menghubungkan jaringan"
                        showToast(errMessage)

                    }
                }
            }
        }
    }

    private fun getCompletedAnimeList() {
        val page = "1"
        viewModel.getCompletedAnime(page).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        setLoadingIndicatorCompleted(true)
                    }

                    is Result.Success -> {
                        setLoadingIndicatorCompleted(false)
                        val animeList = result.data.data
                        setCompletedAnimeList(animeList)
                    }

                    is Result.Error -> {
                        setLoadingIndicatorCompleted(false)
                        val errMessage = "gagal menghubungkan jaringan"
                        showToast(errMessage)
                    }
                }
            }
        }
    }


    private fun setAnimeOngoingList(data: List<DataItemOngoing>) {
        val adapter = OngoingListAdapter()
        adapter.submitList(data)
        binding.rvAnimeListOngoing.adapter = adapter
        val layoutManager = GridLayoutManager(requireActivity(), 3)
        binding.rvAnimeListOngoing.layoutManager = layoutManager
        binding.rvAnimeListOngoing.isNestedScrollingEnabled = false
        adapter.setOnItemClickCallback(object : OngoingListAdapter.OnItemCLickCallback {
            override fun onItemClicked(data: DataItemOngoing) {
                data.title?.let { showToast(it) }
                val intent = Intent(requireActivity(), DetailAnimeActivity::class.java)
                intent.putExtra(DetailAnimeActivity.ANIME_CODE, data.animeCode)
                intent.putExtra(DetailAnimeActivity.ANIME_ID, data.animeId)
                startActivity(intent)
            }

        })
    }

    private fun setCompletedAnimeList(animeList: List<DataItemCompleted?>?) {
        val adapter = CompletedListAdapter()
        adapter.submitList(animeList)
        val layoutManager = GridLayoutManager(requireActivity(), 3)
        with(binding) {
            rvAnimeListCompleted.layoutManager = layoutManager
            rvAnimeListCompleted.adapter = adapter
            rvAnimeListCompleted.isNestedScrollingEnabled = false
        }
        adapter.setOnItemClickCallback(object : CompletedListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItemCompleted) {
                data.title?.let { showToast(it) }
                val intent = Intent(requireActivity(), DetailAnimeActivity::class.java)
                intent.putExtra(DetailAnimeActivity.ANIME_CODE, data.animeCode)
                intent.putExtra(DetailAnimeActivity.ANIME_ID, data.animeId)
                startActivity(intent)
            }
        })
    }

    private fun setLoadingIndicatorOngoing(Loading: Boolean) {
        with(binding) {
            loadingIndicatorOngoingAnime.visibility = if (Loading) View.VISIBLE else View.GONE
        }
    }

    private fun setLoadingIndicatorCompleted(Loading: Boolean) {
        with(binding) {
            loadingIndicatorCompletedAnime.visibility = if (Loading) View.VISIBLE else View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}