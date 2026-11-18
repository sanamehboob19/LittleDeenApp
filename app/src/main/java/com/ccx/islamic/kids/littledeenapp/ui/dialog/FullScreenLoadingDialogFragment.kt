package com.ccx.islamic.kids.littledeenapp.ui.dialog


import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityFullScreenLoadingBinding
import com.ccx.islamic.kids.littledeenapp.repo.VideoRepository
import com.ccx.islamic.kids.littledeenapp.ui.activities.main.MainActivity
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseDialogFragment
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class FullScreenLoadingDialogFragment : BaseDialogFragment<ActivityFullScreenLoadingBinding>(
    ActivityFullScreenLoadingBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var themeManager: ThemeManager

    private var messageJob: Job? = null

    private val loadingPhrases = listOf(
        "Setting up your Islamic garden, please wait! 🌿",
        "Downloading safe halal cartoons and stories... 🎬",
        "Organizing beautiful daily duas for you... 📖",
        "Almost ready for takeoff, little explorer! 🚀"
    )

    override fun onStart() {
        super.onStart()
        // 🎯 Force Dialog to match full screen and be non-cancelable
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#E6F4EA"))) // Matches background gradient tone
        }
        dialog?.setCancelable(false) // Blocks back button & outside touch so user can't exit mid-sync
    }

    override fun setupViews() {
        applyTheme()
        startCyclingMessages()
        simulateFirstTimeSync()
    }

    private fun applyTheme() {
        val theme = themeManager.getSelectedTheme()
        binding.progressLoading.progressTintList = ColorStateList.valueOf(theme.primaryColor)
    }

    private fun startCyclingMessages() {
        messageJob = lifecycleScope.launch {
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

    private fun simulateFirstTimeSync() {
        lifecycleScope.launch {
            delay(8500L) // Simulates initial database caching sync
            messageJob?.cancel()

            binding.tvLoadingMessage.text = "Thanks for waiting! Let's explore! 🎉"
            voiceAssistant.speak("Thanks for waiting! Let's explore together!")

            YoYo.with(Techniques.Bounce)
                .duration(800)
                .playOn(binding.tvLoadingMessage)

            delay(2000L)
            dismiss()

            // Navigate to Main Screen
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        messageJob?.cancel()
        voiceAssistant.stop()
        super.onDestroyView()
    }

    companion object {
        const val TAG = "FullScreenLoadingDialog"
        fun newInstance() = FullScreenLoadingDialogFragment()
    }
}


