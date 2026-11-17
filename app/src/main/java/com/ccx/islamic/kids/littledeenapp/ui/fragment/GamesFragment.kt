package com.ccx.islamic.kids.littledeenapp.ui.fragment


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.FragmentGamesBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.activities.drawingPad.DrawPadActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.drawingPad.NeonPadActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.pixel_art.PixelSelectionActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.tracing.AlphabetSelectionActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.tracing.NumberSelectionActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.tracing.ShapeSelectionActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.tracing.UrduSelectionActivity
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GamesFragment : BaseFragment<FragmentGamesBinding>(
    FragmentGamesBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    override fun setupViews() {
        applyThemeStyling()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        applyThemeStyling() // Ensures theme is updated if parent activity changes it
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        val primaryColor = theme.primaryColor

        // 🎯 Dynamically generate matching gradient for the Games Hero Banner
        val lighterShade = ColorUtils.blendARGB(primaryColor, Color.WHITE, 0.25f)
        val dynamicGradient = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(primaryColor, lighterShade)
        )

        // Apply the dynamic gradient to the background FrameLayout
        binding.flGamesBannerBg.background = dynamicGradient

        // Match the border of the Hero Card to the theme's light color
        binding.cardGamesHero.strokeColor = theme.lightColor
        binding.cardGamesHero.strokeWidth = 2
    }

    private fun setupClickListeners() {
        // Pixel Art
        // Pixel Art
        binding.cardPixelArt.setOnSafeClickListener {
            val intent = Intent(requireContext(), PixelSelectionActivity::class.java)
            startActivity(intent)
        }

        // ABC Tracing
        binding.cardAbcTracing.setOnSafeClickListener {
            startActivity(Intent(requireContext(), AlphabetSelectionActivity::class.java))
        }

        // 123 Tracing
        binding.card123Tracing.setOnSafeClickListener {
            startActivity(Intent(requireContext(), NumberSelectionActivity::class.java))
        }

        // Magic Coloring
        binding.cardShapesTracing.setOnSafeClickListener {
            val intent = Intent(requireContext(), ShapeSelectionActivity::class.java)
            startActivity(intent)
        }

        // Drawing Pad
        binding.cardDrawingPad.setOnSafeClickListener {
            val intent = Intent(requireContext(), DrawPadActivity::class.java)
            startActivity(intent)
        }

        // Neon Pad
        // Neon Pad
        binding.cardNeonPad.setOnSafeClickListener {
            startActivity(Intent(requireContext(), NeonPadActivity::class.java))
        }
    }
}