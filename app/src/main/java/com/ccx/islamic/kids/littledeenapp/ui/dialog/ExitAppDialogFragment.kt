package com.ccx.islamic.kids.littledeenapp.ui.dialog


import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.DialogExitAppBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExitAppDialogFragment : BaseDialogFragment<DialogExitAppBinding>(
    DialogExitAppBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    private var onExitConfirmed: (() -> Unit)? = null

    override fun onStart() {
        super.onStart()
        // Anchor dialog to the bottom of the screen with full width
        dialog?.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setGravity(Gravity.BOTTOM)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            // Smooth bottom-sheet animation
            setWindowAnimations(android.R.style.Animation_InputMethod)
        }
    }

    override fun setupViews() {
        // Apply active theme color to the "Stay & Learn" button
        val primaryColor = themeManager.getSelectedTheme().primaryColor
        binding.btnStay.backgroundTintList = ColorStateList.valueOf(primaryColor)

        // Cancel / Stay
        binding.btnStay.setOnSafeClickListener {
            dismiss()
        }

        // Confirm Exit
        binding.btnExit.setOnSafeClickListener {
            dismiss()
            onExitConfirmed?.invoke()
        }
    }

    companion object {
        const val TAG = "ExitAppDialogFragment"

        fun newInstance(onExit: () -> Unit): ExitAppDialogFragment {
            return ExitAppDialogFragment().apply {
                this.onExitConfirmed = onExit
            }
        }
    }
}