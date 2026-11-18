package com.ccx.islamic.kids.littledeenapp.ui.activities.videoHub


import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.data.VideoCategoryChip
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityVideosHubBinding
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.ui.activities.playlist.PlaylistsActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.CategoriesActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.FavoritesActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.HistoryActivity
import com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer.SearchActivity
import com.ccx.islamic.kids.littledeenapp.ui.adapter.HubCategoryAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



@AndroidEntryPoint
class VideosHubActivity : BaseActivity<ActivityVideosHubBinding>(
    ActivityVideosHubBinding::inflate
) {

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var hubCategoryAdapter: HubCategoryAdapter

    override fun setupViews() {
        applyTheme()
        setupCategoriesList()
        setupClickListeners()
    }

    private fun applyTheme() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)
        binding.tvBrowseTitle.setTextColor(theme.primaryColor)
        binding.cardCustomPlaylistsBanner.setCardBackgroundColor(theme.primaryColor)
        binding.cardCustomPlaylistsBanner.strokeColor = theme.lightColor
    }

    private fun setupCategoriesList() {
        val categories = VideoCategoryChip.getDefaultCategories()

        hubCategoryAdapter = HubCategoryAdapter(categories) { selectedCategory ->
            // Opens CategoriesActivity and selects the tapped category
            val intent = Intent(this, CategoriesActivity::class.java).apply {
                putExtra(CategoriesActivity.EXTRA_TARGET_CATEGORY_ID, selectedCategory.id)
            }
            startActivity(intent)
        }

        binding.rvHubCategoriesList.apply {
            layoutManager = LinearLayoutManager(this@VideosHubActivity)
            adapter = hubCategoryAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }

        binding.btnSearchHub.setOnSafeClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        binding.cardCustomPlaylistsBanner.setOnSafeClickListener {
            startActivity(Intent(this, PlaylistsActivity::class.java))
        }

        binding.cardHubHistory.setOnSafeClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.cardHubFavorites.setOnSafeClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }
}