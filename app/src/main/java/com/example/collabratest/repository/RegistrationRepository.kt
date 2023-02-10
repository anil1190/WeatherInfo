package com.example.collabratest.repository

import androidx.lifecycle.MutableLiveData
import com.example.collabratest.database.UserDao
import com.example.collabratest.database.remote.ApiProvider
import com.example.collabratest.modals.UserEntity
import com.example.collabratest.modals.WeatherModel
import com.example.collabratest.modals.forecast.ForecastResponse
import javax.inject.Inject

class RegistrationRepository @Inject constructor(private val dao : UserDao, private val apiProvider: ApiProvider                                  ) {
    //it is the local database operations
    fun insertUser(userEntity: UserEntity) = dao.insertUser(userEntity)
    fun getAllUsers() : MutableList<UserEntity>  = dao.getAllUsers()

    // it is the remote database operations
    suspend fun getCurrentWeather(lat:Double,lon:Double,appId:String) : WeatherModel =
                                               apiProvider.getCurrentWeather(lat,lon,appId)

    suspend fun getWeatherList(lat:Double,lon:Double,appId:String) : ForecastResponse =
                                            apiProvider.getWeatherList(lat,lon,appId)
}