package com.ccx.islamic.kids.littledeenapp.ui.fragment


import android.content.Intent
import android.content.res.ColorStateList
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.data.VideoCategoryChip
import com.ccx.islamic.kids.littledeenapp.extension.gone
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.visible
import com.ccx.islamic.kids.littledeenapp.model.KidVideo
import com.ccx.islamic.kids.littledeenapp.repo.VideoRepository
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.SearchActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.VideoPlayerActivity
import com.ccx.islamic.kids.littledeenapp.ui.adapter.CategoryChipAdapter
import com.ccx.islamic.kids.littledeenapp.ui.adapter.VideoAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.databinding.FragmentVideosBinding
import com.ccx.islamic.kids.littledeenapp.db.SavedVideoEntity
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.CategoriesActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.FavoritesActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.HistoryActivity
import com.ccx.islamic.kids.littledeenapp.ui.dialog.FullScreenLoadingDialogFragment
import com.ccx.islamic.kids.littledeenapp.ui.dialog.KidsLoadingDialogFragment


@AndroidEntryPoint
class VideosFragment :
    BaseFragment<FragmentVideosBinding>(
        FragmentVideosBinding::inflate) {

    @Inject
    lateinit var videoRepository: VideoRepository

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var videoAdapter: VideoAdapter

    private enum class VideoTab { ALL, HISTORY, FAVORITES }
    private var currentTab = VideoTab.ALL

    override fun setupViews() {
        applyThemeStyling()
        setupVideosRecyclerView()
        setupSwipeRefresh()
        setupHubCardListeners()

        // Select "All" tab by default
        selectTab(VideoTab.ALL)
    }

    override fun onResume() {
        super.onResume()
        applyThemeStyling()
        // Reload current tab content to reflect newly watched or liked videos
        loadTabContent(isSilent = true)
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.swipeRefreshVideos.setColorSchemeColors(theme.primaryColor)
        updateHubTabsUI(currentTab)
    }

    private fun setupVideosRecyclerView() {
        videoAdapter = VideoAdapter { clickedVideo ->
            openVideoPlayer(clickedVideo)
        }

        binding.rvVideos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = videoAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshVideos.setOnRefreshListener {
            loadTabContent(isSilent = true)
        }
    }

    private fun setupHubCardListeners() {
        // Tab 1: All
        binding.cardTabAll.setOnSafeClickListener {
            if (currentTab != VideoTab.ALL) selectTab(VideoTab.ALL)
        }

        // Tab 2: History
        binding.cardTabHistory.setOnSafeClickListener {
            startActivity(Intent(requireContext(), HistoryActivity::class.java))
        }

        // Tab 3: Favorites
        binding.cardTabFavorites.setOnSafeClickListener {
            startActivity(Intent(requireContext(), FavoritesActivity::class.java))
        }

        // Tab 4: Categories
        binding.cardTabCategories.setOnSafeClickListener {
            openCategoriesScreen()
        }
    }

    private fun selectTab(tab: VideoTab) {
        currentTab = tab
        updateHubTabsUI(tab)
        loadTabContent(isSilent = false)
    }

    private fun updateHubTabsUI(activeTab: VideoTab) {
        val theme = themeManager.getSelectedTheme()
        val surfaceColor = ContextCompat.getColor(requireContext(), R.color.color_surface)
        val borderColor = ContextCompat.getColor(requireContext(), R.color.color_border)
        val textPrimary = ContextCompat.getColor(requireContext(), R.color.color_text_primary)

        val cards = listOf(binding.cardTabAll, binding.cardTabHistory, binding.cardTabFavorites, binding.cardTabCategories)
        val labels = listOf(binding.tvTabAllLabel, binding.tvTabHistoryLabel, binding.tvTabFavLabel, binding.tvTabCatLabel)

        // Reset all 4 cards
        cards.forEach {
            it.setCardBackgroundColor(surfaceColor)
            it.strokeColor = borderColor
            it.strokeWidth = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._1sdp)
        }
        labels.forEach { it.setTextColor(textPrimary) }

        // Highlight selected card
        when (activeTab) {
            VideoTab.ALL -> {
                binding.cardTabAll.setCardBackgroundColor(theme.primaryColor)
                binding.cardTabAll.strokeColor = theme.primaryColor
                binding.tvTabAllLabel.setTextColor(Color.WHITE)
                binding.tvSectionTitle.text = "All Videos Feed"
            }
            VideoTab.HISTORY -> {
                binding.cardTabHistory.setCardBackgroundColor(theme.primaryColor)
                binding.cardTabHistory.strokeColor = theme.primaryColor
                binding.tvTabHistoryLabel.setTextColor(Color.WHITE)
                binding.tvSectionTitle.text = "Watch History"
            }
            VideoTab.FAVORITES -> {
                binding.cardTabFavorites.setCardBackgroundColor(theme.primaryColor)
                binding.cardTabFavorites.strokeColor = theme.primaryColor
                binding.tvTabFavLabel.setTextColor(Color.WHITE)
                binding.tvSectionTitle.text = "Saved Favorites"
            }
        }
    }

    private fun loadTabContent(isSilent: Boolean) {
        lifecycleScope.launch {
            if (!isSilent) {
                binding.shimmerVideos.visible()
                binding.shimmerVideos.startShimmer()
                binding.rvVideos.gone()
                binding.layoutEmptyState.gone()
                delay(200)
            }

            when (currentTab) {
                VideoTab.ALL -> loadAllVideos()
                VideoTab.HISTORY -> loadWatchHistory()
                VideoTab.FAVORITES -> loadFavorites()
            }
        }
    }

    private fun loadAllVideos() {
        lifecycleScope.launch {
            // 🎯 Show the gorgeous kids loading dialog when fetching data
            val loadingDialog = FullScreenLoadingDialogFragment.newInstance()

            // Only show if the dialog isn't already showing
            if (isAdded && parentFragmentManager.findFragmentByTag(KidsLoadingDialogFragment.TAG) == null) {
                loadingDialog.show(parentFragmentManager, KidsLoadingDialogFragment.TAG)
            }

            videoRepository.searchVideos("").collect { allVideos ->
                // 🎯 Dismiss the loading dialog once data arrives
                if (loadingDialog.isAdded) {
                    loadingDialog.dismiss()
                }

                renderVideoList(
                    videos = allVideos,
                    emptyIcon = "📺",
                    emptyTitle = "No videos available",
                    emptySubtitle = "Check your connection and pull down to refresh!"
                )
            }
        }
    }

    private fun loadWatchHistory() {
        lifecycleScope.launch {
            videoRepository.getWatchHistory().collect { historyEntities ->
                val videos = historyEntities.map { it.toKidVideo() }
                renderVideoList(
                    videos = videos,
                    emptyIcon = "🕒",
                    emptyTitle = "No watch history yet",
                    emptySubtitle = "Videos your child watches will appear here!"
                )
            }
        }
    }

    private fun loadFavorites() {
        lifecycleScope.launch {
            videoRepository.getFavoriteVideos().collect { favEntities ->
                val videos = favEntities.map { it.toKidVideo() }
                renderVideoList(
                    videos = videos,
                    emptyIcon = "❤️",
                    emptyTitle = "No favorite videos yet",
                    emptySubtitle = "Tap the heart on any video to save it here!"
                )
            }
        }
    }

    private fun renderVideoList(
        videos: List<KidVideo>,
        emptyIcon: String,
        emptyTitle: String,
        emptySubtitle: String
    ) {
        binding.shimmerVideos.stopShimmer()
        binding.shimmerVideos.gone()
        binding.swipeRefreshVideos.isRefreshing = false

        if (videos.isNotEmpty()) {
            binding.rvVideos.visible()
            binding.layoutEmptyState.gone()
            videoAdapter.submitList(videos)
        } else {
            binding.rvVideos.gone()
            binding.layoutEmptyState.visible()
            binding.tvEmptyIcon.text = emptyIcon
            binding.tvEmptyTitle.text = emptyTitle
            binding.tvEmptySubtitle.text = emptySubtitle
        }
    }

    private fun openVideoPlayer(video: KidVideo) {
        val intent = Intent(requireContext(), VideoPlayerActivity::class.java).apply {
            putExtra(VideoPlayerActivity.EXTRA_VIDEO, video)
        }
        startActivity(intent)
    }

    private fun openCategoriesScreen() {
        val intent = Intent(requireContext(), CategoriesActivity::class.java)
        startActivity(intent)
    }

    private fun SavedVideoEntity.toKidVideo(): KidVideo {
        return KidVideo(
            videoId = videoId,
            title = title,
            channelTitle = channelTitle,
            thumbnailUrl = thumbnailUrl,
            badgeLabel = durationText.ifBlank { "Saved" },
            categoryId = categoryId
        )
    }
}







