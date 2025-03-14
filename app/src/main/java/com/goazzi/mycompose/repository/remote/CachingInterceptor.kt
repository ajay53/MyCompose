package com.goazzi.mycompose.repository.remote

import android.content.Context
import com.goazzi.mycompose.util.Util.hasNetwork
import okhttp3.Interceptor
import okhttp3.Response

class CachingInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        /*request = if (context.hasNetwork()) {
            request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
        } else {
            request.newBuilder()
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=${60 * 60 * 24 * 7}"
                ) // 7 days
                .build()
        }*/

        if (!context.hasNetwork()) {
            request = request.newBuilder()
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=${60 * 60 * 12}"
                ) // 12 hours (43200 seconds)
                .build()
        }

        return chain.proceed(request)
    }
}