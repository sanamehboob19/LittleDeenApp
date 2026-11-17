package com.ccx.islamic.kids.littledeenapp.db



import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val playlistId: Long = 0L,
    val playlistName: String,
    val playlistEmoji: String = "📂",
    val coverImageUri: String? = null, // 👈 Stores custom image uri from camera/gallery
    val createdAt: Long = System.currentTimeMillis()
)