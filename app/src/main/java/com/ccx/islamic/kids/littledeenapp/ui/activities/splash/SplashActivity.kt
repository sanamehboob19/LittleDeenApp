package com.ccx.islamic.kids.littledeenapp.ui.activities.splash


import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.ccx.islamic.kids.littledeenapp.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity :
    BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun setupViews() {
        lifecycleScope.launch {
            delay(1300L) // 1.0 second duration
            navigateToSplashNight()
        }
    }

    private fun navigateToSplashNight() {
        startActivity(Intent(this, SplashNightActivity::class.java))
        // Smooth crossfade transition
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}


