package com.ccx.islamic.kids.littledeenapp.ui.activities.namaz

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ccx.islamic.kids.littledeenapp.R
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.SalahStep
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivitySalahGuideBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.adapter.SalahStepAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SalahGuideActivity : BaseActivity<ActivitySalahGuideBinding>(
    ActivitySalahGuideBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    override fun setupViews() {
        applyTheme()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun applyTheme() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)
    }

    private fun setupRecyclerView() {
        val steps = SalahStep.getAllSteps()

        // 1. Rename the local variable from 'adapter' to 'stepAdapter'
        val stepAdapter = SalahStepAdapter(steps) { selectedStep ->
            val intent = Intent(this, SalahDetailActivity::class.java).apply {
                putExtra(SalahDetailActivity.EXTRA_STEP_NUM, selectedStep.stepNumber)
            }
            startActivity(intent)
        }

        binding.rvSalahSteps.apply {
            layoutManager = LinearLayoutManager(this@SalahGuideActivity)
            // 2. Assign it clearly here
            adapter = stepAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }
    }
}