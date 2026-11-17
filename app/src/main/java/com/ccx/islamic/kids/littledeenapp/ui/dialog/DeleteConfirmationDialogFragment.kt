package com.ccx.islamic.kids.littledeenapp.ui.dialog



import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager

import com.ccx.islamic.kids.littledeenapp.databinding.DialogDeleteConfirmationBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeleteConfirmationDialogFragment : BaseDialogFragment<DialogDeleteConfirmationBinding>(
    DialogDeleteConfirmationBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    private var onDeleteConfirmed: (() -> Unit)? = null
    private var dialogTitle: String = "Clear History"
    private var dialogMessage: String = "Are you sure you want to delete these watch records?"

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
        binding.btnConfirmDelete.backgroundTintList = ColorStateList.valueOf(theme.primaryColor)

        binding.tvDeleteTitle.text = dialogTitle
        binding.tvDeleteMessage.text = dialogMessage

        binding.btnCancelDelete.setOnSafeClickListener {
            dismiss()
        }

        binding.btnConfirmDelete.setOnSafeClickListener {
            dismiss()
            onDeleteConfirmed?.invoke()
        }
    }

    companion object {
        const val TAG = "DeleteConfirmationDialog"

        fun newInstance(title: String, message: String, onConfirm: () -> Unit): DeleteConfirmationDialogFragment {
            return DeleteConfirmationDialogFragment().apply {
                this.dialogTitle = title
                this.dialogMessage = message
                this.onDeleteConfirmed = onConfirm
            }
        }
    }
}