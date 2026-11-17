package com.ccx.islamic.kids.littledeenapp.ui.activities.streak

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ccx.islamic.kids.littledeenapp.R

import androidx.lifecycle.lifecycleScope
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityStreakDetailsBinding
import com.ccx.islamic.kids.littledeenapp.db.StreakDao
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StreakDetailsActivity : BaseActivity<ActivityStreakDetailsBinding>(
    ActivityStreakDetailsBinding::inflate
) {

    @Inject
    lateinit var streakDao: StreakDao

    @Inject
    lateinit var themeManager: ThemeManager

    override fun setupViews() {
        applyTheme()
        setupClickListeners()
        loadStreakData()
    }

    private fun applyTheme() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)
    }

    private fun loadStreakData() {
        lifecycleScope.launch {
            streakDao.getStreakFlow().collectLatest { streak ->
                val current = streak?.currentStreak ?: 1
                val longest = streak?.longestStreak ?: current
                val freezes = streak?.availableFreezes ?: 1

                binding.tvBigStreakCount.text = "$current Days"
                binding.tvLongestStreak.text = "$longest Days"
                binding.tvFreezesCount.text = "$freezes / 2"
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }
    }
}