package com.goazzi.mycompose.repository.remote

import com.goazzi.mycompose.model.BusinessesServiceClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search")
    suspend fun searchBusinessesBody(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("radius") radius: Int,
        @Query("sort_by") sortBy: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<BusinessesServiceClass>
}