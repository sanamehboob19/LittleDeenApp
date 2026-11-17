package com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ccx.islamic.kids.littledeenapp.R

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.data.VideoCategoryChip
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityCategoriesBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.adapter.CategoryChipAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.ui.fragment.CategoryVideosFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesActivity : BaseActivity<ActivityCategoriesBinding>(ActivityCategoriesBinding::inflate) {

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var categories: List<VideoCategoryChip>
    private lateinit var chipAdapter: CategoryChipAdapter

    override fun setupViews() {
        categories = VideoCategoryChip.getDefaultCategories()

        setupChips()
        setupViewPager()
        setupClickListeners()
        animateSwipeHint()

        // 🎯 Jump directly to the category tapped in VideosHubActivity
        val targetCatId = intent.getStringExtra(EXTRA_TARGET_CATEGORY_ID)
        if (!targetCatId.isNullOrEmpty()) {
            val targetIndex = categories.indexOfFirst { it.id == targetCatId }
            if (targetIndex != -1) {
                binding.viewPagerCategories.setCurrentItem(targetIndex, false)
                chipAdapter.updateSelection(targetIndex)
                binding.rvCategoryScreenChips.scrollToPosition(targetIndex)
            }
        }


    }

    companion object {
        const val EXTRA_TARGET_CATEGORY_ID = "extra_target_category_id"
    }

    private fun setupChips() {
        chipAdapter = CategoryChipAdapter(categories, themeManager.getSelectedTheme()) { selectedChip ->
            val index = categories.indexOf(selectedChip)
            if (index != -1) {
                binding.viewPagerCategories.currentItem = index
            }
        }

        binding.rvCategoryScreenChips.apply {
            layoutManager = LinearLayoutManager(this@CategoriesActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = chipAdapter
        }
    }

    private fun setupViewPager() {
        binding.viewPagerCategories.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = categories.size

            override fun createFragment(position: Int): Fragment {
                return CategoryVideosFragment.newInstance(categories[position].id)
            }
        }

        binding.viewPagerCategories.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // 1. Update chip selection state
                chipAdapter.updateSelection(position)

                // 2. Smoothly scroll the top chips RecyclerView to center the selected chip
                binding.rvCategoryScreenChips.smoothScrollToPosition(position)
            }
        })
    }



    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }

        // Open Search Activity when tapping search bar
        binding.cardCategorySearch.setOnSafeClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    private fun animateSwipeHint() {
        // Fade out the swipe hint after a few seconds safely
        binding.cardSwipeHint.animate()
            .alpha(0f)
            .setStartDelay(3500)
            .setDuration(800)
            .withEndAction {
                // Ensure activity is not finishing or destroyed before touching binding
                if (!isFinishing && !isDestroyed) {
                    try {
                        binding.cardSwipeHint.visibility = android.view.View.GONE
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            .start()
    }
}