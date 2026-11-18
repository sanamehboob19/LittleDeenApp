package com.ccx.islamic.kids.littledeenapp.ui.activities.stories

import android.os.Bundle
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.ccx.islamic.kids.littledeenapp.data.StoryItem
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityStoryDetailBinding
import com.ccx.islamic.kids.littledeenapp.databinding.FragmentStoryPageBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.util.MusicManager
import com.ccx.islamic.kids.littledeenapp.util.VoiceAssistant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StoryDetailActivity : BaseActivity<ActivityStoryDetailBinding>(
    ActivityStoryDetailBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var storiesList: List<StoryItem>

    @Inject
    lateinit var musicManager: MusicManager

    override fun setupViews() {
        storiesList = StoryItem.getDefaultStories()
        val targetId = intent.getStringExtra(EXTRA_STORY_ID) ?: "story_1"
        val startIndex = storiesList.indexOfFirst { it.id == targetId }.coerceAtLeast(0)

        applyTheme()
        setupViewPager(startIndex)
        setupClickListeners()
    }

    private fun applyTheme() {
        binding.tvHeaderTitle.setTextColor(themeManager.getSelectedTheme().primaryColor)
    }

    private fun setupViewPager(startIndex: Int) {
        binding.viewPagerStories.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = storiesList.size
            override fun createFragment(position: Int): Fragment {
                return StoryPageFragment.newInstance(storiesList[position])
            }
        }
        binding.viewPagerStories.setCurrentItem(startIndex, false)

        binding.viewPagerStories.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateNavButtons(position)
            }
        })
    }

    private fun updateNavButtons(position: Int) {
        binding.btnPrevStory.isEnabled = position > 0
        binding.btnNextStory.isEnabled = position < storiesList.size - 1

        val theme = themeManager.getSelectedTheme()
        if (position == storiesList.size - 1) {
            binding.btnNextStory.text = "Finish 🏆"
            binding.btnNextStory.backgroundTintList = ColorStateList.valueOf(android.graphics.Color.parseColor("#00C853"))
        } else {
            binding.btnNextStory.text = "Next Story →"
            binding.btnNextStory.backgroundTintList = ColorStateList.valueOf(theme.primaryColor)
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }

        binding.btnPrevStory.setOnSafeClickListener {
            val current = binding.viewPagerStories.currentItem
            if (current > 0) binding.viewPagerStories.currentItem = current - 1
        }

        binding.btnNextStory.setOnSafeClickListener {
            val current = binding.viewPagerStories.currentItem
            if (current < storiesList.size - 1) {
                binding.viewPagerStories.currentItem = current + 1
            } else {
                finish()
            }
        }
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

    companion object {
        const val EXTRA_STORY_ID = "extra_story_id"
    }
}
