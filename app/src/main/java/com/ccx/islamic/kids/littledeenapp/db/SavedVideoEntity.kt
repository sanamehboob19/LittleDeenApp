package com.ccx.islamic.kids.littledeenapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_videos")
data class SavedVideoEntity(
    @PrimaryKey val videoId: String,
    val title: String,
    val channelTitle: String,
    val channelId: String = "",
    val thumbnailUrl: String,
    val durationText: String = "",
    val categoryId: String = "",
    val isFavorite: Boolean = false,
    val lastWatchedAt: Long = 0L // 0 means not in history, timestamp means watched
)