package com.example.collabratest.database.remote

import com.example.collabratest.modals.WeatherModel
import com.example.collabratest.modals.forecast.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiProvider {

    @GET("weather/")
    suspend fun getCurrentWeather(
        @Query("lat") lat : Double,
        @Query("lon") lon : Double,
        @Query("appid") appId : String
    ) : WeatherModel

    @GET("forecast/")
    suspend fun getWeatherList(
        @Query("lat") lat : Double,
        @Query("lon") lon : Double,
        @Query("appid") appId : String
    ) : ForecastResponse
}