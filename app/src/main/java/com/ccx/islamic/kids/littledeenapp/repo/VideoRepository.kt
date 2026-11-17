package com.ccx.islamic.kids.littledeenapp.repo



import android.content.Context
import com.ccx.islamic.kids.littledeenapp.api.YouTubeOEmbedApi
import com.ccx.islamic.kids.littledeenapp.db.CachedVideoEntity
import com.ccx.islamic.kids.littledeenapp.db.SavedVideoEntity
import com.ccx.islamic.kids.littledeenapp.db.VideoDao
import com.ccx.islamic.kids.littledeenapp.model.KidVideo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton


data class ShortJsonVideo(
    val youtubeUrl: String,
    val categoryId: String
)

@Singleton
class VideoRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    val videoDao: VideoDao,
    private val oEmbedApi: YouTubeOEmbedApi
) {

    /**
     * Loads videos from Room DB. If empty (first app launch), it reads short JSON,
     * fetches real YouTube titles/authors, saves to Room, and serves instantly.
     */
    private suspend fun getOrSyncVideos(): List<KidVideo> {
        // 1. Check local Room cache first (Instant!)
        val localCache = videoDao.getAllCachedVideos()
        if (localCache.isNotEmpty()) {
            return localCache.map {
                KidVideo(it.videoId, it.title, it.channelTitle, it.thumbnailUrl, "Safe Kids", it.categoryId)
            }
        }

        // 2. If first time launch, read short JSON from assets
        return try {
            val jsonString = context.assets.open("kids_videos.json")
                .bufferedReader()
                .use { it.readText() }

            val listType = object : TypeToken<List<ShortJsonVideo>>() {}.type
            val shortList: List<ShortJsonVideo> = Gson().fromJson(jsonString, listType)

            val newlyFetched = mutableListOf<CachedVideoEntity>()

            for (item in shortList) {
                val vId = extractVideoId(item.youtubeUrl) ?: continue
                var title: String
                var author: String

                try {
                    // Fetch real title & author from YouTube free oEmbed API once
                    val info = oEmbedApi.getVideoInfo(item.youtubeUrl)
                    title = info.title
                    author = info.author_name
                } catch (e: Exception) {
                    title = "Halal Kids Video"
                    author = "Safe Creator"
                }

                val thumb = "https://img.youtube.com/vi/$vId/hqdefault.jpg"
                newlyFetched.add(CachedVideoEntity(vId, title, author, thumb, item.categoryId))
            }

            // 3. Save permanently to Room database so it never needs to fetch again!
            if (newlyFetched.isNotEmpty()) {
                videoDao.insertAllCachedVideos(newlyFetched)
            }

            newlyFetched.map {
                KidVideo(it.videoId, it.title, it.channelTitle, it.thumbnailUrl, "Safe Kids", it.categoryId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun extractVideoId(url: String): String? {
        if (url.length == 11) return url
        val regExp = "^.*(youtu.be/|v/|u/\\w/|embed/|watch\\?v=|&v=)([^#&?]*).*"
        val match = regExp.toRegex().find(url)
        val extracted = match?.groupValues?.getOrNull(2)
        return if (!extracted.isNullOrBlank() && extracted.length == 11) extracted else null
    }

    fun getVideosForCategory(categoryId: String): Flow<List<KidVideo>> = flow {
        val allVideos = getOrSyncVideos()
        val filtered = if (categoryId == "all" || categoryId.isBlank()) {
            allVideos
        } else {
            allVideos.filter { it.categoryId.equals(categoryId, ignoreCase = true) }
        }
        emit(filtered.shuffled())
    }.flowOn(Dispatchers.IO)

    fun searchVideos(query: String): Flow<List<KidVideo>> = flow {
        val allVideos = getOrSyncVideos()
        val cleanQuery = query.trim().lowercase()
        val results = if (cleanQuery.isEmpty()) {
            allVideos.shuffled()
        } else {
            allVideos.filter {
                it.title.lowercase().contains(cleanQuery) ||
                        it.channelTitle.lowercase().contains(cleanQuery)
            }
        }
        emit(results)
    }.flowOn(Dispatchers.IO)

    fun getSuggestedVideos(currentVideoId: String, currentCategoryId: String): Flow<List<KidVideo>> = flow {
        val allVideos = getOrSyncVideos()

        // 🎯 Logic: Filter by same category, exclude current video, and shuffle
        val suggestions = allVideos.filter {
            it.videoId != currentVideoId && it.categoryId == currentCategoryId
        }.shuffled()

        // Use .take(10) if you want 10, or remove .take() to show all available
        emit(suggestions)
    }.flowOn(Dispatchers.IO)

    fun getWatchHistory(): Flow<List<SavedVideoEntity>> = videoDao.getWatchHistory()

    fun getFavoriteVideos(): Flow<List<SavedVideoEntity>> = videoDao.getFavoriteVideos()

    fun isFavorite(videoId: String): Flow<Boolean> = videoDao.isFavorite(videoId)

    suspend fun recordWatchHistory(video: KidVideo) {
        val entity = SavedVideoEntity(
            videoId = video.videoId,
            title = video.title,
            channelTitle = video.channelTitle,
            thumbnailUrl = video.thumbnailUrl,
            durationText = video.badgeLabel,
            categoryId = video.categoryId,
            lastWatchedAt = System.currentTimeMillis()
        )
        videoDao.insertOrUpdateVideo(entity)
    }

    suspend fun toggleFavorite(video: KidVideo, isFavorite: Boolean) {
        val entity = SavedVideoEntity(
            videoId = video.videoId,
            title = video.title,
            channelTitle = video.channelTitle,
            thumbnailUrl = video.thumbnailUrl,
            durationText = video.badgeLabel,
            categoryId = video.categoryId,
            isFavorite = isFavorite,
            lastWatchedAt = System.currentTimeMillis()
        )
        videoDao.insertOrUpdateVideo(entity)
    }
}




