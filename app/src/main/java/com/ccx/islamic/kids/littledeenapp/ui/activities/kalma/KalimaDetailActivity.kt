package com.ccx.islamic.kids.littledeenapp.ui.activities.kalma

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ccx.islamic.kids.littledeenapp.R


import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.core.graphics.ColorUtils
import com.ccx.islamic.kids.littledeenapp.data.KalimaItem
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityKalimaDetailBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast
import com.ccx.islamic.kids.littledeenapp.util.MusicManager
import androidx.annotation.RawRes





@AndroidEntryPoint
class KalimaDetailActivity : BaseActivity<ActivityKalimaDetailBinding>(
    ActivityKalimaDetailBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var musicManager: MusicManager

    private lateinit var kalimasList: List<KalimaItem>
    private var currentIndex = 0
    private var isUrduActive = false

    // 🎵 Instant Offline Player
    private var mediaPlayer: MediaPlayer? = null
    private var isAudioPlaying = false

    override fun setupViews() {

        kalimasList = KalimaItem.getAllKalimas()
        val targetId = intent.getIntExtra(EXTRA_KALIMA_ID, 1)
        currentIndex = kalimasList.indexOfFirst { it.id == targetId }.coerceAtLeast(0)

        applyTheme()
        displayCurrentKalima()
        setupClickListeners()
    }

    private fun applyTheme() {
        val theme = themeManager.getSelectedTheme()
        val primary = theme.primaryColor

        val lighter = ColorUtils.blendARGB(primary, Color.WHITE, 0.25f)
        val grad = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(primary, lighter))
        binding.flArabicBannerBg.background = grad

        binding.tvHeaderTitle.setTextColor(primary)
        binding.btnPlayArabic.setTextColor(primary)
        binding.btnPlayArabic.backgroundTintList = ColorStateList.valueOf(Color.WHITE)

        binding.btnTranslate.setTextColor(primary)
        binding.btnTranslate.strokeColor = ColorStateList.valueOf(primary)
        binding.btnTranslate.backgroundTintList = ColorStateList.valueOf(theme.lightColor)

        binding.btnNextKalima.backgroundTintList = ColorStateList.valueOf(primary)
    }

    private fun displayCurrentKalima() {
        stopAndReleaseAudio()

        val kalima = kalimasList[currentIndex]
        binding.tvKalimaNumberName.text = "Kalima #${kalima.id}: ${kalima.name}"
        binding.tvArabicText.text = kalima.arabicText
        binding.tvTransliteration.text = kalima.transliteration

        binding.tvTranslationLabel.text = "English Translation:"
        binding.tvTranslationText.text = kalima.englishTranslation
        isUrduActive = false
        binding.btnTranslate.text = "اردو ترجمہ 🌐"

        updatePlayButtonUI(isPlaying = false)

        binding.btnPrevKalima.isEnabled = currentIndex > 0
        binding.btnPrevKalima.alpha = if (currentIndex > 0) 1.0f else 0.4f

        binding.btnNextKalima.isEnabled = currentIndex < kalimasList.size - 1
        binding.btnNextKalima.alpha = if (currentIndex < kalimasList.size - 1) 1.0f else 0.4f
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }

        // 🔊 Instant Audio Playback
        binding.btnPlayArabic.setOnSafeClickListener {
            val kalima = kalimasList[currentIndex]
            handleOfflineAudioPlayback(kalima.audioRes)
        }

        // 🌐 Toggle Translations
        binding.btnTranslate.setOnSafeClickListener {
            isUrduActive = !isUrduActive
            val kalima = kalimasList[currentIndex]

            voiceAssistant.stop()
            if (isUrduActive) {
                binding.tvTranslationLabel.text = "اردو ترجمہ (Urdu):"
                binding.tvTranslationText.text = kalima.urduTranslation
                binding.btnTranslate.text = "Switch to English 🌐"
                voiceAssistant.speakInLocale(kalima.urduTranslation, Locale("ur"))
            } else {
                binding.tvTranslationLabel.text = "English Translation:"
                binding.tvTranslationText.text = kalima.englishTranslation
                binding.btnTranslate.text = "اردو ترجمہ 🌐"
                voiceAssistant.speak(kalima.englishTranslation)
            }
        }

        binding.btnPrevKalima.setOnSafeClickListener {
            if (currentIndex > 0) {
                currentIndex--
                displayCurrentKalima()
            }
        }

        binding.btnNextKalima.setOnSafeClickListener {
            if (currentIndex < kalimasList.size - 1) {
                currentIndex++
                displayCurrentKalima()
            }
        }
    }

    // ==========================================
    // ⚡ INSTANT OFFLINE AUDIO PLAYBACK
    // ==========================================
    private fun handleOfflineAudioPlayback(@RawRes audioRes: Int) {
        voiceAssistant.stop()

        // 1. Pause if already playing
        if (isAudioPlaying && mediaPlayer != null) {
            mediaPlayer?.pause()
            isAudioPlaying = false
            updatePlayButtonUI(isPlaying = false)
            return
        }

        // 2. Resume if paused
        if (mediaPlayer != null) {
            mediaPlayer?.start()
            isAudioPlaying = true
            updatePlayButtonUI(isPlaying = true)
            return
        }

        // 3. Play immediately from local resources (Zero buffer delay!)
        try {
            stopAndReleaseAudio()

            mediaPlayer = MediaPlayer.create(this, audioRes)?.apply {
                setOnCompletionListener {
                    isAudioPlaying = false
                    updatePlayButtonUI(isPlaying = false)
                    voiceAssistant.speakSuccess()
                }
                start()
                isAudioPlaying = true
                updatePlayButtonUI(isPlaying = true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            updatePlayButtonUI(isPlaying = false)
        }
    }

    private fun updatePlayButtonUI(isPlaying: Boolean) {
        val theme = themeManager.getSelectedTheme()
        binding.btnPlayArabic.setTextColor(theme.primaryColor)

        if (isPlaying) {
            binding.btnPlayArabic.text = "⏸ Pause Recitation"
        } else {
            binding.btnPlayArabic.text = "▶ Listen Real Recitation"
        }
    }

    private fun stopAndReleaseAudio() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) stop()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
            isAudioPlaying = false
        }
    }

    override fun onPause() {
        stopAndReleaseAudio()
        voiceAssistant.stop()
        super.onPause()
    }

    override fun onDestroy() {
        stopAndReleaseAudio()
        musicManager.startMusic()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        // 🔇 Pause background music whenever this screen is active or re-opened
        musicManager.pauseMusic()
    }


    companion object {
        const val EXTRA_KALIMA_ID = "extra_kalima_id"
    }
}


