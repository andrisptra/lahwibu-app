package com.example.lahwibu.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lahwibu.data.response.DataItemSearch
import com.example.lahwibu.databinding.FragmentSearchBinding
import com.example.lahwibu.ui.detail.DetailAnimeActivity
import com.example.lahwibu.utils.Result
import com.example.lahwibu.utils.ViewModelFactory

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { v, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    searchingAnime(searchBar.text.toString())
                    false
                }
        }

    }

    private fun searchingAnime(query: String) {
        viewModel.searchAnime(query).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {

                    }

                    is Result.Success -> {
                        val animeList = result.data.data
                        setAnimeSearchList(animeList)

                    }

                    is Result.Error -> {

                    }
                }
            }
        }
    }

    private fun setAnimeSearchList(anime: List<DataItemSearch?>?) {
        val adapter = SearchListAdapter()
        adapter.submitList(anime)
        val layoutManager = GridLayoutManager(requireActivity(), 3)
        with(binding) {
            rvAnimeSearchList.layoutManager = layoutManager
            rvAnimeSearchList.adapter = adapter
        }
        adapter.setOnItemClickCallback(object : SearchListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItemSearch) {
                data.animeCode?.let { showToast(it) }
                val intent = Intent(requireActivity(), DetailAnimeActivity::class.java)
                intent.putExtra(DetailAnimeActivity.ANIME_CODE, data.animeCode)
                intent.putExtra(DetailAnimeActivity.ANIME_ID, data.animeId)
                startActivity(intent)
            }

        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}