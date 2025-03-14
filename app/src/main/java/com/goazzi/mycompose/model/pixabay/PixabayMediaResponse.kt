package com.goazzi.mycompose.model.pixabay

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PixabayServiceClass(
    var httpCode: Int = 0,
    var message: String = "",
    val total: Long? = null,
    val totalHits: Long? = null,
    val hits: List<Hit>? = null
)

@JsonClass(generateAdapter = true)
data class Hit(
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
)