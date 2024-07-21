package com.goazzi.mycompose.repository.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_login")
data class LoginEntity(

    @PrimaryKey
    val username: String,

    @ColumnInfo(name = "email_id")
    val emailID: String,
    val password: String
)
