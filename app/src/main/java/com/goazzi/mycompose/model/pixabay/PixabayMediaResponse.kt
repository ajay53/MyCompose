package com.goazzi.mycompose.model.pixabay

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
data class PixabayServiceClass(
    var httpCode: Int = 0,
    var message: String = "",
    val total: Long? = null,
    val totalHits: Long? = null,

    @Json(name = "hits")
    val hits: List<HitMedia>? = null  // Use a single list for both images & videos
)

@Parcelize
sealed class HitMedia : Parcelable {

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Image(
        val id: Long? = null,
        val pageURL: String? = null,
        val type: String? = null,
        val tags: String? = null,
        val previewURL: String? = null,
        val previewWidth: Long? = null,
        val previewHeight: Long? = null,
        val webformatURL: String? = null,
        val webformatWidth: Long? = null,
        val webformatHeight: Long? = null,
        val largeImageURL: String? = null,
        val imageWidth: Long? = null,
        val imageHeight: Long? = null,
        val imageSize: Long? = null,
        val views: Long? = null,
        val downloads: Long? = null,
        val collections: Long? = null,
        val likes: Long? = null,
        val comments: Long? = null,

        @Json(name = "user_id")
        val userID: Long? = null,

        val user: String? = null,
        val userImageURL: String? = null
    ) : HitMedia(), Parcelable

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Video(
        val id: Long? = null,
        val pageURL: String? = null,
        val type: String? = null,
        val tags: String? = null,
        val duration: Long? = null,
        val videos: VideoDetails? = null,
        val views: Long? = null,
        val downloads: Long? = null,
        val likes: Long? = null,
        val comments: Long? = null,

        @Json(name = "user_id")
        val userID: Long? = null,

        val user: String? = null,
        val userImageURL: String? = null
    ) : HitMedia(), Parcelable
}

@Parcelize
@JsonClass(generateAdapter = true)
data class VideoDetails(
    val large: VideoSize? = null,
    val medium: VideoSize? = null,
    val small: VideoSize? = null,
    val tiny: VideoSize? = null
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class VideoSize(
    val url: String? = null,
    val width: Long? = null,
    val height: Long? = null,
    val size: Long? = null,
    val thumbnail: String? = null
) : Parcelable


