package com.ccx.islamic.kids.littledeenapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_channels")
data class SavedChannelEntity(
    @PrimaryKey val channelId: String,
    val channelTitle: String,
    val channelAvatarUrl: String = "",
    val isSaved: Boolean = true,
    val isBlocked: Boolean = false,
    val addedAt: Long = System.currentTimeMillis()
)