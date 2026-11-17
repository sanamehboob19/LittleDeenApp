package com.ccx.islamic.kids.littledeenapp.util


import com.ccx.islamic.kids.littledeenapp.db.StreakDao
import com.ccx.islamic.kids.littledeenapp.db.StreakEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreakManager @Inject constructor(
    private val streakDao: StreakDao
) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    val streakFlow: Flow<StreakEntity?> = streakDao.getStreakFlow()

    /**
     * Call this whenever a child successfully completes a qualifying learning activity
     * (e.g., finishes a Quran lesson, Quiz, Dua, or Story).
     */
    suspend fun recordLearningActivityComplete(): StreakEntity {
        val todayStr = getCurrentDateString()
        val currentStreakData = streakDao.getStreakSync() ?: StreakEntity(
            currentStreak = 0,
            longestStreak = 0,
            availableFreezes = 1,
            daysCompletedForNextFreeze = 0,
            lastCompletedDate = ""
        )

        // If already completed today, do nothing to prevent multi-count spam
        if (currentStreakData.lastCompletedDate == todayStr) {
            return currentStreakData
        }

        val lastDateStr = currentStreakData.lastCompletedDate
        var newStreakCount: Int
        var newFreezes = currentStreakData.availableFreezes
        var freezeProgress = currentStreakData.daysCompletedForNextFreeze

        if (lastDateStr.isEmpty()) {
            // First time ever learning
            newStreakCount = 1
        } else {
            val daysBetween = getDaysBetween(lastDateStr, todayStr)

            when (daysBetween) {
                1L -> {
                    // Consecutive day! Streak increases by 1
                    newStreakCount = currentStreakData.currentStreak + 1
                    freezeProgress += 1

                    // Earn a Streak Freeze every 5 successful days (Max storage: 2)
                    if (freezeProgress >= 5 && newFreezes < 2) {
                        newFreezes += 1
                        freezeProgress = 0
                    } else if (newFreezes >= 2) {
                        freezeProgress = 0 // cap progress if freezes maxed out
                    }
                }
                2L -> {
                    // Missed exactly 1 day. Check if we have a freeze to protect it!
                    if (currentStreakData.availableFreezes > 0) {
                        newFreezes -= 1 // Consume 1 freeze
                        newStreakCount = currentStreakData.currentStreak + 1 // Protect & continue streak!
                    } else {
                        // No freeze available, streak resets
                        newStreakCount = 1
                    }
                }
                else -> {
                    // Missed 2+ days, streak resets completely
                    newStreakCount = 1
                }
            }
        }

        val newLongest = maxOf(newStreakCount, currentStreakData.longestStreak)

        val updated = StreakEntity(
            id = 1,
            currentStreak = newStreakCount,
            longestStreak = newLongest,
            availableFreezes = newFreezes,
            daysCompletedForNextFreeze = freezeProgress,
            lastCompletedDate = todayStr
        )

        streakDao.updateStreak(updated)
        return updated
    }

    private fun getCurrentDateString(): String {
        return dateFormat.format(Calendar.getInstance().time)
    }

    private fun getDaysBetween(oldDateStr: String, newDateStr: String): Long {
        return try {
            val oldDate = dateFormat.parse(oldDateStr) ?: return 0L
            val newDate = dateFormat.parse(newDateStr) ?: return 0L
            val diffMillis = newDate.time - oldDate.time
            diffMillis / (1000 * 60 * 60 * 24)
        } catch (e: Exception) {
            0L
        }
    }
}