package com.ccx.islamic.kids.littledeenapp.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.view.View
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.ccx.islamic.kids.littledeenapp.R
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.FragmentHomeBinding
import com.ccx.islamic.kids.littledeenapp.db.StreakDao
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.ui.activities.drawingPad.DrawPadActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.main.MainActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.playlist.PlaylistsActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.settings.ParentsSettingsActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoHub.VideosHubActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.CategoriesActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.FavoritesActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.HistoryActivity
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseFragment
import com.ccx.islamic.kids.littledeenapp.ui.dialog.ParentGateDialogFragment
import com.ccx.islamic.kids.littledeenapp.util.PreferenceHelper
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var themeManager: ThemeManager

    @Inject
    lateinit var voiceAssistant: VoiceAssistant
    @Inject
    lateinit var streakDao: StreakDao

    override fun setupViews() {
        loadChildProfile()
        applyThemeStyling()
        playEntryAnimations()
        setupClickListeners()
        observeStreak()

        // Enable smooth scrolling text on the top welcome pill
        binding.tvMarqueeWelcome.isSelected = true
    }

    private fun observeStreak() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                streakDao.getStreakFlow().collect { streak ->
                    val days = streak?.currentStreak ?: 1
                    binding.tvStreakCount.text = "$days Days"
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 🎯 Re-apply theme styling when user returns from settings
        applyThemeStyling()
        loadChildProfile()
    }

    /**
     * Loads the child's custom photo or default avatar into the big spotlight
     */
    private fun loadChildProfile() {
        val childName = preferenceHelper.childName
        binding.tvWelcomeChild.text = "Welcome, $childName! 🌞"
        binding.tvMarqueeWelcome.text = "✨ Assalamu Alaikum, $childName! Ready to learn, pray, and grow? ✨"

        val customUri = preferenceHelper.customAvatarUri
        val defaultAvatarRes = preferenceHelper.childAvatarRes.let {
            if (it != 0) it else R.drawable.avatar_boy_1
        }

        val imageSource: Any = if (!customUri.isNullOrEmpty()) {
            Uri.parse(customUri)
        } else {
            defaultAvatarRes
        }

        Glide.with(this)
            .load(imageSource)
            .circleCrop()
            .placeholder(R.drawable.avatar_boy_1)
            .error(R.drawable.avatar_boy_1)
            .into(binding.ivChildAvatar)
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()

        // 1. Tint Avatar Ring & Greeting Elements
        binding.cardAvatarRing.strokeColor = theme.primaryColor
        binding.cardWelcomePill.setCardBackgroundColor(theme.lightColor)
        binding.tvMarqueeWelcome.setTextColor(theme.primaryColor)
        binding.tvGreetingSalam.setTextColor(theme.primaryColor)

        // 2. 🎯 Dynamically generate matching gradient for the Video Banner
        val lighterShade = ColorUtils.blendARGB(theme.primaryColor, Color.WHITE, 0.25f)
        val dynamicGradient = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(theme.primaryColor, lighterShade)
        )
        binding.flVideoBannerBg.background = dynamicGradient
        binding.cardFeaturedVideosBanner.strokeColor = theme.lightColor
    }
    private fun playEntryAnimations() {
        // Zoom-in / Pop entry for the hero spotlight avatar
        YoYo.with(Techniques.Landing)
            .duration(800)
            .playOn(binding.cardAvatarRing)

        // Playful star wiggle
        YoYo.with(Techniques.Swing)
            .duration(1200)
            .repeat(1)
            .playOn(binding.tvFloatingEmoji)

        // Streak Card pulse
        YoYo.with(Techniques.Pulse)
            .duration(1000)
            .playOn(binding.cardStreak)
    }

    private fun setupClickListeners() {
        // ---------------------------------------------
        // 🌟 Interactive Avatar & Streak
        // ---------------------------------------------
        binding.flAvatarContainer.setOnSafeClickListener {
            onAvatarTapped()
        }

//        binding.cardStreak.setOnSafeClickListener {
//            startActivity(Intent(requireContext(), StreakDetailsActivity::class.java))
//        }

        // ---------------------------------------------
        // 🛡️ Parents Corner (Protected by Gate Dialog)
        // ---------------------------------------------
        binding.cardParentsCorner.setOnSafeClickListener {
            val gateDialog = ParentGateDialogFragment.newInstance {
                startActivity(Intent(requireContext(), ParentsSettingsActivity::class.java))
            }
            gateDialog.show(parentFragmentManager, ParentGateDialogFragment.TAG)
        }

        // ---------------------------------------------
        // ▶️ Featured Videos Banner -> Switches to Videos Tab (Index 2)
        // ---------------------------------------------
        binding.cardFeaturedVideosBanner.setOnSafeClickListener {
            navigateToMainTab(2)
        }

        // ---------------------------------------------
        // 📂 Row 1: Custom Playlists & Favorites Activities
        // ---------------------------------------------
        binding.cardHomePlaylists.setOnSafeClickListener {
            startActivity(Intent(requireContext(), PlaylistsActivity::class.java))
        }

        binding.cardHomeFavorites.setOnSafeClickListener {
            startActivity(Intent(requireContext(), FavoritesActivity::class.java))
        }

        // ---------------------------------------------
        // 🕒 Row 2: History & Categories Activities
        // ---------------------------------------------
        binding.cardHomeHistory.setOnSafeClickListener {
            startActivity(Intent(requireContext(), HistoryActivity::class.java))
        }

        // 4. Categories Card on Home -> Opens VideosHubActivity
        binding.cardHomeCategories.setOnSafeClickListener {
            startActivity(Intent(requireContext(), VideosHubActivity::class.java))
        }

        // ---------------------------------------------
        // 🎨 Row 3: Drawing, Duas & Games
        // ---------------------------------------------
        // Drawing Pad: Shows temporary toast
        binding.cardHomeDrawing.setOnSafeClickListener {
            val intent = Intent(requireContext(), DrawPadActivity::class.java)
            startActivity(intent)
        }

        // Daily Duas: Switches to Duas Tab (Index 3)
        binding.cardHomeDuas.setOnSafeClickListener {
            navigateToMainTab(3)
        }

        // Kids Games: Switches to Games Tab (Index 4)
        binding.cardHomeGames.setOnSafeClickListener {
            navigateToMainTab(4)
        }
    }

    /**
     * Helper to cleanly switch tabs in MainActivity's ViewPager2
     */
    private fun navigateToMainTab(tabIndex: Int) {
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.findViewById<ViewPager2>(R.id.viewPagerMain)?.currentItem = tabIndex
    }

    /**
     * Triggered every time the child taps their avatar.
     * Animates and plays a randomized, joyful Islamic audio phrase.
     */
    private fun onAvatarTapped() {
        val name = preferenceHelper.childName

        // Multi-layer visual delight
        YoYo.with(Techniques.Tada)
            .duration(650)
            .playOn(binding.cardAvatarRing)

        YoYo.with(Techniques.RubberBand)
            .duration(650)
            .playOn(binding.tvFloatingEmoji)

        // Rich variety of motivating Islamic phrases for kids
        val playfulPhrases = listOf(
            "As-salaamu alaykum, $name! Let's explore together!",
            "Masha'Allah, $name! You shine bright like a star!",
            "Bismillah, $name! What are we learning today?",
            "Alhamdulillah! It is such a blessed day with you, $name!",
            "SubhanAllah, $name! Look at that bright, beautiful smile!",
            "You are our little Deen hero, $name! Keep learning!",
            "Ready for another great adventure, $name? Let's go!",
            "Allah loves kind and caring kids like you, $name!",
            "High five, $name! Ready to hear an inspiring story?",
            "Super job visiting today, $name! Always remember to say Bismillah!",
            "Keep your daily streak going, $name! You are doing amazing!",
            "Duas bring peace to our hearts, $name! Let's make a sweet Dua!",
            "Smile, $name! Smiling is a Sunnah of our Prophet!",
            "Let's be kind, helpful, and sharing today, $name!",
            "Masha'Allah, $name! You make learning so much fun!",
            "Allah is always with us, $name! Have a wonderful day!"
        )

        voiceAssistant.speak(playfulPhrases.random())
    }

    override fun onPause() {
        voiceAssistant.stop()
        super.onPause()
    }
}