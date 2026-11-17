package com.ccx.islamic.kids.littledeenapp.api

import retrofit2.http.GET
import retrofit2.http.Query

data class OEmbedResponse(
    val title: String,
    val author_name: String,
    val thumbnail_url: String
)

interface YouTubeOEmbedApi {
    @GET("oembed?format=json")
    suspend fun getVideoInfo(@Query("url") videoUrl: String): OEmbedResponse

    companion object {
        const val BASE_URL = "https://www.youtube.com/"
    }
}