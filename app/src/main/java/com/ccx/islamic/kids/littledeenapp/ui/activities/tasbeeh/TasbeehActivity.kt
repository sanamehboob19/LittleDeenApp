package com.ccx.islamic.kids.littledeenapp.ui.activities.tasbeeh


import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityTasbeehBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.data.TasbeehItem
import com.ccx.islamic.kids.littledeenapp.db.TasbeehDao
import com.ccx.islamic.kids.littledeenapp.db.TasbeehEntity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.ui.adapter.TasbeehChipAdapter
import com.ccx.islamic.kids.littledeenapp.util.PreferenceHelper
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class TasbeehActivity : BaseActivity<ActivityTasbeehBinding>(
    ActivityTasbeehBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var tasbeehDao: TasbeehDao

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private lateinit var chipAdapter: TasbeehChipAdapter
    private var currentActiveTasbeeh: TasbeehEntity? = null

    override fun setupViews() {
        applyTheme()
        updateAudioIcon()
        initializeDatabaseDefaults()
        setupChipSelector()
        setupClickListeners()
    }

    private fun applyTheme() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)
        binding.cardTapCircle.setCardBackgroundColor(theme.primaryColor)
    }

    private fun updateAudioIcon() {
        val iconRes = if (preferenceHelper.isTtsEnabled) R.drawable.ic_volume_up else R.drawable.ic_volume_off
        binding.ivAudioToggle.setImageResource(iconRes)
    }

    private fun initializeDatabaseDefaults() {
        lifecycleScope.launch {
            val existing = tasbeehDao.getAllTasbeeh().first()
            if (existing.isEmpty()) {
                TasbeehItem.getDefaultList().forEach { item ->
                    tasbeehDao.insertTasbeeh(
                        TasbeehEntity(
                            dhikrId = item.id,
                            arabicText = item.arabic,
                            englishText = item.english,
                            targetCount = item.target,
                            currentCount = 0
                        )
                    )
                }
            }
        }
    }

    private fun setupChipSelector() {
        chipAdapter = TasbeehChipAdapter(
            activeTheme = themeManager.getSelectedTheme()
        ) { selectedTasbeeh ->
            setActiveTasbeeh(selectedTasbeeh)

            // 🎯 Bilingual TTS on chip selection
            if (preferenceHelper.isTtsEnabled) {
                playBilingualDhikrAudio(selectedTasbeeh)
            }
        }

        binding.rvDhikrSelector.apply {
            layoutManager = LinearLayoutManager(this@TasbeehActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = chipAdapter
        }

        lifecycleScope.launch {
            tasbeehDao.getAllTasbeeh().collect { list ->
                chipAdapter.submitList(list)
                if (list.isNotEmpty() && currentActiveTasbeeh == null) {
                    setActiveTasbeeh(list[0])
                    chipAdapter.setSelected(list[0].dhikrId)
                    if (preferenceHelper.isTtsEnabled) {
                        playBilingualDhikrAudio(list[0])
                    }
                }
            }
        }
    }

    private fun playBilingualDhikrAudio(tasbeeh: TasbeehEntity) {
        voiceAssistant.stop()
        // 🎯 Speaks ONLY the Arabic text in authentic Arabic voice
        voiceAssistant.speakInLocale(tasbeeh.arabicText, Locale("ar"))
    }

    private fun setActiveTasbeeh(tasbeeh: TasbeehEntity) {
        currentActiveTasbeeh = tasbeeh
        binding.tvDhikrArabic.text = tasbeeh.arabicText
        binding.tvDhikrEnglish.text = tasbeeh.englishText
        binding.tvCounterNumber.text = "${tasbeeh.currentCount}"
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }

        // Audio Toggle Switch
        binding.cardAudioToggle.setOnSafeClickListener {
            preferenceHelper.isTtsEnabled = !preferenceHelper.isTtsEnabled
            updateAudioIcon()
            if (!preferenceHelper.isTtsEnabled) voiceAssistant.stop()
            else currentActiveTasbeeh?.let { playBilingualDhikrAudio(it) }
        }

        binding.btnResetCounter.setOnSafeClickListener {
            currentActiveTasbeeh?.let { active ->
                lifecycleScope.launch {
                    tasbeehDao.resetCount(active.dhikrId)
                    currentActiveTasbeeh = active.copy(currentCount = 0)
                    binding.tvCounterNumber.text = "0"
                    showToast("Counter reset")
                }
            }
        }

        // Giant Tap Circle
        binding.flCounterButton.setOnSafeClickListener {
            currentActiveTasbeeh?.let { active ->
                val newCount = active.currentCount + 1
                currentActiveTasbeeh = active.copy(currentCount = newCount)
                binding.tvCounterNumber.text = "$newCount"

                lifecycleScope.launch {
                    tasbeehDao.updateCount(active.dhikrId, newCount)
                }

                if (newCount == active.targetCount) {
                    voiceAssistant.speakSuccess()
                    showToast("Masha'Allah! Target reached! 🎉")
                    YoYo.with(Techniques.Bounce).duration(800).playOn(binding.cardDhikrDisplay)
                }

                binding.cardTapCircle.animate().scaleX(0.92f).scaleY(0.92f).setDuration(50).withEndAction {
                    binding.cardTapCircle.animate().scaleX(1.0f).scaleY(1.0f).setDuration(50).start()
                }.start()
            }
        }
    }

    override fun onPause() {
        voiceAssistant.stop()
        super.onPause()
    }
}











