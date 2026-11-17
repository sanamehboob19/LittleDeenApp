package com.ccx.islamic.kids.littledeenapp.ui.activities.playlist


import com.ccx.islamic.kids.littledeenapp.R
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityPlaylistDetailBinding
import com.ccx.islamic.kids.littledeenapp.extension.gone
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.extension.visible
import com.ccx.islamic.kids.littledeenapp.model.KidVideo
import com.ccx.islamic.kids.littledeenapp.repo.VideoRepository
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.VideoPlayerActivity
import com.ccx.islamic.kids.littledeenapp.ui.adapter.HistoryAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.app.AlertDialog
import android.net.Uri
import android.widget.EditText
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import kotlinx.coroutines.flow.first




@AndroidEntryPoint
class PlaylistDetailActivity : BaseActivity<ActivityPlaylistDetailBinding>(
    ActivityPlaylistDetailBinding::inflate
) {

    @Inject
    lateinit var videoRepository: VideoRepository

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var videoAdapter: HistoryAdapter
    private var playlistId: Long = -1L
    private var playlistName: String = ""
    private var coverImageUri: String? = null

    // CanHub Cropper Launcher for Playlist Cover
    private val cropImageLauncher = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            result.uriContent?.let { uri ->
                coverImageUri = uri.toString()
                updatePlaylistCoverInDb(uri.toString())
            }
        }
    }

    override fun setupViews() {
        playlistId = intent.getLongExtra(EXTRA_PLAYLIST_ID, -1L)
        playlistName = intent.getStringExtra(EXTRA_PLAYLIST_NAME) ?: "Playlist"

        binding.tvPlaylistDetailTitle.text = playlistName
        loadPlaylistData()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun loadPlaylistData() {
        lifecycleScope.launch {
            val allPlaylists = videoRepository.videoDao.getAllPlaylists().first()
            val currentPlaylist = allPlaylists.find { it.playlistId == playlistId }
            currentPlaylist?.let {
                playlistName = it.playlistName
                coverImageUri = it.coverImageUri
                binding.tvPlaylistDetailTitle.text = playlistName

                // Load cover image
                Glide.with(this@PlaylistDetailActivity)
                    .load(if (!coverImageUri.isNullOrEmpty()) Uri.parse(coverImageUri) else R.drawable.bg_input_field)
                    .centerCrop()
                    .into(binding.ivPlaylistCover)
            }
        }
    }

    private fun setupRecyclerView() {
        videoAdapter = HistoryAdapter { historyItem ->
            val video = KidVideo(
                videoId = historyItem.videoId,
                title = historyItem.title,
                channelTitle = historyItem.channelTitle,
                thumbnailUrl = historyItem.thumbnailUrl,
                badgeLabel = historyItem.durationText,
                categoryId = historyItem.categoryId
            )
            val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                putExtra(VideoPlayerActivity.EXTRA_VIDEO, video)
            }
            startActivity(intent)
        }

        binding.rvPlaylistVideos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = videoAdapter
        }

        lifecycleScope.launch {
            videoRepository.videoDao.getVideosForPlaylist(playlistId).collect { savedVideos ->
                binding.tvPlaylistMeta.text = "${savedVideos.size} items • Custom playlist"
                if (savedVideos.isNotEmpty()) {
                    binding.rvPlaylistVideos.visible()
                    binding.layoutEmptyPlaylistVideos.gone()
                    videoAdapter.submitList(savedVideos)
                } else {
                    binding.rvPlaylistVideos.gone()
                    binding.layoutEmptyPlaylistVideos.visible()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }

        // Change Cover Button
        binding.btnChangeCover.setOnSafeClickListener {
            openImageCropper()
        }

        // 🎯 Play All Button
        binding.btnPlayAll.setOnSafeClickListener {
            lifecycleScope.launch {
                val videos = videoRepository.videoDao.getVideosForPlaylist(playlistId).first()
                if (videos.isNotEmpty()) {
                    // Convert first saved video to KidVideo and launch player
                    val firstItem = videos[0]
                    val video = KidVideo(
                        videoId = firstItem.videoId,
                        title = firstItem.title,
                        channelTitle = firstItem.channelTitle,
                        thumbnailUrl = firstItem.thumbnailUrl,
                        badgeLabel = firstItem.durationText,
                        categoryId = firstItem.categoryId
                    )
                    val intent = Intent(this@PlaylistDetailActivity, VideoPlayerActivity::class.java).apply {
                        putExtra(VideoPlayerActivity.EXTRA_VIDEO, video)
                    }
                    startActivity(intent)
                } else {
                    showToast("No videos found to play in this playlist!")
                }
            }
        }

        // Three-Dot Menu Options (Rename, Change Cover, Delete)
        binding.btnOptionsMenu.setOnSafeClickListener { view ->
            showPopupMenu(view)
        }
    }
    private fun showPopupMenu(anchor: android.view.View) {
        val popup = android.widget.PopupMenu(this, anchor)
        popup.menu.add(0, 1, 0, "Change Cover Image")
        popup.menu.add(0, 2, 1, "Rename Playlist")
        popup.menu.add(0, 3, 2, "Delete Playlist")

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                1 -> {
                    openImageCropper()
                    true
                }
                2 -> {
                    showRenameDialog()
                    true
                }
                3 -> {
                    deletePlaylist()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun openImageCropper() {
        val cropOptions = CropImageOptions().apply {
            imageSourceIncludeGallery = true
            imageSourceIncludeCamera = true
            cropShape = CropImageView.CropShape.RECTANGLE
            fixAspectRatio = true
            aspectRatioX = 16
            aspectRatioY = 9
            guidelines = CropImageView.Guidelines.ON
        }
        cropImageLauncher.launch(CropImageContractOptions(uri = null, cropImageOptions = cropOptions))
    }

    private fun updatePlaylistCoverInDb(uriStr: String) {
        lifecycleScope.launch {
            val allPlaylists = videoRepository.videoDao.getAllPlaylists().first()
            val current = allPlaylists.find { it.playlistId == playlistId }
            current?.let {
                val updated = it.copy(coverImageUri = uriStr)
                videoRepository.videoDao.insertPlaylist(updated)
                showToast("Playlist cover updated! 🖼️")
                loadPlaylistData()
            }
        }
    }

    private fun showRenameDialog() {
        val input = EditText(this).apply {
            setText(playlistName)
            setPadding(40, 30, 40, 30)
        }

        AlertDialog.Builder(this)
            .setTitle("Rename Playlist")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    lifecycleScope.launch {
                        val allPlaylists = videoRepository.videoDao.getAllPlaylists().first()
                        val current = allPlaylists.find { it.playlistId == playlistId }
                        current?.let {
                            val updated = it.copy(playlistName = newName)
                            videoRepository.videoDao.insertPlaylist(updated)
                            playlistName = newName
                            binding.tvPlaylistDetailTitle.text = newName
                            showToast("Playlist renamed!")
                        }
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deletePlaylist() {
        AlertDialog.Builder(this)
            .setTitle("Delete Playlist")
            .setMessage("Are you sure you want to delete \"$playlistName\"?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    videoRepository.videoDao.deletePlaylist(playlistId)
                    showToast("Playlist deleted")
                    finish()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    companion object {
        const val EXTRA_PLAYLIST_ID = "extra_playlist_id"
        const val EXTRA_PLAYLIST_NAME = "extra_playlist_name"
    }
}