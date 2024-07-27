package com.goazzi.mycompose.repository

import com.goazzi.mycompose.model.SearchBusiness
import com.goazzi.mycompose.repository.local.dao.LoginDao
import com.goazzi.mycompose.repository.local.entity.LoginEntity
import com.goazzi.mycompose.repository.remote.ApiService
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor() {

    @Inject
    lateinit var loginDao: LoginDao

    @Inject
    lateinit var apiService: ApiService

    suspend fun insertLogin(loginEntity: LoginEntity) {
        loginDao.insert(loginEntity = loginEntity)
    }

    suspend fun getBusinesses(searchBusiness: SearchBusiness) = apiService.searchBusinessesBody(
        lat = searchBusiness.lat,
        lon = searchBusiness.lon,
        radius = searchBusiness.radius,
        sortBy = searchBusiness.sortBy,
        limit = searchBusiness.limit,
        offset = searchBusiness.offset
    )
}