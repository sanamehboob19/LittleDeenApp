package com.ccx.islamic.kids.littledeenapp.ui.fragment


import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.extension.gone
import com.ccx.islamic.kids.littledeenapp.extension.visible
import com.ccx.islamic.kids.littledeenapp.repo.VideoRepository
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.VideoPlayerActivity
import com.ccx.islamic.kids.littledeenapp.ui.adapter.VideoAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.ccx.islamic.kids.littledeenapp.databinding.FragmentCategoryVideosBinding



@AndroidEntryPoint
class CategoryVideosFragment :
    BaseFragment<FragmentCategoryVideosBinding>(
        FragmentCategoryVideosBinding::inflate) {

    @Inject
    lateinit var videoRepository: VideoRepository

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var videoAdapter: VideoAdapter
    private var categoryId: String = "cat_islamic"

    override fun setupViews() {
        categoryId = arguments?.getString(ARG_CATEGORY_ID) ?: "cat_islamic"

        applyTheme()
        setupRecyclerView()
        setupSwipeRefresh()
        loadCategoryVideos(isRefresh = false)
    }

    private fun applyTheme() {
        val theme = themeManager.getSelectedTheme()
        binding.swipeRefreshCategory.setColorSchemeColors(theme.primaryColor)
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoAdapter { video ->
            val intent = Intent(requireContext(), VideoPlayerActivity::class.java).apply {
                putExtra(VideoPlayerActivity.EXTRA_VIDEO, video)
            }
            startActivity(intent)
        }

        binding.rvCategoryVideos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = videoAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshCategory.setOnRefreshListener {
            loadCategoryVideos(isRefresh = true)
        }
    }

    private fun loadCategoryVideos(isRefresh: Boolean) {
        lifecycleScope.launch {
            if (!isRefresh) {
                binding.shimmerCategoryVideos.visible()
                binding.shimmerCategoryVideos.startShimmer()
                binding.rvCategoryVideos.gone()
            }

            videoRepository.getVideosForCategory(categoryId).collect { videos ->
                binding.shimmerCategoryVideos.stopShimmer()
                binding.shimmerCategoryVideos.gone()
                binding.swipeRefreshCategory.isRefreshing = false

                if (videos.isNotEmpty()) {
                    binding.rvCategoryVideos.visible()
                    binding.layoutCategoryEmpty.gone()
                    // Submitting shuffled list ensures random order on each refresh
                    videoAdapter.submitList(videos.shuffled())
                } else {
                    binding.rvCategoryVideos.gone()
                    binding.layoutCategoryEmpty.visible()
                }
            }
        }
    }

    companion object {
        private const val ARG_CATEGORY_ID = "arg_category_id"

        fun newInstance(catId: String): CategoryVideosFragment {
            return CategoryVideosFragment().apply {
                arguments = android.os.Bundle().apply {
                    putString(ARG_CATEGORY_ID, catId)
                }
            }
        }
    }
}