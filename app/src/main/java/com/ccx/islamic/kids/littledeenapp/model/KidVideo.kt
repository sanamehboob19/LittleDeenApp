package com.ccx.islamic.kids.littledeenapp.model



import android.os.Parcelable
import kotlinx.parcelize.Parcelize



@Parcelize
data class KidVideo(
    val videoId: String,
    val title: String,
    val channelTitle: String,
    val thumbnailUrl: String = "",
    val badgeLabel: String = "Safe Kids",
    val categoryId: String = ""
) : Parcelable {

    // Automatically extracts 11-character YouTube ID from any full YouTube URL format
    val cleanVideoId: String
        get() {
            if (videoId.length == 11) return videoId
            val regExp = "^.*(youtu.be/|v/|u/\\w/|embed/|watch\\?v=|&v=)([^#&?]*).*"
            val match = videoId.toRegex().find(videoId)
            val extracted = match?.groupValues?.getOrNull(2)
            return if (!extracted.isNullOrBlank() && extracted.length == 11) extracted else videoId
        }

    // Automatically generates YouTube's crisp thumbnail from the link if thumbnail is blank
    val resolvedThumbnailUrl: String
        get() {
            if (thumbnailUrl.isNotBlank() && thumbnailUrl.startsWith("http")) {
                return thumbnailUrl
            }
            return "https://img.youtube.com/vi/$cleanVideoId/hqdefault.jpg"
        }
}


//@Parcelize
//data class KidVideo(
//    val videoId: String,
//    val title: String,
//    val channelTitle: String,
//    val thumbnailUrl: String,
//    val badgeLabel: String = "Safe Kids",
//    val categoryId: String = ""
//) : Parcelable