package com.goazzi.mycompose.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Business(
    val alias: String = "",
    val categories: List<Category>,
    val coordinates: Center,

    @Json(name = "display_phone")
    val displayPhone: String = "",

    val distance: Double,
    val id: String = "",

    @Json(name = "image_url")
    val imageURL: String = "",

    @Json(name = "is_closed")
    val isClosed: Boolean,

    val location: Location,
    val name: String = "",
    val phone: String = "",
    val price: String? = "",
    val rating: Double = 0.0,

    @Json(name = "review_count")
    val reviewCount: Long = 0,

    val transactions: List<String>,
    val url: String = ""
)

@JsonClass(generateAdapter = true)
data class Category(
    val alias: String = "",
    val title: String = ""
)

@JsonClass(generateAdapter = true)
data class Center(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

@JsonClass(generateAdapter = true)
data class Location(
    val address1: String = "",
    val address2: String? = "",
    val address3: String? = "",
    val city: String = "",
    val country: String = "",

    @Json(name = "display_address")
    val displayAddress: List<String>,

    val state: String = "",

    @Json(name = "zip_code")
    val zipCode: String = ""
)

@JsonClass(generateAdapter = true)
data class Region(
    val center: Center
)

@JsonClass(generateAdapter = true)
data class BusinessesServiceClass(
    val businesses: List<Business>,
    val total: Long,
    val region: Region,
    var httpCode: Int = 0,
    var message: String = ""
)