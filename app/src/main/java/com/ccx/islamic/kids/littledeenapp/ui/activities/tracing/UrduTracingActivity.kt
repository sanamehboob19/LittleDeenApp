package com.ccx.islamic.kids.littledeenapp.ui.activities.tracing


import android.os.Build
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.model.UrduAlphabetModel
import com.ccx.islamic.kids.littledeenapp.data.UrduAlphabetProvider
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityUrduTracingBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.ui.dialog.ExcellentBannerDialogFragment
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class UrduTracingActivity : BaseActivity<ActivityUrduTracingBinding>(
    ActivityUrduTracingBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var themeManager: ThemeManager

    private var currentIndex = 0
    private val lettersList = UrduAlphabetProvider.letters

    override fun setupViews() {
        extractInitialLetter()
        applyThemeStyling()
        setupTopBar()
        setupBottomControls()
        setupTracingCallbacks()
        loadCurrentLetter()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.tvLetterTitle.setTextColor(theme.primaryColor)
        binding.btnNextLetter.backgroundTintList = android.content.res.ColorStateList.valueOf(theme.primaryColor)
    }

    private fun extractInitialLetter() {
        val selected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_SELECTED_URDU_LETTER, UrduAlphabetModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_SELECTED_URDU_LETTER)
        }

        currentIndex = if (selected != null) {
            lettersList.indexOfFirst { it.letter == selected.letter }.coerceAtLeast(0)
        } else {
            0
        }
    }

    private fun loadCurrentLetter() {
        val current = lettersList[currentIndex]

        binding.tvLetterTitle.text = "${current.letter} سے ${current.word} (${current.wordEnglish}) ${current.emoji}"
        binding.urduTracingView.loadLetter(current)

        // Pronounce using Urdu locale via VoiceAssistant
        voiceAssistant.speakInLocale("${current.letter}، ${current.word}", Locale("ur"))

        binding.btnPrevLetter.isEnabled = currentIndex > 0
        binding.btnPrevLetter.alpha = if (currentIndex > 0) 1.0f else 0.4f

        binding.btnNextLetter.isEnabled = currentIndex < lettersList.size - 1
        binding.btnNextLetter.alpha = if (currentIndex < lettersList.size - 1) 1.0f else 0.4f
    }

    private fun setupTopBar() {
        binding.btnBack.setOnSafeClickListener { finish() }
        binding.btnReset.setOnSafeClickListener { loadCurrentLetter() }
    }

    private fun setupBottomControls() {
        binding.btnPrevLetter.setOnSafeClickListener {
            if (currentIndex > 0) {
                currentIndex--
                loadCurrentLetter()
            }
        }

        binding.btnNextLetter.setOnSafeClickListener {
            if (currentIndex < lettersList.size - 1) {
                currentIndex++
                loadCurrentLetter()
            }
        }
    }

    private fun setupTracingCallbacks() {
        binding.urduTracingView.onCompleted = {
            // Show celebratory banner dialog
            ExcellentBannerDialogFragment.show(supportFragmentManager) {
                if (currentIndex < lettersList.size - 1) {
                    currentIndex++
                    loadCurrentLetter()
                } else {
                    finish()
                }
            }
        }
    }

    companion object {
        const val EXTRA_SELECTED_URDU_LETTER = "extra_selected_urdu_letter"
    }
}