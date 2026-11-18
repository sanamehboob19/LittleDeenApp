package com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer


import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityVideoPlayerBinding
import com.ccx.islamic.kids.littledeenapp.extension.gone
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.extension.visible
import com.ccx.islamic.kids.littledeenapp.model.KidVideo
import com.ccx.islamic.kids.littledeenapp.repo.VideoRepository
import com.ccx.islamic.kids.littledeenapp.ui.adapter.VideoAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.ui.dialog.AddPlaylistDialogFragment
import com.ccx.islamic.kids.littledeenapp.util.MusicManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VideoPlayerActivity : BaseActivity<ActivityVideoPlayerBinding>(
    ActivityVideoPlayerBinding::inflate
)
{

    @Inject
    lateinit var videoRepository: VideoRepository

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var musicManager: MusicManager

    private var activeVideo: KidVideo? = null
    private var activeYouTubePlayer: YouTubePlayer? = null
    private lateinit var suggestedAdapter: VideoAdapter
    private var isFavoriteState = false
    private var isFullscreen = false
    private var exitFullscreenCallback: (() -> Unit)? = null
    private var currentSuggestedList: List<KidVideo> = emptyList()

    override fun setupViews() {

        // 2. Parse passed video data
        activeVideo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_VIDEO, KidVideo::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_VIDEO)
        }

        val video = activeVideo ?: run {
            showToast("Video not found")
            finish()
            return
        }

        applyThemeStyling()
        displayVideoInfo(video)
        initYouTubePlayer(video.videoId)
        setupSuggestedVideos(video)
        observeFavoriteState(video.videoId)
        setupClickListeners(video)
        setupBackHandling()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)
        binding.btnViewChannel.backgroundTintList = ColorStateList.valueOf(theme.lightColor)
        binding.btnViewChannel.setTextColor(theme.primaryColor)
        binding.btnViewChannel.strokeColor = ColorStateList.valueOf(theme.primaryColor)
    }

    private fun displayVideoInfo(video: KidVideo) {
        binding.tvDetailTitle.text = video.title
        binding.tvDetailChannel.text = video.channelTitle
        binding.tvCategoryLabel.text = "Category: ${video.badgeLabel}"
    }



    private fun initYouTubePlayer(videoId: String) {
        lifecycle.addObserver(binding.youtubePlayerView)

        binding.youtubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true
                exitFullscreenCallback = exitFullscreen
                binding.fullscreenViewContainer.visible()
                binding.fullscreenViewContainer.addView(fullscreenView)
                binding.headerBar.gone()
                binding.playerContainer.gone()
                binding.scrollContainer.gone()
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                // 🎯 4. Hide System Bars (True Fullscreen Immersive Mode)
                WindowCompat.setDecorFitsSystemWindows(window, false)
                WindowInsetsControllerCompat(window, binding.root).let { controller ->
                    controller.hide(WindowInsetsCompat.Type.systemBars())
                    controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }

            override fun onExitFullscreen() {
                isFullscreen = false
                exitFullscreenCallback = null
                binding.fullscreenViewContainer.removeAllViews()
                binding.fullscreenViewContainer.gone()
                binding.headerBar.visible()
                binding.playerContainer.visible()
                binding.scrollContainer.visible()
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                // 🎯 4. Restore System Bars
                WindowCompat.setDecorFitsSystemWindows(window, true)
                WindowInsetsControllerCompat(window, binding.root).show(WindowInsetsCompat.Type.systemBars())
            }
        })

        val iFrameOptions = IFramePlayerOptions.Builder(this)
            .controls(1)
            .fullscreen(1)
            .rel(0) // Minimizes related videos
            .build()

        binding.youtubePlayerView.initialize(
            youTubePlayerListener = object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    activeYouTubePlayer = youTubePlayer
                    youTubePlayer.loadVideo(videoId, 0f)
                }

                // 🎯 BYPASS YOUTUBE'S END SCREEN GRID
                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) {
                    super.onStateChange(youTubePlayer, state)
                    // When video ends, immediately play the next safe video from OUR app!
                    if (state == PlayerConstants.PlayerState.ENDED) {
                        if (currentSuggestedList.isNotEmpty()) {
                            switchPlayingVideo(currentSuggestedList.first())
                        }
                    }
                }
            },
            handleNetworkEvents = true,
            playerOptions = iFrameOptions
        )
    }

    // ==========================================
    // 🛡️ 100% ESCAPE-PROOF INTENT INTERCEPTOR
    // ==========================================
    override fun startActivity(intent: Intent?) {
        // If the YouTube player tries to open the YouTube App or a web browser...
        if (intent?.action == Intent.ACTION_VIEW) {
            val url = intent.dataString ?: ""
            if (url.contains("youtube") || url.contains("youtu.be") || url.startsWith("http")) {
                // 🛑 Block the action completely!
                showToast("Leaving the app is disabled for your safety! 🛡️")
                return
            }
        }
        // Otherwise, allow normal app navigation (like opening Playlists)
        super.startActivity(intent)
    }


    private fun setupSuggestedVideos(video: KidVideo) {
        suggestedAdapter = VideoAdapter { selectedVideo ->
            switchPlayingVideo(selectedVideo)
        }

        binding.rvSuggestedVideos.apply {
            layoutManager = LinearLayoutManager(this@VideoPlayerActivity)
            adapter = suggestedAdapter
        }

        lifecycleScope.launch {
            // 🎯 Pass both videoId AND categoryId to get relevant suggestions
            videoRepository.getSuggestedVideos(video.videoId, video.categoryId).collect { suggested ->
                suggestedAdapter.submitList(suggested)
            }
        }
    }

    private fun switchPlayingVideo(newVideo: KidVideo) {
        this.activeVideo = newVideo
        displayVideoInfo(newVideo)
        observeFavoriteState(newVideo.videoId)

        // Save to watch history
        lifecycleScope.launch {
            videoRepository.recordWatchHistory(newVideo)
        }

        // Load new video into active player
        activeYouTubePlayer?.loadVideo(newVideo.videoId, 0f)
        binding.scrollContainer.smoothScrollTo(0, 0)
    }

    private fun observeFavoriteState(videoId: String) {
        lifecycleScope.launch {
            videoRepository.isFavorite(videoId).collect { isFav ->
                isFavoriteState = isFav
                updateFavoriteButtonUI(isFav)
            }
        }
    }

    private fun updateFavoriteButtonUI(isFav: Boolean) {
        if (isFav) {
            binding.tvFavIcon.text = "❤️"
            binding.tvFavLabel.text = "Saved"
            binding.tvFavLabel.setTextColor(Color.parseColor("#E91E63"))
        } else {
            binding.tvFavIcon.text = "🤍"
            binding.tvFavLabel.text = "Save"
            binding.tvFavLabel.setTextColor(getColor(android.R.color.black))
        }
    }

    private fun setupClickListeners(video: KidVideo) {
        // Back
        binding.btnBack.setOnSafeClickListener {
            finish()
        }

        // Toggle Favorite in Room Database
        binding.btnFavorite.setOnSafeClickListener {
            val targetVideo = activeVideo ?: return@setOnSafeClickListener
            lifecycleScope.launch {
                val newState = !isFavoriteState
                videoRepository.toggleFavorite(targetVideo, newState)
                showToast(if (newState) "Saved to Favorites ❤️" else "Removed from Favorites")
            }
        }

        // Share Video Link
        binding.btnShare.setOnSafeClickListener {
            val targetVideo = activeVideo ?: return@setOnSafeClickListener
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Watch \"${targetVideo.title}\" on Little Deen: https://www.youtube.com/watch?v=${targetVideo.videoId}"
                )
            }
            startActivity(Intent.createChooser(shareIntent, "Share Video"))
        }

        // 📂 Add to Playlist Button Click
        binding.btnAddToPlaylist.setOnSafeClickListener {
            val targetVideo = activeVideo ?: return@setOnSafeClickListener
            val dialog = AddPlaylistDialogFragment.newInstance(targetVideo)
            dialog.show(supportFragmentManager, AddPlaylistDialogFragment.TAG)
        }

        // 📺 View Channel Button Click
        // View Channel Button Click
        binding.btnViewChannel.setOnSafeClickListener {
            val targetVideo = activeVideo ?: return@setOnSafeClickListener
            val intent = Intent(this, ChannelDetailActivity::class.java).apply {
                putExtra(ChannelDetailActivity.EXTRA_CHANNEL_TITLE, targetVideo.channelTitle)
                putExtra(ChannelDetailActivity.EXTRA_CHANNEL_THUMB, targetVideo.thumbnailUrl)
            }
            startActivity(intent)
        }

    }

    private fun setupBackHandling() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isFullscreen) {
                    // If user presses Android back button in fullscreen, exit fullscreen cleanly
                    exitFullscreenCallback?.invoke()
                } else {
                    finish()
                }
            }
        })
    }

    override fun onDestroy() {
        binding.youtubePlayerView.release()
        // Resume background music if enabled by parent
        musicManager.startMusic()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        // 🔇 Pause background music whenever this screen is active or re-opened
        musicManager.pauseMusic()
    }

    companion object {
        const val EXTRA_VIDEO = "extra_video"
    }
}







