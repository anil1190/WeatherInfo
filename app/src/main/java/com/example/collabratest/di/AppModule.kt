package com.example.collabratest.di

import android.content.Context
import androidx.room.Room
import com.example.collabratest.BuildConfig
import com.example.collabratest.MyApplication
import com.example.collabratest.database.UserDataBase
import com.example.collabratest.database.remote.ApiProvider
import com.example.collabratest.modals.UserEntity
import com.example.collabratest.modals.WeatherModel
import com.example.collabratest.uitls.Constants
import com.example.collabratest.uitls.Constants.USER_DATABASE
import com.example.collabratest.uitls.LocationLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // for local database
    @Singleton
    @Provides
    fun provide(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, UserDataBase::class.java, USER_DATABASE
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db: UserDataBase) = db.userDao()

    @Provides
    fun provideEntity() = UserEntity()

    // for remote database
    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiProvider::class.java)

    @Provides
    fun provideLocation() = LocationLiveData(MyApplication.getAppInstance())

}