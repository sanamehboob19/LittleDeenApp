package com.ccx.islamic.kids.littledeenapp.ui.activities.surah

import android.content.Context
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
import com.ccx.islamic.kids.littledeenapp.data.SurahItem
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivitySurahDetailBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.ccx.islamic.kids.littledeenapp.util.MusicManager



@AndroidEntryPoint
class SurahDetailActivity : BaseActivity<ActivitySurahDetailBinding>(
    ActivitySurahDetailBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var musicManager: MusicManager

    private lateinit var surahsList: List<SurahItem>
    private var currentIndex = 0
    private var isUrduActive = false

    // 🎵 Real Quran Audio Player
    private var mediaPlayer: MediaPlayer? = null
    private var isAudioPlaying = false
    private var isAudioPrepared = false

    override fun setupViews() {

        surahsList = SurahItem.getAllSurahs()
        val targetId = intent.getIntExtra(EXTRA_SURAH_ID, 1)
        currentIndex = surahsList.indexOfFirst { it.id == targetId }.coerceAtLeast(0)

        applyTheme()
        displayCurrentSurah()
        setupClickListeners()
    }

    private fun applyTheme() {
        val theme = themeManager.getSelectedTheme()
        val primary = theme.primaryColor

        // Header & Title
        binding.tvHeaderTitle.setTextColor(primary)
        binding.tvSurahNumberName.setTextColor(primary)

        // Dynamic Gradient for Arabic Card
        val lighter = ColorUtils.blendARGB(primary, Color.WHITE, 0.25f)
        val grad = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(primary, lighter))
        binding.flArabicBannerBg.background = grad

        // Play Button Styling
        binding.btnPlayArabic.setTextColor(primary)
        binding.btnPlayArabic.backgroundTintList = ColorStateList.valueOf(Color.WHITE)

        // Translation Button Styling
        binding.btnTranslate.setTextColor(primary)
        binding.btnTranslate.strokeColor = ColorStateList.valueOf(primary)
        binding.btnTranslate.backgroundTintList = ColorStateList.valueOf(theme.lightColor)

        // Navigation Next Button
        binding.btnNextSurah.backgroundTintList = ColorStateList.valueOf(primary)
    }

    private fun displayCurrentSurah() {
        // Stop any previous playing audio
        stopAndReleaseAudio()

        val surah = surahsList[currentIndex]
        binding.tvSurahNumberName.text = "${surah.name} (${surah.arabicName}) - ${surah.meaning}"
        binding.tvArabicText.text = surah.arabicText
        binding.tvTransliteration.text = surah.transliteration

        binding.tvTranslationLabel.text = "English Translation:"
        binding.tvTranslationText.text = surah.englishTranslation
        isUrduActive = false
        binding.btnTranslate.text = "اردو ترجمہ 🌐"

        // Reset play button icon & text
        updatePlayButtonUI(isPlaying = false, isLoading = false)

        // Update Prev / Next buttons opacity
        binding.btnPrevSurah.isEnabled = currentIndex > 0
        binding.btnPrevSurah.alpha = if (currentIndex > 0) 1.0f else 0.4f

        binding.btnNextSurah.isEnabled = currentIndex < surahsList.size - 1
        binding.btnNextSurah.alpha = if (currentIndex < surahsList.size - 1) 1.0f else 0.4f
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }

        // 🔊 Play / Pause REAL Recorded Quran Tilawat
        // 🔊 Play / Pause Button Click
        binding.btnPlayArabic.setOnSafeClickListener {
            val surah = surahsList[currentIndex]
            handleAudioPlayback(surah.audioUrl, surah.arabicText)
        }

        // 🌐 Toggle Translation (Urdu <-> English) with Speech Support
        binding.btnTranslate.setOnSafeClickListener {
            isUrduActive = !isUrduActive
            val surah = surahsList[currentIndex]

            voiceAssistant.stop()
            if (isUrduActive) {
                binding.tvTranslationLabel.text = "اردو ترجمہ (Urdu):"
                binding.tvTranslationText.text = surah.urduTranslation
                binding.btnTranslate.text = "Switch to English 🌐"
                voiceAssistant.speakInLocale(surah.urduTranslation, Locale("ur"))
            } else {
                binding.tvTranslationLabel.text = "English Translation:"
                binding.tvTranslationText.text = surah.englishTranslation
                binding.btnTranslate.text = "اردو ترجمہ 🌐"
                voiceAssistant.speak(surah.englishTranslation)
            }
        }

        binding.btnPrevSurah.setOnSafeClickListener {
            if (currentIndex > 0) {
                currentIndex--
                displayCurrentSurah()
            }
        }

        binding.btnNextSurah.setOnSafeClickListener {
            if (currentIndex < surahsList.size - 1) {
                currentIndex++
                displayCurrentSurah()
            }
        }
    }

    // ==========================================
    // 🎧 REAL AUDIO PLAYBACK ENGINE (MediaPlayer)
    // ==========================================
    private fun handleAudioPlayback(audioUrl: String, arabicText: String) {
        voiceAssistant.stop()

        // 1. If currently playing, pause it
        if (isAudioPlaying && mediaPlayer != null) {
            mediaPlayer?.pause()
            isAudioPlaying = false
            updatePlayButtonUI(isPlaying = false, isLoading = false)
            return
        }

        // 2. If already prepared and paused, resume
        if (isAudioPrepared && mediaPlayer != null) {
            mediaPlayer?.start()
            isAudioPlaying = true
            updatePlayButtonUI(isPlaying = true, isLoading = false)
            return
        }

        // 3. 🌐 Check Internet Connection Before Streaming
        if (!isNetworkAvailable()) {
            // Show clear message to user
            Toast.makeText(this, "No internet connection! 📡 Playing offline voice...", Toast.LENGTH_SHORT).show()

            // Update button text to reflect offline recitation
            binding.btnPlayArabic.text = "⏸ Playing Offline..."

            // Recite offline using VoiceAssistant TTS in Arabic
            voiceAssistant.setSpeechRate(0.80f)
            voiceAssistant.speakInLocale(arabicText, Locale("ar")) {
                voiceAssistant.setSpeechRate(0.92f)
                updatePlayButtonUI(isPlaying = false, isLoading = false)
            }
            return
        }

        // 4. Online: Stream authentic recorded Tilawat via MediaPlayer
        updatePlayButtonUI(isPlaying = false, isLoading = true)

        try {
            stopAndReleaseAudio()

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(audioUrl)

                setOnPreparedListener { mp ->
                    isAudioPrepared = true
                    isAudioPlaying = true
                    mp.start()
                    updatePlayButtonUI(isPlaying = true, isLoading = false)
                }

                setOnCompletionListener {
                    isAudioPlaying = false
                    isAudioPrepared = false
                    updatePlayButtonUI(isPlaying = false, isLoading = false)
                    voiceAssistant.speakSuccess()
                }

                setOnErrorListener { _, _, _ ->
                    updatePlayButtonUI(isPlaying = false, isLoading = false)
                    Toast.makeText(this@SurahDetailActivity, "Unable to stream audio. Check your connection! 📡", Toast.LENGTH_SHORT).show()
                    true
                }

                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            updatePlayButtonUI(isPlaying = false, isLoading = false)
        }
    }

    private fun updatePlayButtonUI(isPlaying: Boolean, isLoading: Boolean) {
        val theme = themeManager.getSelectedTheme()
        binding.btnPlayArabic.setTextColor(theme.primaryColor)

        if (isLoading) {
            binding.btnPlayArabic.text = "⏳ Loading Recitation..."
        } else if (isPlaying) {
            binding.btnPlayArabic.text = "⏸ Pause Recitation"
        } else {
            binding.btnPlayArabic.text = "▶ Listen Real Recitation"
        }
    }

    private fun stopAndReleaseAudio() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) stop()
                reset()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
            isAudioPlaying = false
            isAudioPrepared = false
        }
    }

    override fun onPause() {
        stopAndReleaseAudio()
        voiceAssistant.stop()
        super.onPause()
    }

    override fun onDestroy() {
        stopAndReleaseAudio()
        // Resume background music when leaving Surah screen
        musicManager.startMusic()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        // 🔇 Pause background music whenever this screen is active or re-opened
        musicManager.pauseMusic()
    }


    companion object {
        const val EXTRA_SURAH_ID = "extra_surah_id"
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}