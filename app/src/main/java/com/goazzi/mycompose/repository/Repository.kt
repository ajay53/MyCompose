package com.goazzi.mycompose.repository

import com.goazzi.mycompose.model.SearchBusiness
import com.goazzi.mycompose.repository.remote.ApiService
import com.goazzi.mycompose.repository.remote.pixabay.PixabayApiService
import javax.inject.Inject
import javax.inject.Singleton

//@ViewModelScoped
@Singleton
class Repository @Inject constructor() {

//    @Inject
//    lateinit var loginDao: LoginDao

    @Inject
    lateinit var yelpApiService: ApiService

    @Inject
    lateinit var pixabayApiService: PixabayApiService

    /*suspend fun insertLogin(loginEntity: LoginEntity) {
        loginDao.insert(loginEntity = loginEntity)
    }*/

    suspend fun getBusinesses(searchBusiness: SearchBusiness) = yelpApiService.searchBusinessesBody(
        lat = searchBusiness.lat,
        lon = searchBusiness.lon,
        radius = searchBusiness.radius,
        sortBy = searchBusiness.sortBy,
        limit = searchBusiness.limit,
        offset = searchBusiness.offset
    )

    suspend fun searchMedia(params: Map<String, String>) =
        pixabayApiService.searchMedia(params = params)
}