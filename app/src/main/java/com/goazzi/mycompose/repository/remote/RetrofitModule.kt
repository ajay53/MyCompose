package com.goazzi.mycompose.repository.remote

import android.content.Context
import com.goazzi.mycompose.repository.remote.pixabay.PixabayApiService
import com.goazzi.mycompose.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

//@InstallIn(ViewModelComponent::class)
@InstallIn(SingletonComponent::class)
@Module
class RetrofitModule {

//    @ViewModelScoped
    @Singleton
    @Provides
    fun providesApiService(@ApplicationContext context: Context): ApiService {

        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)

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

        val okHttpClient = OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor { chain -> // Cache for online mode
                val response = chain.proceed(chain.request())
                response.newBuilder()
                    .header("Cache-Control", "public, max-age=${60 * 60 * 12}") // 12 hours (43200 seconds)
                    .build()
            }
            .build()

        val retrofitBuilder: Retrofit.Builder by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BUSINESS_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
        }

        return retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesPixabayApiService(@ApplicationContext context: Context): PixabayApiService {

        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)

        val loggingInterceptor: HttpLoggingInterceptor by lazy {
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val okHttpClient = OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor { chain -> // Cache for online mode
                val response = chain.proceed(chain.request())
                response.newBuilder()
                    .header("Cache-Control", "public, max-age=${60 * 60 * 12}") // 12 hours (43200 seconds)
                    .build()
            }
            .build()

        val retrofitBuilder: Retrofit.Builder by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.PIXABAY_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
        }

        return retrofitBuilder
            .build()
            .create(PixabayApiService::class.java)
    }
}
