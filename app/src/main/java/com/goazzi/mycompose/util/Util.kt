package com.goazzi.mycompose.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.ActivityCompat
import com.goazzi.mycompose.R
import timber.log.Timber

private const val TAG = "Util"

object Util{
    //checking for GPS
    fun isGpsEnabled(context: Context): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var isGpsEnabled = false
        try {
            isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: java.lang.Exception) {
            Timber.tag(TAG).d(message = "isGpsEnabled: $ex")
        }

        return isGpsEnabled
    }

    //checking for location permission
    fun hasLocationPermission(context: Context): Boolean {
        val fineLoc: Boolean = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLoc: Boolean = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fineLoc && coarseLoc
    }

    fun getRating(rating: Double):Int {

//    val colorId: Int = when {
        return when {
            rating > 4.5 -> R.color.holo_green_dark
            rating > 4.0 -> R.color.yellow
            rating > 3.5 -> R.color.holo_orange_dark
            else -> R.color.holo_red_dark
        }
    }

    fun Context.hasNetwork(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    val rursusFamily = FontFamily(
        Font(R.font.rursus_compact_mono, FontWeight.Medium),
        Font(R.font.rursus_compact_mono, FontWeight.Normal),
        Font(R.font.rursus_compact_mono, FontWeight.Bold),
        Font(R.font.rursus_compact_mono, FontWeight.SemiBold),
    )
}


