package com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ccx.islamic.kids.littledeenapp.R
import android.content.Intent
import android.content.res.ColorStateList
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityChannelDetailBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.repo.VideoRepository
import com.ccx.islamic.kids.littledeenapp.ui.adapter.VideoAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject





@AndroidEntryPoint
class ChannelDetailActivity : BaseActivity<ActivityChannelDetailBinding>(
    ActivityChannelDetailBinding::inflate
) {

    @Inject
    lateinit var videoRepository: VideoRepository

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var videoAdapter: VideoAdapter
    private var channelTitle: String = ""
    private var channelThumbnail: String = ""

    override fun setupViews() {
        channelTitle = intent.getStringExtra(EXTRA_CHANNEL_TITLE) ?: "One 4 Kids (Zaky)"
        channelThumbnail = intent.getStringExtra(EXTRA_CHANNEL_THUMB) ?: ""

        applyThemeStyling()
        displayChannelInfo()
        setupRecyclerView()
        loadChannelVideos()
        setupClickListeners()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)
        binding.btnSaveChannel.backgroundTintList = ColorStateList.valueOf(theme.primaryColor)
    }

    private fun displayChannelInfo() {
        binding.tvChannelName.text = channelTitle

        // Use the video's actual thumbnail as the banner art background
        val bannerUrl = if (channelThumbnail.isNotBlank()) channelThumbnail else "https://img.youtube.com/vi/2MHYWw0D_bY/hqdefault.jpg"

        Glide.with(this)
            .load(bannerUrl)
            .centerCrop()
            .into(binding.ivChannelBanner)

        // Load avatar using the same thumbnail
        Glide.with(this)
            .load(bannerUrl)
            .circleCrop()
            .into(binding.ivChannelAvatar)
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoAdapter { video ->
            val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                putExtra(VideoPlayerActivity.EXTRA_VIDEO, video)
            }
            startActivity(intent)
        }

        binding.rvChannelVideos.apply {
            layoutManager = LinearLayoutManager(this@ChannelDetailActivity)
            adapter = videoAdapter
        }
    }

    private fun loadChannelVideos() {
        lifecycleScope.launch {
            videoRepository.searchVideos(channelTitle).collect { videos ->
                videoAdapter.submitList(videos)
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener {
            finish()
        }

        binding.btnSaveChannel.setOnSafeClickListener {
            showToast("Channel saved to your favorites! ♥")
        }

        binding.btnBlockChannel.setOnSafeClickListener {
            showToast("Channel blocked successfully.")
            finish()
        }
    }

    companion object {
        const val EXTRA_CHANNEL_TITLE = "extra_channel_title"
        const val EXTRA_CHANNEL_THUMB = "extra_channel_thumb"
    }
}


