package com.ccx.islamic.kids.littledeenapp.ui.activities.main



import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityMainBinding
import com.ccx.islamic.kids.littledeenapp.db.StreakDao
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.activities.settings.ParentsSettingsActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.SearchActivity
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.ui.dialog.ExitAppDialogFragment
import com.ccx.islamic.kids.littledeenapp.ui.dialog.ParentGateDialogFragment
import com.ccx.islamic.kids.littledeenapp.ui.dialog.StreakPopupDialogFragment
import com.ccx.islamic.kids.littledeenapp.ui.fragment.DuasFragment
import com.ccx.islamic.kids.littledeenapp.ui.fragment.GamesFragment
import com.ccx.islamic.kids.littledeenapp.ui.fragment.HomeFragment
import com.ccx.islamic.kids.littledeenapp.ui.fragment.LearningFragment
import com.ccx.islamic.kids.littledeenapp.ui.fragment.VideosFragment

import com.ccx.islamic.kids.littledeenapp.util.MusicManager
import com.ccx.islamic.kids.littledeenapp.util.PreferenceHelper
import com.ccx.islamic.kids.littledeenapp.util.StreakManager
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject





@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate) {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var musicManager: MusicManager

    @Inject
    lateinit var voiceAssistant: VoiceAssistant

    @Inject
    lateinit var streakDao: StreakDao

    @Inject
    lateinit var streakManager: StreakManager

    private var videoButtonAnimatorSet: AnimatorSet? = null

    override fun setupViews() {
        applyThemeStyling()
        loadChildAvatar()
        setupViewPager()
        setupBottomNavClicks()
        setupHeaderActions()
        setupBackPressHandling()
        startContinuousVideoAnimation()

        // 🎯 Check and show Streak Dialog on app launch
//        checkAndShowStreakDialog()
    }

//    private fun checkAndShowStreakDialog() {
//        lifecycleScope.launch {
//            val streakData = streakDao.getStreakSync()
//            val currentStreak = if (streakData != null && streakData.currentStreak > 0) {
//                streakData.currentStreak
//            } else {
//                1 // Default starting streak
//            }
//
////            val streakDialog = StreakPopupDialogFragment.newInstance(days = currentStreak)
////            streakDialog.show(supportFragmentManager, StreakPopupDialogFragment.TAG)
///
//    }

    override fun onResume() {
        super.onResume()
        // 🎯 1. Re-apply theme styling to MainActivity views (title, buttons, bottom nav)
        applyThemeStyling()

        // 🎯 2. Refresh child profile avatar & music icon
        loadChildAvatar()
        updateMusicIcon()

        // 🎯 3. Refresh ViewPager2 fragments so HomeFragment, DuasFragment, etc. re-tint immediately
        binding.viewPagerMain.adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        videoButtonAnimatorSet?.cancel()
        videoButtonAnimatorSet = null
        super.onDestroy()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        val primaryColor = theme.primaryColor
        val colorStateList = ColorStateList.valueOf(primaryColor)

        binding.tvAppName.setTextColor(primaryColor)

        // 🎯 Tint icons with the selected theme color
        binding.ivSettings.imageTintList = colorStateList
        binding.ivMusicToggle.imageTintList = colorStateList
        binding.btnCenterVideos.backgroundTintList = colorStateList

        // 🎯 Tint card borders with the theme's soft accent color
        binding.cardSearch.strokeColor = theme.lightColor
        binding.cardSettings.strokeColor = theme.lightColor
        binding.cardMusicToggle.strokeColor = theme.lightColor

        updateBottomNavSelection(0)
    }

    private fun setupViewPager() {
        binding.viewPagerMain.apply {
            isUserInputEnabled = false
            offscreenPageLimit = 4
            adapter = MainPagerAdapter(this@MainActivity)
        }

        binding.viewPagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateBottomNavSelection(position)
            }
        })
    }

    private fun setupBottomNavClicks() {
        binding.tabHome.setOnSafeClickListener { binding.viewPagerMain.currentItem = 0 }
        binding.tabStories.setOnSafeClickListener { binding.viewPagerMain.currentItem = 1 }
        // 🎯 Tapping the Videos tab counts toward the daily streak!
        binding.btnCenterVideos.setOnSafeClickListener {
            binding.viewPagerMain.currentItem = 2

            // Increment or record streak in background
            lifecycleScope.launch {
                streakManager.recordLearningActivityComplete()
            }
        }
        binding.tabDuas.setOnSafeClickListener { binding.viewPagerMain.currentItem = 3 }
        binding.tabGames.setOnSafeClickListener { binding.viewPagerMain.currentItem = 4 }
    }

    private fun updateBottomNavSelection(position: Int) {
        val primaryColor = themeManager.getSelectedTheme().primaryColor
        val inactiveColor = ContextCompat.getColor(this, R.color.color_text_secondary)

        binding.tvNavHome.setTextColor(if (position == 0) primaryColor else inactiveColor)
        binding.tvNavStories.setTextColor(if (position == 1) primaryColor else inactiveColor)
        binding.tvNavDuas.setTextColor(if (position == 3) primaryColor else inactiveColor)
        binding.tvNavGames.setTextColor(if (position == 4) primaryColor else inactiveColor)

        if (position == 2) {
            binding.btnCenterVideos.animate().scaleX(1.15f).scaleY(1.15f).setDuration(150).start()
        } else {
            binding.btnCenterVideos.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
        }
    }

    private fun setupHeaderActions() {
        // 🔍 Search Click -> Opens SearchActivity
        binding.cardSearch.setOnSafeClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        // ⚙️ Settings Click -> Protected by Parental Gate before opening ParentsSettingsActivity
        binding.cardSettings.setOnSafeClickListener {
            val gateDialog = ParentGateDialogFragment.newInstance {
                startActivity(Intent(this, ParentsSettingsActivity::class.java))
            }
            gateDialog.show(supportFragmentManager, ParentGateDialogFragment.TAG)
        }

        // Music On/Off Toggle
        binding.cardMusicToggle.setOnSafeClickListener {
            val newMusicState = !preferenceHelper.isBgMusicEnabled
            preferenceHelper.isBgMusicEnabled = newMusicState
            musicManager.setMusicEnabled(newMusicState)
            updateMusicIcon()
        }

        // Exit App Button
        binding.cardExitApp.setOnSafeClickListener {
            showExitDialog()
        }
    }

    private fun updateMusicIcon() {
        val iconRes = if (preferenceHelper.isBgMusicEnabled) {
            R.drawable.ic_volume_up
        } else {
            R.drawable.ic_volume_off
        }
        binding.ivMusicToggle.setImageResource(iconRes)
    }

    private fun setupBackPressHandling() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.viewPagerMain.currentItem != 0) {
                    binding.viewPagerMain.currentItem = 0
                } else {
                    showExitDialog()
                }
            }
        })
    }

    private fun showExitDialog() {
        val exitDialog = ExitAppDialogFragment.newInstance {
            finishAffinity()
        }
        exitDialog.show(supportFragmentManager, ExitAppDialogFragment.TAG)
    }

    private fun loadChildAvatar() {
        val customUri = preferenceHelper.customAvatarUri
        val defaultAvatarRes = preferenceHelper.childAvatarRes.let {
            if (it != 0) it else R.drawable.avatar_boy_1
        }

        val imageSource: Any = if (!customUri.isNullOrEmpty()) {
            android.net.Uri.parse(customUri)
        } else {
            defaultAvatarRes
        }

        Glide.with(this)
            .load(imageSource)
            .circleCrop()
            .placeholder(R.drawable.avatar_boy_1)
            .error(R.drawable.avatar_boy_1)
            .into(binding.ivAppLogo)

        val primaryColor = themeManager.getSelectedTheme().primaryColor
        binding.cardTopAvatar.strokeColor = primaryColor
    }

    private fun startContinuousVideoAnimation() {
        val scaleX = ObjectAnimator.ofFloat(binding.btnCenterVideos, "scaleX", 1.0f, 1.10f).apply {
            duration = 900
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }

        val scaleY = ObjectAnimator.ofFloat(binding.btnCenterVideos, "scaleY", 1.0f, 1.10f).apply {
            duration = 900
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }

        videoButtonAnimatorSet = AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            start()
        }
    }

    inner class MainPagerAdapter(activity: MainActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 5

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> LearningFragment()
                2 -> VideosFragment()
                3 -> DuasFragment()
                else -> GamesFragment()
            }
        }
    }
}