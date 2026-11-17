package com.ccx.islamic.kids.littledeenapp.ui.activities.videoPlayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ccx.islamic.kids.littledeenapp.R
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ccx.islamic.kids.littledeenapp.data.ThemeManager
import com.ccx.islamic.kids.littledeenapp.data.VideoCategoryChip
import com.ccx.islamic.kids.littledeenapp.databinding.ActivityHistoryBinding
import com.ccx.islamic.kids.littledeenapp.db.SavedVideoEntity
import com.ccx.islamic.kids.littledeenapp.extension.gone
import com.ccx.islamic.kids.littledeenapp.extension.setOnSafeClickListener
import com.ccx.islamic.kids.littledeenapp.extension.showToast
import com.ccx.islamic.kids.littledeenapp.extension.visible
import com.ccx.islamic.kids.littledeenapp.model.KidVideo
import com.ccx.islamic.kids.littledeenapp.repo.VideoRepository
import com.ccx.islamic.kids.littledeenapp.ui.adapter.CategoryChipAdapter
import com.ccx.islamic.kids.littledeenapp.ui.adapter.HistoryAdapter
import com.ccx.islamic.kids.littledeenapp.ui.base.BaseActivity
import com.ccx.islamic.kids.littledeenapp.ui.dialog.DeleteConfirmationDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class HistoryActivity : BaseActivity<ActivityHistoryBinding>(ActivityHistoryBinding::inflate) {

    @Inject
    lateinit var videoRepository: VideoRepository

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var filterAdapter: CategoryChipAdapter
    private var allHistoryList: List<SavedVideoEntity> = emptyList()
    private var currentFilter = "All"

    override fun setupViews() {
        applyThemeStyling()
        setupFilters()
        setupRecyclerView()
        setupClickListeners()
        loadHistoryData()
    }

    private fun applyThemeStyling() {
        val theme = themeManager.getSelectedTheme()
        binding.tvHeaderTitle.setTextColor(theme.primaryColor)
    }

    private fun setupFilters() {
        val filters = listOf(
            VideoCategoryChip("all", "All", "📁", "all", true),
            VideoCategoryChip("today", "Today", "🔥", "today"),
            VideoCategoryChip("yesterday", "Yesterday", "⏳", "yesterday"),
            VideoCategoryChip("weekly", "Weekly", "📅", "weekly"),
            VideoCategoryChip("monthly", "Monthly", "🗓️", "monthly")
        )

        filterAdapter = CategoryChipAdapter(filters, themeManager.getSelectedTheme()) { chip ->
            currentFilter = chip.title
            filterAndDisplayList(allHistoryList)
        }

        binding.rvHistoryFilters.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = filterAdapter
        }
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter { historyItem ->
            val video = KidVideo(
                videoId = historyItem.videoId,
                title = historyItem.title,
                channelTitle = historyItem.channelTitle,
                thumbnailUrl = historyItem.thumbnailUrl,
                badgeLabel = historyItem.durationText,
                categoryId = historyItem.categoryId
            )
            val intent = Intent(this, VideoPlayerActivity::class.java).apply {
                putExtra(VideoPlayerActivity.EXTRA_VIDEO, video)
            }
            startActivity(intent)
        }

        binding.rvHistoryVideos.apply {
            layoutManager = LinearLayoutManager(context) // 👈 Changed from 'this' to 'context'
            adapter = historyAdapter
        }
    }

    private fun loadHistoryData() {
        lifecycleScope.launch {
            videoRepository.getWatchHistory().collect { history ->
                allHistoryList = history
                filterAndDisplayList(history)
            }
        }
    }

    private fun filterAndDisplayList(list: List<SavedVideoEntity>) {
        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance()

        val filtered = when (currentFilter) {
            "Today" -> {
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                val startOfDay = calendar.timeInMillis
                list.filter { it.lastWatchedAt >= startOfDay }
            }
            "Yesterday" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                val startOfYesterday = calendar.timeInMillis
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                val endOfYesterday = calendar.timeInMillis
                list.filter { it.lastWatchedAt in startOfYesterday..endOfYesterday }
            }
            "Weekly" -> {
                val weekAgo = currentTime - (7L * 24 * 60 * 60 * 1000)
                list.filter { it.lastWatchedAt >= weekAgo }
            }
            "Monthly" -> {
                val monthAgo = currentTime - (30L * 24 * 60 * 60 * 1000)
                list.filter { it.lastWatchedAt >= monthAgo }
            }
            else -> list
        }

        if (filtered.isNotEmpty()) {
            binding.rvHistoryVideos.visible()
            binding.layoutEmptyHistory.gone()
            historyAdapter.submitList(filtered)
        } else {
            binding.rvHistoryVideos.gone()
            binding.layoutEmptyHistory.visible()
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnSafeClickListener { finish() }

        binding.btnClearHistory.setOnSafeClickListener {
            val dialog = DeleteConfirmationDialogFragment.newInstance(
                title = "Clear History",
                message = "Are you sure you want to clear your watch history ($currentFilter)?",
                onConfirm = {
                    executeDeletion()
                }
            )
            dialog.show(supportFragmentManager, DeleteConfirmationDialogFragment.TAG)
        }
    }

    private fun executeDeletion() {
        lifecycleScope.launch {
            if (currentFilter == "All") {
                videoRepository.videoDao.clearAllHistory()
                showToast("All watch history cleared")
            } else {
                // Delete filtered items matching the current view
                val currentTime = System.currentTimeMillis()
                val calendar = Calendar.getInstance()
                val dao = videoRepository.videoDao

                when (currentFilter) {
                    "Today" -> {
                        calendar.set(Calendar.HOUR_OF_DAY, 0)
                        calendar.set(Calendar.MINUTE, 0)
                        val startOfDay = calendar.timeInMillis
                        allHistoryList.filter { it.lastWatchedAt >= startOfDay }.forEach {
                            dao.removeFromHistory(it.videoId)
                        }
                    }
                    "Weekly" -> {
                        val weekAgo = currentTime - (7L * 24 * 60 * 60 * 1000)
                        allHistoryList.filter { it.lastWatchedAt >= weekAgo }.forEach {
                            dao.removeFromHistory(it.videoId)
                        }
                    }
                    "Monthly" -> {
                        val monthAgo = currentTime - (30L * 24 * 60 * 60 * 1000)
                        allHistoryList.filter { it.lastWatchedAt >= monthAgo }.forEach {
                            dao.removeFromHistory(it.videoId)
                        }
                    }
                }
                showToast("$currentFilter history cleared")
            }
        }
    }
}