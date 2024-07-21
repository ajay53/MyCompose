package com.goazzi.mycompose.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.goazzi.mycompose.repository.local.entity.LoginEntity

@Dao
interface LoginDao {

    @Insert
    suspend fun insert(loginEntity:LoginEntity)
}