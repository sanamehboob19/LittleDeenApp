package com.ccx.islamic.kids.littledeenapp.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.DialogSelectPlaylistBinding
import com.ccx.islamic.kids.littledeenapp.db.PlaylistVideoCrossRef
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.model.KidVideo
import com.ccx.islamic.kids.littledeenapp.repo.VideoRepository
import com.ccx.islamic.kids.littledeenapp.ui.adapter.PlaylistSelectAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddPlaylistDialogFragment :
    BaseDialogFragment<DialogSelectPlaylistBinding>(
        DialogSelectPlaylistBinding::inflate
    ) {

    @Inject
    lateinit var videoRepository: VideoRepository

    @Inject
    lateinit var themeManager: ThemeManager

    private var targetVideo: KidVideo? = null

    private lateinit var playlistAdapter: PlaylistSelectAdapter

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun setupViews() {
        val video = targetVideo

        if (video == null) {
            dismiss()
            return
        }

        setupDialogTheme()
        setupRecyclerView(video)
        setupClickListeners()
        observePlaylists()
    }

    private fun setupDialogTheme() {
        val primaryColor = themeManager.getSelectedTheme().primaryColor

        binding.btnCreateNewPlaylist.apply {
            setTextColor(primaryColor)
            strokeColor = android.content.res.ColorStateList.valueOf(primaryColor)
        }
    }

    private fun setupRecyclerView(video: KidVideo) {

        playlistAdapter = PlaylistSelectAdapter(
            activeTheme = themeManager.getSelectedTheme(),

            // Count requests are cancelled when this dialog view is destroyed.
            adapterScope = viewLifecycleOwner.lifecycleScope,

            videoCountFetcher = { playlistId ->
                videoRepository.videoDao.getPlaylistVideoCount(playlistId)
            },

            onPlaylistSelected = { playlist ->
                addVideoToPlaylist(
                    playlistId = playlist.playlistId,
                    playlistName = playlist.playlistName,
                    video = video
                )
            }
        )

        binding.rvSelectPlaylists.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playlistAdapter
            setHasFixedSize(false)
            itemAnimator = null
        }
    }

    private fun observePlaylists() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                videoRepository.videoDao.getAllPlaylists()
                    .collectLatest { playlists ->

                        Log.d(
                            TAG,
                            "Received playlists: ${playlists.size}"
                        )

                        playlistAdapter.submitList(playlists)

                        Log.d(
                            TAG,
                            "Adapter item count: ${playlistAdapter.itemCount}"
                        )
                    }
            }
        }
    }

    private fun addVideoToPlaylist(
        playlistId: Long,
        playlistName: String,
        video: KidVideo
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                videoRepository.recordWatchHistory(video)

                videoRepository.videoDao.addVideoToPlaylist(
                    PlaylistVideoCrossRef(
                        playlistId = playlistId,
                        videoId = video.videoId
                    )
                )

                requireContext().showToast(
                    "Added to \"$playlistName\" 📂"
                )

                dismiss()

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "Failed to add video to playlist",
                    e
                )

                if (isAdded) {
                    requireContext().showToast(
                        "Unable to add video to playlist"
                    )
                }
            }
        }
    }

    private fun setupClickListeners() {

        binding.btnClosePlaylistDialog.setOnSafeClickListener {
            dismiss()
        }

        binding.btnCreateNewPlaylist.setOnSafeClickListener {
            showCreatePlaylistPrompt()
        }
    }

    private fun showCreatePlaylistPrompt() {
        CreatePlaylistDialogFragment.newInstance()
            .show(
                parentFragmentManager,
                CreatePlaylistDialogFragment.TAG
            )
    }

    override fun onDestroyView() {
        if (::playlistAdapter.isInitialized) {
            playlistAdapter.cancelPendingCountRequests()
        }

        super.onDestroyView()
    }

    companion object {

        const val TAG = "AddPlaylistDialog"

        fun newInstance(video: KidVideo): AddPlaylistDialogFragment {
            return AddPlaylistDialogFragment().apply {
                targetVideo = video
            }
        }
    }
}
