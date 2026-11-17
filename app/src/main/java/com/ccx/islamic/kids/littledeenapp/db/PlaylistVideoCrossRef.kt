package com.ccx.islamic.kids.littledeenapp.db



import androidx.room.Entity

@Entity(tableName = "playlist_video_cross_ref", primaryKeys = ["playlistId", "videoId"])
data class PlaylistVideoCrossRef(
    val playlistId: Long,
    val videoId: String
)