package com.goazzi.mycompose.repository.remote

import com.goazzi.mycompose.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder =
            chain.request()
                .newBuilder()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
                .build()
        return chain.proceed(requestBuilder)
    }
}

/*
class KisiHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder =
            chain.request()
                .newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
//                .addHeader("Authorization", "KISI-LOGIN a1d80afff1ab5af93ac7bc524dfc01cf")
                .addHeader("Authorization", "KISI-LOGIN ${ViewModelCompose.loginSecret}")
//                .addHeader("Authorization", "KISI-LOGIN ${KisiRetrofitBuilder.getSecretKey()}")
                .build()
        return chain.proceed(requestBuilder)
    }
}*/
