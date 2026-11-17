package com.ccx.islamic.kids.littledeenapp.ui.dialog


import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.DialogKidsLoadingBinding
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityFullScreenLoadingBinding
import com.ccx.islamic.kids.littledeenapp.ui.activities.main.MainActivity
import com.ccx.islamic.kids.littledeenapp.util.PreferenceHelper
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@AndroidEntryPoint
class KidsLoadingDialogFragment : BaseDialogFragment<ActivityFullScreenLoadingBinding>(
    ActivityFullScreenLoadingBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private var messageCycleJob: Job? = null
    private var syncJob: Job? = null

    private val loadingPhrases = listOf(
        "Setting up your Islamic garden, please wait! 🌿",
        "Preparing safe halal cartoons and stories... 🎬",
        "Organizing beautiful daily duas for you... 📖",
        "Almost ready for takeoff, little explorer! 🚀"
    )

    override fun onStart() {
        super.onStart()
        // 🎯 1. Make dialog full-screen and non-cancelable
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialog?.setCancelable(false)
    }

    override fun setupViews() {
        applyThemeStyling()
        loadChildAvatar()
        startMessageCycle()
        startInitialSync()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.progressLoading.setIndicatorColor(theme.primaryColor)
        binding.progressLoading.trackColor = theme.lightColor
        binding.cardLoadingAvatarRing.strokeColor = theme.primaryColor
    }

    /**
     * Loads the child's actual photo or selected character avatar
     */
    private fun loadChildAvatar() {
        val customUriStr = preferenceHelper.customAvatarUri
        val defaultAvatarRes = preferenceHelper.childAvatarRes.let {
            if (it != 0) it else R.drawable.avatar_boy_1
        }

        if (!customUriStr.isNullOrEmpty()) {
            // 🎯 User picked a custom gallery photo: Load with Glide
            Glide.with(requireContext())
                .load(Uri.parse(customUriStr))
                .circleCrop()
                .placeholder(defaultAvatarRes)
                .error(defaultAvatarRes)
                .into(binding.ivLoadingAvatar)
        } else {
            // 🎯 User selected an avatar character in ChildNameActivity: Load instantly!
            binding.ivLoadingAvatar.setImageResource(defaultAvatarRes)
        }

        // Animate the floating star
        YoYo.with(Techniques.Swing)
            .duration(1200)
            .repeat(YoYo.INFINITE)
            .playOn(binding.tvFloatingStar)
    }

    /**
     * Cycles through encouraging text messages and speaks them aloud via TTS
     */
    private fun startMessageCycle() {
        messageCycleJob = viewLifecycleOwner.lifecycleScope.launch {
            var index = 0
            while (true) {
                val currentText = loadingPhrases[index % loadingPhrases.size]
                binding.tvLoadingMessage.text = currentText
                voiceAssistant.speak(currentText)

                index++
                delay(2800L)
            }
        }
    }

    /**
     * Simulates initial data caching and finishes with a celebration
     */
    private fun startInitialSync() {
        syncJob = viewLifecycleOwner.lifecycleScope.launch {
            // First time sync duration (7.5s)
            delay(7500L)
            messageCycleJob?.cancel()

            // 🎯 Celebration tick & message
            val celebrationText = "Thanks for waiting! Let's explore! 🎉"
            binding.tvLoadingMessage.text = celebrationText
            voiceAssistant.speak("Thanks for waiting! Let's explore together!")

            YoYo.with(Techniques.Bounce)
                .duration(800)
                .playOn(binding.tvLoadingMessage)

            delay(1800L)
            dismiss()

            // Navigate to Home screen
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        messageCycleJob?.cancel()
        syncJob?.cancel()
        voiceAssistant.stop()
        super.onDestroyView()
    }

    companion object {
        const val TAG = "KidsLoadingDialog"
        fun newInstance() = KidsLoadingDialogFragment()
    }
}



