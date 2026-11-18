package com.ccx.islamic.kids.littledeenapp.ui.dialog


import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.DialogCreatePlaylistBinding
import com.ccx.islamic.kids.littledeenapp.db.PlaylistEntity
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.repo.VideoRepository
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CreatePlaylistDialogFragment : BaseDialogFragment<DialogCreatePlaylistBinding>(
    DialogCreatePlaylistBinding::inflate
) {

    @Inject
    lateinit var videoRepository: VideoRepository

    @Inject
    lateinit var themeManager: ThemeManager

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
            setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun setupViews() {
        val theme = themeManager.getSelectedTheme()
        binding.btnConfirmCreate.backgroundTintList = ColorStateList.valueOf(theme.primaryColor)

        binding.btnCancelCreate.setOnSafeClickListener { dismiss() }

        binding.btnConfirmCreate.setOnSafeClickListener {
            val name = binding.etPlaylistName.text.toString().trim()
            if (name.isNotEmpty()) {
                lifecycleScope.launch {
                    videoRepository.videoDao.insertPlaylist(PlaylistEntity(playlistName = name))
//                    showToast("Playlist \"$name\" created! 📂")
                    dismiss()
                }
            } else {
//                showToast("Please enter a playlist name")
            }
        }
    }

    companion object {
        const val TAG = "CreatePlaylistDialog"
        fun newInstance() = CreatePlaylistDialogFragment()
    }
}