package com.goazzi.mycompose.util

enum class SortByEnum(val type: String) {
    BEST_MATCH("best_match"),
    RATING("rating"),
    REVIEW_COUNT("review_count"),
    DISTANCE("distance")
}

enum class PermissionEnum {
    GPS, LOCATION
}

enum class LocationEnum(value:String) {
    USA(value = "USA"),
    CURRENT(value="Current")
}