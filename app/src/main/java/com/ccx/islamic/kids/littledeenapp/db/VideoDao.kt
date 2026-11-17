package com.ccx.islamic.kids.littledeenapp.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    // ==========================================
    // 📺 History Operations
    // ==========================================
    @Query("SELECT * FROM saved_videos WHERE lastWatchedAt > 0 ORDER BY lastWatchedAt DESC")
    fun getWatchHistory(): Flow<List<SavedVideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateVideo(video: SavedVideoEntity)

    @Query("UPDATE saved_videos SET lastWatchedAt = :timestamp WHERE videoId = :videoId")
    suspend fun updateLastWatched(videoId: String, timestamp: Long)

    @Query("UPDATE saved_videos SET lastWatchedAt = 0 WHERE videoId = :videoId")
    suspend fun removeFromHistory(videoId: String)

    @Query("UPDATE saved_videos SET lastWatchedAt = 0")
    suspend fun clearAllHistory()

    // ==========================================
    // ❤️ Favorites Operations
    // ==========================================
    @Query("SELECT * FROM saved_videos WHERE isFavorite = 1 ORDER BY lastWatchedAt DESC")
    fun getFavoriteVideos(): Flow<List<SavedVideoEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_videos WHERE videoId = :videoId AND isFavorite = 1)")
    fun isFavorite(videoId: String): Flow<Boolean>

    @Query("UPDATE saved_videos SET isFavorite = :isFav WHERE videoId = :videoId")
    suspend fun setFavorite(videoId: String, isFav: Boolean)

    // ==========================================
    // 🏷️ Saved Channels Operations
    // ==========================================
    @Query("SELECT * FROM saved_channels WHERE isSaved = 1 AND isBlocked = 0")
    fun getSavedChannels(): Flow<List<SavedChannelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChannel(channel: SavedChannelEntity)

    @Query("DELETE FROM saved_channels WHERE channelId = :channelId")
    suspend fun removeChannel(channelId: String)



    // ==========================================
    // 📂 Custom Playlists Operations
    // ==========================================
    @Query("SELECT * FROM playlists ORDER BY createdAt DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("DELETE FROM playlists WHERE playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addVideoToPlaylist(crossRef: PlaylistVideoCrossRef)

    @Query("DELETE FROM playlist_video_cross_ref WHERE playlistId = :playlistId AND videoId = :videoId")
    suspend fun removeVideoFromPlaylist(playlistId: Long, videoId: String)

    @Query("""
        SELECT * FROM saved_videos 
        INNER JOIN playlist_video_cross_ref ON saved_videos.videoId = playlist_video_cross_ref.videoId 
        WHERE playlist_video_cross_ref.playlistId = :playlistId
    """)
    fun getVideosForPlaylist(playlistId: Long): Flow<List<SavedVideoEntity>>

    @Query("SELECT * FROM playlists ORDER BY createdAt DESC")
    suspend fun getAllPlaylistsList(): List<PlaylistEntity>

    @Query("SELECT COUNT(*) FROM playlist_video_cross_ref WHERE playlistId = :playlistId")
    suspend fun getPlaylistVideoCount(playlistId: Long): Int


    @Query("SELECT * FROM cached_videos")
    suspend fun getAllCachedVideos(): List<CachedVideoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCachedVideos(videos: List<CachedVideoEntity>)



}