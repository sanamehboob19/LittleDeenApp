package com.ccx.islamic.kids.littledeenapp.ui.activities.surah

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.SurahItem
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivitySurahsBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.adapter.SurahListAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SurahsActivity :
    BaseActivity<ActivitySurahsBinding>(ActivitySurahsBinding::inflate) {

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var surahAdapter: SurahListAdapter

    override fun setupViews() {
        applyThemeStyling()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)

        // Gradient logic for Hero Card
        val lighter = ColorUtils.blendARGB(theme.primaryColor, Color.WHITE, 0.25f)
        val grad = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(theme.primaryColor, lighter)
        )
        binding.flSurahBannerBg.background = grad
        binding.cardHeaderHero.strokeWidth = 2
    }

    private fun setupRecyclerView() {
        val surahs = SurahItem.getAllSurahs()

        surahAdapter = SurahListAdapter(surahs) { selectedSurah ->
            val intent = Intent(this, SurahDetailActivity::class.java).apply {
                putExtra(SurahDetailActivity.EXTRA_SURAH_ID, selectedSurah.id)
            }
            startActivity(intent)
        }

        binding.rvSurahsList.apply {
            layoutManager = LinearLayoutManager(this@SurahsActivity)
            adapter = surahAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }
    }
}