package com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ccx.islamic.kids.littledeenapp.R

import android.content.Intent
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivitySearchBinding
import com.ccx.islamic.kids.littledeenapp.extension.gone
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.visible
import com.ccx.islamic.kids.littledeenapp.model.KidVideo
import com.ccx.islamic.kids.littledeenapp.repo.VideoRepository
import com.ccx.islamic.kids.littledeenapp.ui.adapter.VideoAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding>(ActivitySearchBinding::inflate) {

    @Inject
    lateinit var videoRepository: VideoRepository

    private lateinit var searchAdapter: VideoAdapter
    private var searchJob: Job? = null

    override fun setupViews() {
        setupResultsRecyclerView()
        setupSearchInput()
        setupRecommendedChips()
        setupClickListeners()
    }

    private fun setupResultsRecyclerView() {
        searchAdapter = VideoAdapter { selectedVideo ->
            openVideoPlayer(selectedVideo)
        }

        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }
    }

    private fun setupSearchInput() {
        binding.etSearch.doAfterTextChanged { text ->
            val query = text?.toString()?.trim().orEmpty()

            if (query.isNotEmpty()) {
                binding.ivClearSearch.visible()
                performSearch(query)
            } else {
                binding.ivClearSearch.gone()
                showRecommendedTags()
            }
        }
    }

    private fun performSearch(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(150) // Small debounce for smooth typing

            videoRepository.searchVideos(query).collect { results ->
                binding.scrollRecommended.gone()

                if (results.isNotEmpty()) {
                    binding.rvSearchResults.visible()
                    binding.layoutEmptyResults.gone()
                    searchAdapter.submitList(results)
                } else {
                    binding.rvSearchResults.gone()
                    binding.layoutEmptyResults.visible()
                }
            }
        }
    }

    private fun showRecommendedTags() {
        binding.scrollRecommended.visible()
        binding.rvSearchResults.gone()
        binding.layoutEmptyResults.gone()
    }

    private fun setupRecommendedChips() {
        val chipTags = listOf(
            binding.chipRamadan to "Ramadan",
            binding.chipProphets to "Prophets",
            binding.chipAnimals to "Animals",
            binding.chipManners to "Manners",
            binding.chipAlphabet to "Alphabet",
            binding.chipNasheeds to "Nasheed",
            binding.chipBirds to "Birds"
        )

        chipTags.forEach { (chip, queryKeyword) ->
            chip.setOnClickListener {
                binding.etSearch.setText(queryKeyword)
                binding.etSearch.setSelection(queryKeyword.length)
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener {
            finish()
        }

        binding.ivClearSearch.setOnSafeClickListener {
            binding.etSearch.text?.clear()
        }
    }

    private fun openVideoPlayer(video: KidVideo) {
        val intent = Intent(this, VideoPlayerActivity::class.java).apply {
            putExtra(VideoPlayerActivity.EXTRA_VIDEO, video)
        }
        startActivity(intent)
    }
}






