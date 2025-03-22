package com.goazzi.mycompose.util

object Constants {
    const val BUSINESS_URL: String = "https://api.yelp.com/v3/businesses/"
    const val PIXABAY_URL: String = "https://pixabay.com/"
    const val PAGE_LIMIT: Int = 15
    const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
    const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000

    const val LAT_US = 40.73061
    const val LON_US = -73.935242
}

object PrefKeys{
    /**
     * There is one work around which uses shouldShowRequestPermissionRationale.
     * Create a SharedPreference with default value false and store value returned by shouldShowRequestPermissionRationale in it.
     * Before updating the value, check if the value set was true. If it was true then don't update it.
     *
     * Whenever you want to check for permission, get the value from SharedPreference and current value returned by shouldShowRequestPermissionRationale.
     * If shouldShowRequestPermissionRationale returns false but value from SharedPreference is true,
     * you can deduce that Never ask again was selected by user.
     */
//    const val SHOW_LOC_PERM_REQ = "show_loc_perm_req"


}