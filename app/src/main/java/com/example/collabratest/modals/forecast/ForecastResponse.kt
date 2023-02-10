package com.example.collabratest.modals.forecast

data class ForecastResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WeatherList>,
    val message: Int
)