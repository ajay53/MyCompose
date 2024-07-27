package com.goazzi.mycompose.repository.remote

import com.goazzi.mycompose.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(ViewModelComponent::class)
@Module
class RetrofitModule {

    @ViewModelScoped
    @Provides
    fun providesApiService(): ApiService {
        val loggingInterceptor: HttpLoggingInterceptor by lazy {
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val mOkHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .addInterceptor(HeaderInterceptor())
                .addInterceptor(loggingInterceptor)
//                .readTimeout(3, TimeUnit.SECONDS)
//                .connectTimeout(3, TimeUnit.SECONDS)
                .build()
        }

        val retrofitBuilder: Retrofit.Builder by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BUSINESS_URL)
                .client(mOkHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
        }

        return retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }
}
