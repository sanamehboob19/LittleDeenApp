package com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ccx.islamic.kids.littledeenapp.R
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityFavoritesBinding
import com.ccx.islamic.kids.littledeenapp.extension.gone
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.visible
import com.ccx.islamic.kids.littledeenapp.model.KidVideo
import com.ccx.islamic.kids.littledeenapp.repo.VideoRepository
import com.ccx.islamic.kids.littledeenapp.ui.adapter.HistoryAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesActivity : BaseActivity<ActivityFavoritesBinding>(ActivityFavoritesBinding::inflate) {

    @Inject
    lateinit var videoRepository: VideoRepository

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var favoritesAdapter: HistoryAdapter

    override fun setupViews() {
        applyThemeStyling()
        setupRecyclerView()
        setupClickListeners()
        loadFavoritesData()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)
    }

    private fun setupRecyclerView() {
        favoritesAdapter = HistoryAdapter { favItem ->
            val video = KidVideo(
                videoId = favItem.videoId,
                title = favItem.title,
                channelTitle = favItem.channelTitle,
                thumbnailUrl = favItem.thumbnailUrl,
                badgeLabel = favItem.durationText,
                categoryId = favItem.categoryId
            )
            val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                putExtra(VideoPlayerActivity.EXTRA_VIDEO, video)
            }
            startActivity(intent)
        }

        binding.rvFavoritesVideos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapter
        }
    }

    private fun loadFavoritesData() {
        lifecycleScope.launch {
            videoRepository.getFavoriteVideos().collect { favorites ->
                if (favorites.isNotEmpty()) {
                    binding.rvFavoritesVideos.visible()
                    binding.layoutEmptyFavorites.gone()
                    favoritesAdapter.submitList(favorites)
                } else {
                    binding.rvFavoritesVideos.gone()
                    binding.layoutEmptyFavorites.visible()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener {
            finish()
        }
    }
}