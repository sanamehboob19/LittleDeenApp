package com.ccx.islamic.kids.littledeenapp.ui.activities.onboarding


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.ccx.islamic.kids.littledeenapp.R
import androidx.viewpager2.widget.ViewPager2
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityOnboardingBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.model.OnboardingSlide
import com.ccx.islamic.kids.littledeenapp.ui.adapter.OnboardingAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.util.PreferenceHelper
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>(
    ActivityOnboardingBinding::inflate
) {

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var slides: List<OnboardingSlide>
    private var buttonPulseAnimatorSet: AnimatorSet? = null

    override fun setupViews() {
        applyThemeStyling()
        initSlidesData()
        setupViewPager()
        updateAudioToggleIcon()
        setupClickListeners()
        startContinuousButtonPulse()
    }

    override fun onResume() {
        super.onResume()
        speakCurrentSlide()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.btnAction.backgroundTintList = ColorStateList.valueOf(theme.primaryColor)
        binding.dotsIndicator.selectedDotColor = theme.primaryColor
    }

    private fun initSlidesData() {
        slides = listOf(
            OnboardingSlide(
                titleRes = R.string.onboarding_title_1,
                descRes = R.string.onboarding_desc_1,
                ttsRes = R.string.tts_onboarding_1,
                imageRes = R.drawable.img_onboarding_1
            ),
            OnboardingSlide(
                titleRes = R.string.onboarding_title_2,
                descRes = R.string.onboarding_desc_2,
                ttsRes = R.string.tts_onboarding_2,
                imageRes = R.drawable.img_onboarding_2
            ),
            OnboardingSlide(
                titleRes = R.string.onboarding_title_3,
                descRes = R.string.onboarding_desc_3,
                ttsRes = R.string.tts_onboarding_3,
                imageRes = R.drawable.img_onboarding_3
            ),
            OnboardingSlide(
                titleRes = R.string.onboarding_title_4,
                descRes = R.string.onboarding_desc_4,
                ttsRes = R.string.tts_onboarding_4,
                imageRes = R.drawable.img_onboarding_4
            )
        )
    }

    private fun setupViewPager() {
        val adapter = OnboardingAdapter(slides)
        binding.viewPagerOnboarding.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPagerOnboarding)

        binding.viewPagerOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val isLastPage = position == slides.size - 1
                binding.btnAction.text = getString(
                    if (isLastPage) R.string.btn_get_started else R.string.btn_next
                )

                speakCurrentSlide()
            }
        })
    }

    private fun speakCurrentSlide() {
        voiceAssistant.stop()
        if (preferenceHelper.isTtsEnabled) {
            val currentPos = binding.viewPagerOnboarding.currentItem
            voiceAssistant.speak(getString(slides[currentPos].ttsRes))
        }
    }

    private fun setupClickListeners() {
        binding.cardAudioToggle.setOnSafeClickListener {
            preferenceHelper.isTtsEnabled = !preferenceHelper.isTtsEnabled
            updateAudioToggleIcon()

            if (preferenceHelper.isTtsEnabled) {
                speakCurrentSlide()
            } else {
                voiceAssistant.stop()
            }
        }

        binding.btnAction.setOnSafeClickListener {
            val current = binding.viewPagerOnboarding.currentItem
            if (current < slides.size - 1) {
                binding.viewPagerOnboarding.currentItem = current + 1
            } else {
                navigateToChildSetup()
            }
        }
    }

    // 🎯 Gentle, continuous heartbeat animation on the Next button
    private fun startContinuousButtonPulse() {
        val scaleX = ObjectAnimator.ofFloat(binding.btnAction, "scaleX", 1.0f, 1.05f).apply {
            duration = 900
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val scaleY = ObjectAnimator.ofFloat(binding.btnAction, "scaleY", 1.0f, 1.05f).apply {
            duration = 900
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }

        buttonPulseAnimatorSet = AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            start()
        }
    }

    private fun updateAudioToggleIcon() {
        val iconRes = if (preferenceHelper.isTtsEnabled) {
            R.drawable.ic_volume_up
        } else {
            R.drawable.ic_volume_off
        }
        binding.ivAudioToggle.setImageResource(iconRes)
    }

    private fun navigateToChildSetup() {
        buttonPulseAnimatorSet?.cancel()
        voiceAssistant.stop()
        startActivity(Intent(this, ChildNameActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onPause() {
        voiceAssistant.stop()
        super.onPause()
    }

    override fun onDestroy() {
        buttonPulseAnimatorSet?.cancel()
        buttonPulseAnimatorSet = null
        voiceAssistant.stop()
        super.onDestroy()
    }
}






