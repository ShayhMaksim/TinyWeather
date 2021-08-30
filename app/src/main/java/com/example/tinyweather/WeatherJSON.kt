package com.example.tinyweather

import com.google.gson.annotations.SerializedName
import java.util.*

class WeatherJSON {

    data class WeatherInfo (
        @SerializedName("coord")
        val coord: Coordinate,

        @SerializedName("weather")
        val weather: ArrayList<CurrWeather>,

        @SerializedName("base")
        val base: String,

        @SerializedName("main")
        val main: MainWeather,

        @SerializedName("visibility")
        val visibility: Int,

        @SerializedName("wind")
        val wind: Wind,

        @SerializedName("clouds")
        val clouds: Clouds,

        @SerializedName("dt")
        val dt: Long,

        @SerializedName("sys")
        val sys: Sys,

        @SerializedName("timezone")
        val timezone: Int,

        @SerializedName("id")
        val id: Int,

        @SerializedName("name")
        val name: String,

        @SerializedName("cod")
        val cod: Int
    )

    data class Coordinate (
        val lon: Float,
        val lat: Float
    )

    data class CurrWeather (
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
    )

    data class MainWeather (
        val temp:Float,
        val feels_like: Float,
        val temp_min:Float,
        val temp_max:Float,
        val pressure:Float,
        val humidity:Float,
        val sea_level:Float,
        val grnd_level:Float
    )

    data class Wind(
        val speed: Float,
        val deg: Float,
        val gust: Float
    )

    data class Clouds (
        val all: Int
            )

    data class Sys (
        val county: String,
        val sunrise: Int,
        val sunset: Int
    )


}

