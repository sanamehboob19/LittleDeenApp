package com.ccx.islamic.kids.littledeenapp.ui.activities.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ccx.islamic.kids.littledeenapp.R
import android.content.SharedPreferences
import android.content.res.ColorStateList
import androidx.recyclerview.widget.GridLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.extension.gone
import com.ccx.islamic.kids.littledeenapp.extension.visible
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityThemeSelectionBinding
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.model.AppThemeColor
import com.ccx.islamic.kids.littledeenapp.ui.activities.settings.AudioPreferencesActivity
import com.ccx.islamic.kids.littledeenapp.ui.adapter.ThemeColorAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.util.AppConstants
import com.ccx.islamic.kids.littledeenapp.util.PreferenceHelper
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject




@AndroidEntryPoint
class ThemeSelectionActivity : BaseActivity<ActivityThemeSelectionBinding>(
    ActivityThemeSelectionBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private lateinit var themeAdapter: ThemeColorAdapter

    override fun setupViews() {
        updateAudioIcon()
        setupColorGrid()
        setupClickListeners()

        // 🎯 If editing from settings, update button text
        if (!preferenceHelper.isFirstLaunch) {
            binding.btnContinue.text = "Save Theme"
        }
    }

    override fun onResume() {
        super.onResume()
        if (preferenceHelper.isTtsEnabled) {
            voiceAssistant.speak(getString(R.string.tts_theme_selection))
        }
    }

    private fun setupColorGrid() {
        val colors = ThemeManager.THEME_PALETTES
        val currentTheme = themeManager.getSelectedTheme()
        val defaultIndex = colors.indexOfFirst { it.id == currentTheme.id }.coerceAtLeast(0)

        // Initialize preview with currently active theme
        updateLivePreview(currentTheme)

        themeAdapter = ThemeColorAdapter(colors, defaultIndex) { selectedTheme ->
            // Update preview live when tapped
            updateLivePreview(selectedTheme)
        }

        binding.rvThemeColors.apply {
            layoutManager = GridLayoutManager(this@ThemeSelectionActivity, 2)
            adapter = themeAdapter
        }
    }

    private fun updateLivePreview(theme: AppThemeColor) {
        with(binding) {
            cardLivePreview.setCardBackgroundColor(theme.lightColor)
            cardLivePreview.strokeColor = theme.primaryColor
            tvThemeNamePreview.text = theme.name
            tvThemeNamePreview.setTextColor(theme.primaryColor)
            btnContinue.backgroundTintList = ColorStateList.valueOf(theme.primaryColor)
        }
    }

    private fun setupClickListeners() {
        // Audio Toggle
        binding.cardAudioToggle.setOnSafeClickListener {
            preferenceHelper.isTtsEnabled = !preferenceHelper.isTtsEnabled
            updateAudioIcon()

            if (preferenceHelper.isTtsEnabled) {
                voiceAssistant.speak(getString(R.string.tts_theme_selection))
            } else {
                voiceAssistant.stop()
            }
        }

        // Continue Button
        binding.btnContinue.setOnSafeClickListener {
            val chosenTheme = themeAdapter.getSelectedTheme()
            themeManager.saveTheme(chosenTheme.id)
            voiceAssistant.stop()

            // 🎯 Check mode
            if (preferenceHelper.isFirstLaunch) {
                navigateToPreferencesScreen()
            } else {
                showToast("Theme color updated!")
                finish() // 👈 Returns straight back to Settings!
            }
        }
    }

    private fun updateAudioIcon() {
        val iconRes = if (preferenceHelper.isTtsEnabled) {
            R.drawable.ic_volume_up
        } else {
            R.drawable.ic_volume_off
        }
        binding.ivAudioToggle.setImageResource(iconRes)
    }

    private fun navigateToPreferencesScreen() {
        startActivity(Intent(this, AudioPreferencesActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onPause() {
        voiceAssistant.stop()
        super.onPause()
    }

    override fun onDestroy() {
        voiceAssistant.stop()
        super.onDestroy()
    }
}


