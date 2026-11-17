package com.ccx.islamic.kids.littledeenapp.ui.dialog


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.ccx.islamic.kids.littledeenapp.databinding.DialogStreakPopupBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseDialogFragment
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StreakPopupDialogFragment : BaseDialogFragment<DialogStreakPopupBinding>(
    DialogStreakPopupBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    private var streakDays: Int = 1

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun setupViews() {
        binding.tvStreakPopupMessage.text = "You are on a $streakDays-Day Learning Streak! 🔥"

        // Speak encouragement aloud
        voiceAssistant.speak("Amazing! You are on a $streakDays day learning streak. Keep it up!")

        // Bounce animation
        YoYo.with(Techniques.BounceIn)
            .duration(700)
            .playOn(binding.root)

        binding.btnAwesome.setOnSafeClickListener {
            dismiss()
        }
    }

    companion object {
        const val TAG = "StreakPopupDialog"

        fun newInstance(days: Int): StreakPopupDialogFragment {
            return StreakPopupDialogFragment().apply {
                this.streakDays = days
            }
        }
    }
}