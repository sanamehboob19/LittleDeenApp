package com.ccx.islamic.kids.littledeenapp.ui.activities.splash


import com.ccx.islamic.kids.littledeenapp.R
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.lifecycleScope
import com.ccx.islamic.kids.littledeenapp.databinding.ActivitySplashNightBinding
import com.ccx.islamic.kids.littledeenapp.ui.activities.onboarding.OnboardingActivity
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.util.AppConstants
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.ccx.islamic.kids.littledeenapp.ui.activities.main.MainActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.onboarding.ChildNameActivity
import com.ccx.islamic.kids.littledeenapp.util.PreferenceHelper
import kotlinx.coroutines.Job



@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashNightActivity : BaseActivity<ActivitySplashNightBinding>(
    ActivitySplashNightBinding::inflate
) {
//
//    @Inject
//    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private var hasNavigated = false
    private var fallbackTimeoutJob: Job? = null

    override fun setupViews() {
        startGreetingAndTransition()
    }

    private fun startGreetingAndTransition() {
        // Safety timeout: If TTS fails to initialize or doesn't complete within 3.5s, proceed anyway
        fallbackTimeoutJob = lifecycleScope.launch {
            delay(1000L)
            navigateToNextScreen()
        }

        // Speaks immediately. onDone is called the exact millisecond speech finishes.
        // Speaks authentic Arabic Salam, followed by English welcome
//        voiceAssistant.speakSalamWelcome("Welcome to Little Deen!") {
//            runOnUiThread {
//                navigateToNextScreen()
//            }
//        }
    }

    private fun navigateToNextScreen() {
        if (hasNavigated) return
        hasNavigated = true

        fallbackTimeoutJob?.cancel()

        // 🎯 The Single Check:
        val targetActivity = if (preferenceHelper.isFirstLaunch) {
            OnboardingActivity::class.java   // New user: Go through setup
        } else {
            MainActivity::class.java         // Existing user: Jump straight to Home
        }

        startActivity(Intent(this, targetActivity))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onDestroy() {
        fallbackTimeoutJob?.cancel()
//        voiceAssistant.stop()
        super.onDestroy()
    }
}