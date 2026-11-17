
package com.ccx.islamic.kids.littledeenapp.ui.activities.playlist

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityPlaylistsBinding
import com.ccx.islamic.kids.littledeenapp.extension.gone
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.extension.visible
import com.ccx.islamic.kids.littledeenapp.repo.VideoRepository
import com.ccx.islamic.kids.littledeenapp.ui.adapter.PlaylistSelectAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.ui.dialog.CreatePlaylistDialogFragment
import com.ccx.islamic.kids.littledeenapp.ui.dialog.DeleteConfirmationDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistsActivity :
    BaseActivity<ActivityPlaylistsBinding>(
        ActivityPlaylistsBinding::inflate
    ) {

    @Inject
    lateinit var videoRepository: VideoRepository

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var playlistAdapter: PlaylistSelectAdapter

    override fun setupViews() {
        applyTheme()
        setupRecyclerView()
        setupClickListeners()
        loadPlaylists()
    }

    // =============================================
    // Theme
    // =============================================

    private fun applyTheme() {
        val theme = themeManager.getSelectedTheme()

        binding.tvHeaderTitle.setTextColor(theme.primaryColor)

        binding.btnCreatePlaylistFab.backgroundTintList =
            ColorStateList.valueOf(theme.primaryColor)
    }

    // =============================================
    // RecyclerView
    // =============================================

    private fun setupRecyclerView() {

        playlistAdapter = PlaylistSelectAdapter(

            activeTheme = themeManager.getSelectedTheme(),

            // Activity lifecycle scope for video count requests
            adapterScope = lifecycleScope,

            // Fetch actual video count from Room
            videoCountFetcher = { playlistId ->
                videoRepository.videoDao.getPlaylistVideoCount(
                    playlistId
                )
            },

            // Open playlist details
            onPlaylistSelected = { playlist ->

                val intent = Intent(
                    this@PlaylistsActivity,
                    PlaylistDetailActivity::class.java
                ).apply {

                    putExtra(
                        PlaylistDetailActivity.EXTRA_PLAYLIST_ID,
                        playlist.playlistId
                    )

                    putExtra(
                        PlaylistDetailActivity.EXTRA_PLAYLIST_NAME,
                        playlist.playlistName
                    )
                }

                startActivity(intent)
            },

            // Multi-selection callback
            onSelectionChanged = { selectedCount ->

                updateSelectionUI(selectedCount)
            }
        )

        binding.rvPlaylists.apply {

            layoutManager = LinearLayoutManager(
                this@PlaylistsActivity
            )

            adapter = playlistAdapter

            setHasFixedSize(false)

            itemAnimator = null
        }
    }

    // =============================================
    // Selection UI
    // =============================================

    private fun updateSelectionUI(selectedCount: Int) {

        if (selectedCount > 0) {

            binding.btnCreatePlaylistFab.text =
                "🗑️ Delete ($selectedCount)"

            binding.btnCreatePlaylistFab.backgroundTintList =
                ColorStateList.valueOf(
                    Color.parseColor("#D32F2F")
                )

        } else {

            binding.btnCreatePlaylistFab.text =
                "+ New Playlist"

            binding.btnCreatePlaylistFab.backgroundTintList =
                ColorStateList.valueOf(
                    themeManager.getSelectedTheme().primaryColor
                )
        }
    }

    // =============================================
    // Load Playlists
    // =============================================

    private fun loadPlaylists() {

        lifecycleScope.launch {

            try {

                videoRepository.videoDao.getAllPlaylists()
                    .collect { playlists ->

                        if (playlists.isNotEmpty()) {

                            binding.rvPlaylists.visible()
                            binding.layoutEmptyPlaylists.gone()

                        } else {

                            binding.rvPlaylists.gone()
                            binding.layoutEmptyPlaylists.visible()
                        }

                        playlistAdapter.submitList(playlists)
                    }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                android.util.Log.e(
                    "PlaylistsActivity",
                    "Failed to load playlists",
                    e
                )

                showToast("Unable to load playlists")
            }
        }
    }

    // =============================================
    // Click Listeners
    // =============================================

    private fun setupClickListeners() {

        // Back button
        binding.btnBack.setOnSafeClickListener {

            if (playlistAdapter.isMultiSelectMode) {

                playlistAdapter.clearSelection()

            } else {

                finish()
            }
        }

        // Create / Delete button
        binding.btnCreatePlaylistFab.setOnSafeClickListener {

            if (playlistAdapter.isMultiSelectMode) {

                showDeleteConfirmation()

            } else {

                showCreatePlaylistDialog()
            }
        }
    }

    // =============================================
    // Create Playlist
    // =============================================

    private fun showCreatePlaylistDialog() {

        val dialog = CreatePlaylistDialogFragment.newInstance()

        dialog.show(
            supportFragmentManager,
            CreatePlaylistDialogFragment.TAG
        )
    }

    // =============================================
    // Delete Selected Playlists
    // =============================================

    private fun showDeleteConfirmation() {

        // Take a snapshot so the selection cannot change
        // while the confirmation dialog is open.
        val selectedIds =
            playlistAdapter.selectedPlaylistIds.toList()

        if (selectedIds.isEmpty()) {
            return
        }

        val count = selectedIds.size

        val dialog = DeleteConfirmationDialogFragment.newInstance(

            title = "Delete Playlists",

            message = "Are you sure you want to delete $count selected playlist(s)?",

            onConfirm = {

                lifecycleScope.launch {

                    try {

                        selectedIds.forEach { id ->

                            videoRepository.videoDao.deletePlaylist(id)
                        }

                        playlistAdapter.clearSelection()

                        showToast(
                            "Deleted $count playlist(s)"
                        )

                    } catch (e: CancellationException) {

                        throw e

                    } catch (e: Exception) {

                        android.util.Log.e(
                            "PlaylistsActivity",
                            "Failed to delete playlists",
                            e
                        )

                        showToast("Unable to delete playlists")
                    }
                }
            }
        )

        dialog.show(
            supportFragmentManager,
            DeleteConfirmationDialogFragment.TAG
        )
    }

    // =============================================
    // Back Press
    // =============================================

    @Deprecated("Deprecated in Android API")
    override fun onBackPressed() {

        if (::playlistAdapter.isInitialized &&
            playlistAdapter.isMultiSelectMode
        ) {

            playlistAdapter.clearSelection()

        } else {

            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (::playlistAdapter.isInitialized) {
            playlistAdapter.cancelPendingCountRequests()
        }

        super.onDestroy()
    }
}


