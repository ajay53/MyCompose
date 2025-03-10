package com.goazzi.mycompose.repository.local

/*
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
}*/
