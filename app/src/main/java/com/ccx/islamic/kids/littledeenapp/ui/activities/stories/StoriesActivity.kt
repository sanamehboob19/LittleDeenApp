package com.ccx.islamic.kids.littledeenapp.ui.activities.stories

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ccx.islamic.kids.littledeenapp.R

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.StoryItem
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityStoriesBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.adapter.StoryListAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StoriesActivity : BaseActivity<ActivityStoriesBinding>(ActivityStoriesBinding::inflate)
{

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var storyAdapter: StoryListAdapter

    override fun setupViews() {
        applyThemeStyling()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        val primary = theme.primaryColor

        // Header
        binding.tvHeaderTitle.setTextColor(primary)

        // Hero Card Gradient
        val lighter = ColorUtils.blendARGB(primary, Color.WHITE, 0.25f)
        val grad =
            GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(primary, lighter))
        binding.flStoryBannerBg.background = grad

        // Borders
        binding.cardHeaderHero.strokeWidth = 2
    }

    private fun setupRecyclerView() {
        val stories = StoryItem.getDefaultStories()
        // Inject themeManager here too if you want to tint individual story cards
        storyAdapter = StoryListAdapter(stories, themeManager) { selectedStory ->
            val intent = Intent(this, StoryDetailActivity::class.java).apply {
                putExtra(StoryDetailActivity.EXTRA_STORY_ID, selectedStory.id)
            }
            startActivity(intent)
        }
        binding.rvStoriesList.apply {
            layoutManager = LinearLayoutManager(this@StoriesActivity)
            adapter = storyAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }
    }
}