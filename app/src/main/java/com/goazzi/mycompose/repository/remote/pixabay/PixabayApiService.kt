package com.goazzi.mycompose.repository.remote.pixabay

import com.goazzi.mycompose.BuildConfig
import com.goazzi.mycompose.model.pixabay.PixabayServiceClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface PixabayApiService {

    @GET("api/{apiPath}")
    suspend fun searchMedia(
        @Path("apiPath", encoded = true) apiPath: String = "",
        @Query("key") key: String = BuildConfig.PIXABAY_API_KEY,
        @QueryMap params: Map<String, String>
    ): Response<PixabayServiceClass>
}