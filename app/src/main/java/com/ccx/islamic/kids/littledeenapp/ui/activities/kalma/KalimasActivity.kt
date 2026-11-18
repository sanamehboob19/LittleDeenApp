package com.ccx.islamic.kids.littledeenapp.ui.activities.kalma

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.KalimaItem
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityKalimasBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.adapter.KalimaListAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.util.MusicManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class KalimasActivity : BaseActivity<ActivityKalimasBinding>(
    ActivityKalimasBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var kalimaAdapter: KalimaListAdapter
    @Inject
    lateinit var musicManager: MusicManager

    override fun setupViews() {
        applyThemeStyling() // Use the consistent method name
        setupRecyclerView()
        setupClickListeners()
    }


    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        val primaryColor = theme.primaryColor
        val lightColor = theme.lightColor

        // 1. Header Styling
        binding.tvHeaderTitle.setTextColor(primaryColor)

        // 2. 🎯 Dynamically generate matching gradient for the Hero Card
        // This creates that "Premium/Professional" look consistent with your Home screen
        val lighterShade = ColorUtils.blendARGB(primaryColor, Color.WHITE, 0.25f)
        val dynamicGradient = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(primaryColor, lighterShade)
        )

        // Applying to the Hero Card container
        // Make sure your XML FrameLayout has id="@+id/flKalimaBannerBg"
        binding.flKalimaBannerBg.background = dynamicGradient

        // 3. Card Borders & Elements
        binding.cardHeaderHero.setCardBackgroundColor(primaryColor)

        // Tinting the text/icons inside the Hero card if needed
        // binding.tvHeroTitle.setTextColor(Color.WHITE)
    }

    private fun setupRecyclerView() {
        val kalimas = KalimaItem.getAllKalimas()

        kalimaAdapter = KalimaListAdapter(kalimas, themeManager) { selectedKalima ->
            val intent = Intent(this, KalimaDetailActivity::class.java).apply {
                putExtra(KalimaDetailActivity.EXTRA_KALIMA_ID, selectedKalima.id)
            }
            startActivity(intent)
        }

        binding.rvKalimasList.apply {
            layoutManager = LinearLayoutManager(this@KalimasActivity)
            adapter = kalimaAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        // 🔇 Pause background music whenever this screen is active or re-opened
        musicManager.pauseMusic()
    }

    override fun onDestroy() {
        // 🔊 Resume background music when the user leaves this screen
        musicManager.startMusic()
        super.onDestroy()
    }


}