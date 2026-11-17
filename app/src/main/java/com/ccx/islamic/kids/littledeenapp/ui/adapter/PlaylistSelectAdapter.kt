package com.ccx.islamic.kids.littledeenapp.ui.adapter



import android.content.res.ColorStateList
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.databinding.ItemPlaylistRowBinding
import com.ccx.islamic.kids.littledeenapp.db.PlaylistEntity
import com.ccx.islamic.kids.littledeenapp.extension.gone
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.visible
import com.ccx.islamic.kids.littledeenapp.model.AppThemeColor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PlaylistSelectAdapter(
    private val activeTheme: AppThemeColor,
    private val adapterScope: CoroutineScope,
    private val videoCountFetcher: suspend (Long) -> Int,
    private val onPlaylistSelected: (PlaylistEntity) -> Unit,
    private val onSelectionChanged: (Int) -> Unit = {}
) : RecyclerView.Adapter<PlaylistSelectAdapter.PlaylistViewHolder>()
{

    companion object {
        private const val TAG = "PlaylistCount"
    }

    private var playlists: List<PlaylistEntity> = emptyList()

    val selectedPlaylistIds = mutableSetOf<Long>()

    var isMultiSelectMode = false
        private set

    inner class PlaylistViewHolder(
        val binding: ItemPlaylistRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        var countJob: Job? = null
        var boundPlaylistId: Long? = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder {

        val binding = ItemPlaylistRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PlaylistViewHolder(binding)
    }


    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int
    ) {
        val playlist = playlists[position]
        val playlistId = playlist.playlistId
        val context = holder.itemView.context
        val isSelected = selectedPlaylistIds.contains(playlistId)

        // Cancel previous count request to prevent recycled-holder issues.
        holder.countJob?.cancel()
        holder.countJob = null
        holder.boundPlaylistId = playlistId

        with(holder.binding) {

            // -------------------------------------
            // Playlist Information
            // -------------------------------------

            tvPlaylistEmoji.text = playlist.playlistEmoji
            tvPlaylistName.text = playlist.playlistName

            val dateStr = DateUtils.getRelativeTimeSpanString(
                playlist.createdAt,
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS
            ).toString()

            tvCreatedDate.text = dateStr

            // -------------------------------------
            // Cover Image
            // -------------------------------------

            if (!playlist.coverImageUri.isNullOrEmpty()) {
                ivPlaylistCover.visible()
                tvPlaylistEmoji.gone() // Hide emoji if custom photo is present

                com.bumptech.glide.Glide.with(context)
                    .load(android.net.Uri.parse(playlist.coverImageUri))
                    .centerCrop()
                    .into(ivPlaylistCover)
            } else {
                ivPlaylistCover.gone()
                tvPlaylistEmoji.visible()
            }

            // -------------------------------------
            // Video Count
            // -------------------------------------

            tvVideoCount.apply {
                text = "Loading..."
                setTextColor(activeTheme.primaryColor)
            }

            holder.countJob = adapterScope.launch {
                try {
                    Log.d(TAG, "Fetching count for playlist ID: $playlistId")

                    val count = videoCountFetcher(playlistId)

                    Log.d(TAG, "Playlist ID: $playlistId, Video count: $count")

                    if (
                        holder.bindingAdapterPosition != RecyclerView.NO_POSITION &&
                        holder.boundPlaylistId == playlistId
                    ) {
                        tvVideoCount.apply {
                            text = when (count) {
                                0 -> "No videos yet"
                                1 -> "1 video"
                                else -> "$count videos"
                            }

                            setTextColor(activeTheme.primaryColor)
                        }
                    }

                } catch (e: CancellationException) {
                    throw e

                } catch (e: Exception) {
                    Log.e(
                        TAG,
                        "Failed to fetch video count for playlist ID: $playlistId",
                        e
                    )

                    if (
                        holder.bindingAdapterPosition != RecyclerView.NO_POSITION &&
                        holder.boundPlaylistId == playlistId
                    ) {
                        tvVideoCount.apply {
                            text = "Count unavailable"
                            setTextColor(activeTheme.primaryColor)
                        }
                    }
                }
            }

            // -------------------------------------
            // Multi Selection UI
            // -------------------------------------

            if (isMultiSelectMode) {

                checkboxSelect.visible()
                tvSelectHint.gone()

                checkboxSelect.isChecked = isSelected

                checkboxSelect.buttonTintList =
                    ColorStateList.valueOf(activeTheme.primaryColor)

                if (isSelected) {

                    cardPlaylistRow.setCardBackgroundColor(
                        activeTheme.lightColor
                    )

                    cardPlaylistRow.strokeColor =
                        activeTheme.primaryColor

                } else {

                    cardPlaylistRow.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_surface
                        )
                    )

                    cardPlaylistRow.strokeColor =
                        ContextCompat.getColor(
                            context,
                            R.color.color_border
                        )
                }

            } else {

                checkboxSelect.gone()
                tvSelectHint.visible()

                cardPlaylistRow.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_surface
                    )
                )

                cardPlaylistRow.strokeColor =
                    ContextCompat.getColor(
                        context,
                        R.color.color_border
                    )
            }

            // -------------------------------------
            // Normal Click
            // -------------------------------------

            cardPlaylistRow.setOnSafeClickListener {
                if (isMultiSelectMode) {
                    toggleSelection(playlistId)
                } else {
                    onPlaylistSelected(playlist)
                }
            }

            // -------------------------------------
            // Long Press Selection
            // -------------------------------------

            cardPlaylistRow.setOnLongClickListener {
                if (!isMultiSelectMode) {
                    isMultiSelectMode = true
                    toggleSelection(playlistId)
                }

                true
            }
        }
    }

    // =============================================
    // Selection Management
    // =============================================

    private fun toggleSelection(id: Long) {

        if (selectedPlaylistIds.contains(id)) {

            selectedPlaylistIds.remove(id)

            if (selectedPlaylistIds.isEmpty()) {

                isMultiSelectMode = false
            }

        } else {

            selectedPlaylistIds.add(id)
        }

        onSelectionChanged(selectedPlaylistIds.size)

        notifyDataSetChanged()
    }

    fun clearSelection() {

        isMultiSelectMode = false

        selectedPlaylistIds.clear()

        onSelectionChanged(0)

        notifyDataSetChanged()
    }

    // =============================================
    // Update Playlist List
    // =============================================

    fun submitList(newList: List<PlaylistEntity>) {

        playlists = newList

        // Remove selections for deleted playlists.
        val validIds = newList.map { it.playlistId }.toSet()

        selectedPlaylistIds.retainAll(validIds)

        if (selectedPlaylistIds.isEmpty()) {
            isMultiSelectMode = false
        }

        onSelectionChanged(selectedPlaylistIds.size)

        notifyDataSetChanged()
    }

    // =============================================
    // RecyclerView
    // =============================================

    override fun getItemCount(): Int = playlists.size

    override fun onViewRecycled(holder: PlaylistViewHolder) {

        holder.countJob?.cancel()
        holder.countJob = null
        holder.boundPlaylistId = null

        super.onViewRecycled(holder)
    }

    fun cancelPendingCountRequests() {
        // Cancel all requests through the adapter scope's
        // lifecycle when the Activity is destroyed.
    }
}
