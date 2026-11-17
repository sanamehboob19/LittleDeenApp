package com.ccx.islamic.kids.littledeenapp.ui.fragment


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.core.graphics.ColorUtils
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.FragmentLearningBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.ui.activities.kalma.KalimasActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.namaz.SalahGuideActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.stories.StoriesActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.surah.SurahsActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.tasbeeh.TasbeehActivity
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.jvm.java

@AndroidEntryPoint
class LearningFragment : BaseFragment<FragmentLearningBinding>(FragmentLearningBinding::inflate) {

    @Inject
    lateinit var themeManager: ThemeManager

    override fun setupViews() {
        applyThemeStyling()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        applyThemeStyling()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        val primaryColor = theme.primaryColor

        // 🎯 Dynamically generate matching gradient for the Top Learning Hero Banner
        val lighterShade = ColorUtils.blendARGB(primaryColor, Color.WHITE, 0.25f)
        val dynamicGradient = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(primaryColor, lighterShade)
        )
        binding.flLearningBannerBg.background = dynamicGradient
        binding.cardLearningHeroBanner.strokeColor = theme.lightColor
    }

    private fun setupClickListeners() {
        // 1. Kids Digital Tasbeeh Card
        binding.cardLearningTasbeeh.setOnSafeClickListener {
             startActivity(Intent(requireContext(), TasbeehActivity::class.java))
        }

        // 2. The 6 Kalimas Card
        binding.cardLearningKalimas.setOnSafeClickListener {
             startActivity(Intent(requireContext(), KalimasActivity::class.java))
        }

        // 3. Short Surahs & 4 Quls Card
        binding.cardLearningSurahs.setOnSafeClickListener {
             startActivity(Intent(requireContext(), SurahsActivity::class.java))
        }

        // 4. Learn Salah (Prayer) Card
        binding.cardLearningSalah.setOnSafeClickListener {
             startActivity(Intent(requireContext(), SalahGuideActivity::class.java))
        }

        // 5. Prophet & Moral Stories Card
        binding.cardLearningStories.setOnSafeClickListener {
             startActivity(Intent(requireContext(), StoriesActivity::class.java))
        }
    }
}