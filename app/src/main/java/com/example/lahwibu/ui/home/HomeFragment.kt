package com.example.lahwibu.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lahwibu.R
import com.example.lahwibu.adapter.CarouselAdapter
import com.example.lahwibu.adapter.CompletedListAdapter
import com.example.lahwibu.adapter.OngoingListAdapter
import com.example.lahwibu.data.response.DataItemCompleted
import com.example.lahwibu.data.response.DataItemOngoing
import com.example.lahwibu.databinding.FragmentHomeBinding
import com.example.lahwibu.ui.detail.DetailAnimeActivity
import com.example.lahwibu.ui.detaillist.DetailListCompleteActivity
import com.example.lahwibu.ui.detaillist.DetailListOngoingActivity
import com.example.lahwibu.utils.Result
import com.example.lahwibu.utils.ViewModelFactory
import com.example.lahwibu.viewmodel.HomeViewModel
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.HeroCarouselStrategy

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
        getCompletedAnimeList()
        getOngoingAnime()
        buttonIntentToDetailAnime()
        setIconTopAppBarAction()
    }

    private fun buttonIntentToDetailAnime() {
        binding.btnSeeAllOngoingAnime.setOnClickListener {
            val intent = Intent(requireActivity(), DetailListOngoingActivity::class.java)
            startActivity(intent)
        }
        binding.btnSeeAllCompletedAnime.setOnClickListener {
            val intent = Intent(requireActivity(), DetailListCompleteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setIconTopAppBarAction() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    view?.findNavController()
                        ?.navigate(R.id.action_navigation_home_to_navigation_search)
                    true

                }

                else -> {
                    false
                }
            }
        }
    }

    private fun getOngoingAnime() {
        val order = "popular"
        viewModel.getOngoingAnime(order).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        setLoadingIndicator(true)
                    }

                    is Result.Success -> {
                        setLoadingIndicator(false)
                        val animeList = result.data.data
                        setAnimeOngoingList(animeList)
                    }

                    is Result.Error -> {
                        setLoadingIndicator(false)
                        val errMessage = "gagal menghubungkan jaringan"
                        showToast(errMessage)

                    }
                }
            }
        }
    }

    private fun getCompletedAnimeList() {
        val order = "popular"
        viewModel.getCompletedAnime(order).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        setLoadingIndicator(true)
                    }

                    is Result.Success -> {
                        setLoadingIndicator(false)
                        val animeList = result.data.data
                        setCompletedAnimeList(animeList)
                        setCarouselItem(animeList)
                    }

                    is Result.Error -> {
                        setLoadingIndicator(false)
                        val errMessage = "gagal menghubungkan jaringan"
                        showToast(errMessage)
                    }
                }
            }
        }
    }


    private fun setAnimeOngoingList(data: List<DataItemOngoing>) {
        val adapter = OngoingListAdapter()
        val layoutManager = GridLayoutManager(requireActivity(), 3)
        adapter.submitList(data)
        with(binding) {
            rvAnimeListOngoing.adapter = adapter
            rvAnimeListOngoing.layoutManager = layoutManager
            rvAnimeListOngoing.isNestedScrollingEnabled = false
        }
        adapter.setOnItemClickCallback(object : OngoingListAdapter.OnItemCLickCallback {
            override fun onItemClicked(data: DataItemOngoing) {
                intentToDetail(data)
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
                intentToDetail(data)
            }
        })
    }

    private fun setCarouselItem(animeList: List<DataItemCompleted>) {
        val adapter = CarouselAdapter()
        val animeTop = animeList.take(4)
        adapter.submitList(animeTop)
        val layoutManager = CarouselLayoutManager(HeroCarouselStrategy())
        with(binding) {
            carouselRecyclerView.layoutManager = layoutManager
            carouselRecyclerView.adapter = adapter
        }
        adapter.setOnItemClickCallback(object : CarouselAdapter.OnItemCLickCallback {
            override fun onItemClicked(data: DataItemCompleted) {
                intentToDetail(data)
            }
        })
    }

    private fun intentToDetail(data: DataItem) {
        data.title?.let { showToast(it) }
        val intent = Intent(requireActivity(), DetailAnimeActivity::class.java)
        intent.putExtra(DetailAnimeActivity.ANIME_CODE, data.animeCode)
        intent.putExtra(DetailAnimeActivity.ANIME_ID, data.animeId)
        startActivity(intent)
    }

    private fun setLoadingIndicator(loading: Boolean) {
        with(binding) {
            if (loading) {
                loadingIndicator.visibility = View.VISIBLE
                titleCompletedAnime.visibility = View.GONE
                titleOngoingAnime.visibility = View.GONE
                btnSeeAllOngoingAnime.visibility = View.GONE
                btnSeeAllCompletedAnime.visibility = View.GONE
            } else {
                loadingIndicator.visibility = View.GONE
                titleCompletedAnime.visibility = View.VISIBLE
                titleOngoingAnime.visibility = View.VISIBLE
                btnSeeAllOngoingAnime.visibility = View.VISIBLE
                btnSeeAllCompletedAnime.visibility = View.VISIBLE
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    interface DataItem {
        val title: String?
        val animeCode: String?
        val animeId: String?
        val image: String?
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}