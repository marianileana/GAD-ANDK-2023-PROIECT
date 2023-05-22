package com.example.travelmemories

data class WeatherData(
    val main: MainData,
    val weather: List<WeatherInfo>
)

data class MainData(
    val temp: Double,
    val humidity: Int
)

data class WeatherInfo(
    val main: String,
    val description: String
)
