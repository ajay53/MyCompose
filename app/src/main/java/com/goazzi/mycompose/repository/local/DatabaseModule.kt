package com.goazzi.mycompose.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.goazzi.mycompose.repository.local.dao.LoginDao
import com.goazzi.mycompose.repository.local.entity.LoginEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
class DatabaseModule {

    @Provides
    @ViewModelScoped
    fun provideDatabase(@ApplicationContext context: Context): DatabaseHandler {
        return Room.databaseBuilder(
            context,
            DatabaseHandler::class.java,
            "oonga boonga"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @ViewModelScoped
    fun provideLoginDao(databaseHandler: DatabaseHandler): LoginDao {
        return databaseHandler.loginDao()
    }
}

@Database(
    entities = [LoginEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseHandler : RoomDatabase() {

    abstract fun loginDao(): LoginDao
}