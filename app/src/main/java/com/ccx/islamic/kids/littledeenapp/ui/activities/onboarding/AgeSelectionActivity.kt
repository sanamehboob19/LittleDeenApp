package com.ccx.islamic.kids.littledeenapp.ui.activities.onboarding

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ccx.islamic.kids.littledeenapp.R
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityAgeSelectionBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.gone
import com.ccx.islamic.kids.littledeenapp.extension.visible
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.util.AppConstants
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.net.Uri
import com.bumptech.glide.Glide
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.util.PreferenceHelper


@AndroidEntryPoint
class AgeSelectionActivity : BaseActivity<ActivityAgeSelectionBinding>(
    ActivityAgeSelectionBinding::inflate
)
{

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private var selectedAgeGroup = AppConstants.AGE_GROUP_6_TO_8

    override fun setupViews() {
        updateAudioIcon()
        loadSelectedChildAvatar()
        selectAgeOption(2) // Default to 6-8 Years
        setupClickListeners()

        // 🎯 If editing from settings, update button text
        if (!preferenceHelper.isFirstLaunch) {
            binding.btnContinue.text = "Save Changes"
        }
    }
    override fun onResume() {
        super.onResume()
        if (preferenceHelper.isTtsEnabled) {
            voiceAssistant.speak(getString(R.string.tts_age_selection))
        }
    }

    /**
     * Loads the child's chosen photo (or selected avatar icon) onto all 3 age cards
     */
    private fun loadSelectedChildAvatar() {
        val customUri = preferenceHelper.customAvatarUri
        val defaultAvatarRes = preferenceHelper.childAvatarRes.let {
            if (it != 0) it else R.drawable.avatar_boy_1
        }

        val imageSource: Any = if (!customUri.isNullOrEmpty()) {
            Uri.parse(customUri)
        } else {
            defaultAvatarRes
        }

        listOf(binding.ivBadge1, binding.ivBadge2, binding.ivBadge3).forEach { imageView ->
            Glide.with(this)
                .load(imageSource)
                .circleCrop()
                .placeholder(R.drawable.avatar_boy_1)
                .into(imageView)
        }
    }

    private fun setupClickListeners() {
        // Audio Narration Toggle
        binding.cardAudioToggle.setOnSafeClickListener {
            preferenceHelper.isTtsEnabled = !preferenceHelper.isTtsEnabled
            updateAudioIcon()

            if (preferenceHelper.isTtsEnabled) {
                voiceAssistant.speak(getString(R.string.tts_age_selection))
            } else {
                voiceAssistant.stop()
            }
        }

        // Age Group Cards
        binding.cardAgeOption1.setOnSafeClickListener { selectAgeOption(1) }
        binding.cardAgeOption2.setOnSafeClickListener { selectAgeOption(2) }
        binding.cardAgeOption3.setOnSafeClickListener { selectAgeOption(3) }

        // Continue Button
        binding.btnContinue.setOnSafeClickListener {
            preferenceHelper.childAgeGroup = selectedAgeGroup
            voiceAssistant.stop()

            // 🎯 Check mode
            if (preferenceHelper.isFirstLaunch) {
                navigateToThemeSelection()
            } else {
                showToast("Age group updated!")
                finish() // 👈 Returns straight back to Settings!
            }
        }

    }

    private fun selectAgeOption(optionIndex: Int) {
        val strokeSelected = ContextCompat.getColor(this, R.color.color_primary)
        val strokeUnselected = ContextCompat.getColor(this, R.color.color_border)
        val bgSelected = ContextCompat.getColor(this, R.color.color_primary_light)
        val bgUnselected = ContextCompat.getColor(this, R.color.color_surface)

        val strokeWidthSelected = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._2sdp)
        val strokeWidthUnselected = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._1sdp)

        // Reset all 3 cards
        val cards = listOf(binding.cardAgeOption1, binding.cardAgeOption2, binding.cardAgeOption3)
        val checks = listOf(binding.ivCheck1, binding.ivCheck2, binding.ivCheck3)

        cards.forEach {
            it.setCardBackgroundColor(bgUnselected)
            it.strokeColor = strokeUnselected
            it.strokeWidth = strokeWidthUnselected
        }
        checks.forEach { it.gone() }

        // Highlight selected card
        when (optionIndex) {
            1 -> {
                binding.cardAgeOption1.setCardBackgroundColor(bgSelected)
                binding.cardAgeOption1.strokeColor = strokeSelected
                binding.cardAgeOption1.strokeWidth = strokeWidthSelected
                binding.ivCheck1.visible()
                selectedAgeGroup = AppConstants.AGE_GROUP_2_TO_5
            }
            2 -> {
                binding.cardAgeOption2.setCardBackgroundColor(bgSelected)
                binding.cardAgeOption2.strokeColor = strokeSelected
                binding.cardAgeOption2.strokeWidth = strokeWidthSelected
                binding.ivCheck2.visible()
                selectedAgeGroup = AppConstants.AGE_GROUP_6_TO_8
            }
            3 -> {
                binding.cardAgeOption3.setCardBackgroundColor(bgSelected)
                binding.cardAgeOption3.strokeColor = strokeSelected
                binding.cardAgeOption3.strokeWidth = strokeWidthSelected
                binding.ivCheck3.visible()
                selectedAgeGroup = AppConstants.AGE_GROUP_9_TO_12
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

    private fun navigateToThemeSelection() {
        startActivity(Intent(this, ThemeSelectionActivity::class.java))
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









