package com.example.democorekotlin.model.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ModelWeather(
    val coord: Coord?,
    val weather: List<Weather>?,
    val base: String?,
    val main: Main?,
    val visibility: Int?,
    val wind: Wind?,
    val clouds: Clouds?,
    val dt: Int?,
    val sys: Sys?,
    val timezone: Int?,
    val id: Int?,
    val name: String?,
    val cod: Int?
)

data class Coord(
    val lon: Double?,
    val lat: Double?
)

data class Weather(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?
)

data class Main(
    val temp: Double?,
    @JsonProperty("feels_like")
    val feelsLike: Double?,
    @JsonProperty("temp_min")
    val tempMin: Double?,
    @JsonProperty("temp_max")
    val tempMax: Double?,
    val pressure: Double?,
    val humidity: Double?,
    @JsonProperty("sea_level")
    val seaLevel: Double?,
    @JsonProperty("grnd_level")
    val grndLevel: Double?
)

data class Wind(
    val speed: Double?,
    val deg: Double?,
    val gust: Double?
)

data class Clouds(
    val all: Int?
)

data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String?,
    val sunrise: Int?,
    val sunset: Int?
)