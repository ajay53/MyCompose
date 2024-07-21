package com.goazzi.mycompose.repository

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
}