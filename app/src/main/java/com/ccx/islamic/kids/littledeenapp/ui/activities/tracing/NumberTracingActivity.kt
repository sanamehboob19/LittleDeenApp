package com.ccx.islamic.kids.littledeenapp.ui.activities.tracing


import android.os.Build
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.model.NumberModel
import com.ccx.islamic.kids.littledeenapp.data.NumberProvider
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityNumberTracingBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.ui.dialog.ExcellentBannerDialogFragment
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NumberTracingActivity : BaseActivity<ActivityNumberTracingBinding>(
    ActivityNumberTracingBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var themeManager: ThemeManager

    private var currentIndex = 0
    private val numbersList = NumberProvider.numbers

    override fun setupViews() {
        extractInitialNumber()
        applyThemeStyling()
        setupTopBar()
        setupBottomControls()
        setupTracingCallbacks()
        loadCurrentNumber()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.tvNumberTitle.setTextColor(theme.primaryColor)
        binding.btnNextNumber.backgroundTintList = android.content.res.ColorStateList.valueOf(theme.primaryColor)
    }

    private fun extractInitialNumber() {
        val selected = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_SELECTED_NUMBER, NumberModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_SELECTED_NUMBER)
        }

        currentIndex = if (selected != null) {
            numbersList.indexOfFirst { it.number == selected.number }.coerceAtLeast(0)
        } else {
            0
        }
    }

    private fun loadCurrentNumber() {
        val current = numbersList[currentIndex]

        binding.tvNumberTitle.text = "Number ${current.word} (${current.text}) ⭐"
        binding.numberTracingView.loadNumber(current)

        // Pronounce via VoiceAssistant
        voiceAssistant.speak("Number ${current.word}!")

        binding.btnPrevNumber.isEnabled = currentIndex > 0
        binding.btnPrevNumber.alpha = if (currentIndex > 0) 1.0f else 0.4f

        binding.btnNextNumber.isEnabled = currentIndex < numbersList.size - 1
        binding.btnNextNumber.alpha = if (currentIndex < numbersList.size - 1) 1.0f else 0.4f
    }

    private fun setupTopBar() {
        binding.btnBack.setOnSafeClickListener { finish() }
        binding.btnReset.setOnSafeClickListener { loadCurrentNumber() }
    }

    private fun setupBottomControls() {
        binding.btnPrevNumber.setOnSafeClickListener {
            if (currentIndex > 0) {
                currentIndex--
                loadCurrentNumber()
            }
        }

        binding.btnNextNumber.setOnSafeClickListener {
            if (currentIndex < numbersList.size - 1) {
                currentIndex++
                loadCurrentNumber()
            }
        }
    }

    private fun setupTracingCallbacks() {
        binding.numberTracingView.onCompleted = {
            // Show celebratory banner
            ExcellentBannerDialogFragment.show(supportFragmentManager) {
                if (currentIndex < numbersList.size - 1) {
                    currentIndex++
                    loadCurrentNumber()
                } else {
                    finish()
                }
            }
        }
    }

    companion object {
        const val EXTRA_SELECTED_NUMBER = "extra_selected_number"
    }
}