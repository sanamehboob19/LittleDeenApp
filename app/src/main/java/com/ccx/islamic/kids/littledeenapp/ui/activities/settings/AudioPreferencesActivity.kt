package com.ccx.islamic.kids.littledeenapp.ui.activities.settings


import android.content.Intent
import com.ccx.islamic.kids.littledeenapp.R
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.SeekBar
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.util.AppConstants
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityAudioPreferencesBinding
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.ui.activities.main.MainActivity
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.util.MusicManager
import com.ccx.islamic.kids.littledeenapp.util.PreferenceHelper
import com.google.android.material.materialswitch.MaterialSwitch
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject




@AndroidEntryPoint
class AudioPreferencesActivity : BaseActivity<ActivityAudioPreferencesBinding>(
    ActivityAudioPreferencesBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant
    @Inject
    lateinit var musicManager: MusicManager

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    override fun setupViews() {
        applyActiveThemeStyling()
        loadSavedSettings()
        setupListeners()

        // 🎯 If editing from settings, update button text
        if (!preferenceHelper.isFirstLaunch) {
            binding.btnLetsExplore.text = "Save Audio Settings"
        }
    }

    override fun onResume() {
        super.onResume()
        if (preferenceHelper.isTtsEnabled) {
            voiceAssistant.speak(getString(R.string.tts_audio_prefs_intro))
        }
    }

    /**
     * Dynamically styles all views with the child's chosen theme from the previous screen
     */
    private fun applyActiveThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        val primaryColor = theme.primaryColor
        val lightColor = theme.lightColor

        // Title and Buttons
        binding.tvTitle.setTextColor(primaryColor)
        binding.btnLetsExplore.backgroundTintList = ColorStateList.valueOf(primaryColor)

        binding.btnTestVoice.setTextColor(primaryColor)
        binding.btnTestVoice.strokeColor = ColorStateList.valueOf(primaryColor)
        binding.btnTestVoice.backgroundTintList = ColorStateList.valueOf(lightColor)

        // Tint SeekBars
        val primaryTint = ColorStateList.valueOf(primaryColor)
        binding.sbSpeed.progressTintList = primaryTint
        binding.sbSpeed.thumbTintList = primaryTint

        binding.sbPitch.progressTintList = primaryTint
        binding.sbPitch.thumbTintList = primaryTint

        binding.sbVolume.progressTintList = primaryTint
        binding.sbVolume.thumbTintList = primaryTint

        // Tint iOS-style MaterialSwitches
        styleIosSwitch(binding.switchTts, primaryColor)
        styleIosSwitch(binding.switchBgMusic, primaryColor)
        styleIosSwitch(binding.switchSoundFx, primaryColor)
    }

    /**
     * Styles MaterialSwitch into an iOS-style rounded toggle
     * (White thumb + theme color track when active, soft grey track when inactive)
     */
    private fun styleIosSwitch(switchView: MaterialSwitch, activeColor: Int) {
        val trackColors = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                activeColor,
                Color.parseColor("#E0E0E0") // Inactive gray track
            )
        )

        val thumbColors = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(
                Color.WHITE,
                Color.WHITE
            )
        )

        switchView.trackTintList = trackColors
        switchView.thumbTintList = thumbColors
    }

    private fun loadSavedSettings() {
        binding.switchTts.isChecked = preferenceHelper.isTtsEnabled
        binding.switchBgMusic.isChecked = preferenceHelper.isBgMusicEnabled
        binding.switchSoundFx.isChecked = preferenceHelper.isSoundFxEnabled

        binding.sbSpeed.progress = preferenceHelper.ttsSpeechRate
        binding.sbPitch.progress = preferenceHelper.ttsPitch
        binding.sbVolume.progress = preferenceHelper.appVolume
    }

    private fun setupListeners() {
        // Switches
        binding.switchTts.setOnCheckedChangeListener { _, isChecked ->
            preferenceHelper.isTtsEnabled = isChecked
            if (!isChecked) voiceAssistant.stop()
        }

        // Background Music Switch
        binding.switchBgMusic.setOnCheckedChangeListener { _, isChecked ->
            musicManager.setMusicEnabled(isChecked)
        }

        binding.switchSoundFx.setOnCheckedChangeListener { _, isChecked ->
            preferenceHelper.isSoundFxEnabled = isChecked
        }

        // Live Voice Speed slider (maps 0..100 to 0.5f..1.5f)
        binding.sbSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                val rate = 0.5f + (progress / 100f)
                voiceAssistant.setSpeechRate(rate)
                preferenceHelper.ttsSpeechRate = progress
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        // Live Voice Pitch slider (maps 0..100 to 0.7f..1.7f)
        binding.sbPitch.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                val pitch = 0.7f + (progress / 100f)
                voiceAssistant.setPitch(pitch)
                preferenceHelper.ttsPitch = progress
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        // Volume slider
        // Volume slider listener
        binding.sbVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                preferenceHelper.appVolume = progress
                musicManager.updateVolume(progress) // Updates music volume live!
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        // Test Voice button
        binding.btnTestVoice.setOnSafeClickListener {
            voiceAssistant.speak(getString(R.string.tts_sample_test))
        }

        // Finish Onboarding & Let's Explore!
        binding.btnLetsExplore.setOnSafeClickListener {
            voiceAssistant.stop()

            if (preferenceHelper.isFirstLaunch) {
                preferenceHelper.isFirstLaunch = false
                navigateToHomeScreen()
            } else {
                showToast("Audio settings updated!")
                finish() // 👈 Returns straight back to Settings!
            }
        }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
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



