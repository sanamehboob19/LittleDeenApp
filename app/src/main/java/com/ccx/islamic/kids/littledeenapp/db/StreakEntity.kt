package com.ccx.islamic.kids.littledeenapp.db


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learning_streak")
data class StreakEntity(
    @PrimaryKey val id: Int = 1, // Singleton row pattern
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val availableFreezes: Int = 1, // Start with 1 freeze
    val daysCompletedForNextFreeze: Int = 0, // 0 to 5 days towards next freeze
    val lastCompletedDate: String = "" // Format: "yyyy-MM-dd" using device local date
)