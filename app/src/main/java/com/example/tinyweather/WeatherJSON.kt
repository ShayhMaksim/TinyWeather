package com.example.tinyweather

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

class WeatherJSON {

    // Current Data
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

    // Week info
    data class WeekWeatherInfo (
        @SerializedName("lat")
        val lat:Float,
        @SerializedName("lon")
        val lon:Float,
        @SerializedName("timezone")
        val timezone:String,
        @SerializedName("timezone_offset")
        val timezone_offset:Long,
        @SerializedName("current")
        val current:Current,
        @SerializedName("hourly")
        val hourly: ArrayList<Hourly>,
        @SerializedName("daily")
        val daily: ArrayList<Daily>,
        @SerializedName("alert")
        val alert: ArrayList<Alert>,
    )

    data class Alert(
        val sender_name:String,
        val event:String,
        val start:Long,
        val end:Long,
        val description: String,
        val tags: ArrayList<String>,
    )

    data class Current(
        @SerializedName("dt")
        val dt:Long,
        @SerializedName("sunrise")
        val sunrise: Int,
        @SerializedName("sunset")
        val sunset: Int,
        @SerializedName("temp")
        val temp: Float,
        @SerializedName("feels_like")
        val feels_like: Float,
        @SerializedName("pressure")
        val pressure: Float,
        @SerializedName("humidity")
        val humidity: Float,
        @SerializedName("dew_point")
        val dew_point:Float,
        @SerializedName("uvi")
        val uvi:Float,
        @SerializedName("clouds")
        val clouds: Int,
        @SerializedName("visibility")
        val visibility: Int,
        @SerializedName("wind_speed")
        val wind_speed:Float,
        @SerializedName("wind_deg")
        val wind_deg:Float,
        @SerializedName("wind_gust")
        val wind_gust:Float,
        @SerializedName("weather")
        val weather: ArrayList<CurrWeather>
    )

    data class Hourly(
        @SerializedName("dt")
        val dt:Long,
        @SerializedName("temp")
        val temp: Float,
        @SerializedName("feels_like")
        val feels_like: Float,
        @SerializedName("pressure")
        val pressure: Float,
        @SerializedName("humidity")
        val humidity: Float,
        @SerializedName("dew_point")
        val dew_point: Float,
        @SerializedName("uvi")
        val uvi: Float,
        @SerializedName("clouds")
        val clouds: Int,
        @SerializedName("visibility")
        val visibility: Int,
        @SerializedName("wind_speed")
        val wind_speed: Float,
        @SerializedName("wind_deg")
        val wind_deg: Float,
        @SerializedName("wind_gust")
        val wind_gust: Float,
        @SerializedName("weather")
        val weather: ArrayList<CurrWeather>,
        @SerializedName("pop")
        val pop: Float,
        @SerializedName("rain")
        val rain:Rain,
        @SerializedName("snow")
        val snow:Snow,
    )

    data class Rain(
        @SerializedName("h1")
        val h1: Float,
    )

    data class Snow(
        @SerializedName("h1")
        val h1: Float,
    )

    data class Daily(
        @SerializedName("dt")
        val dt:Long,
        @SerializedName("sunrise")
        val sunrise: Int,
        @SerializedName("sunset")
        val sunset: Int,
        @SerializedName("moonrise")
        val moonrise: Int,
        @SerializedName("moonset")
        val moonset: Int,
        @SerializedName("moon_phase")
        val moon_phase: Float,
        @SerializedName("temp")
        val temp: Temp,
        @SerializedName("feels_like")
        val feels_like: Feel_like,
        @SerializedName("pressure")
        val pressure: Float,
        @SerializedName("humidity")
        val humidity: Float,
        @SerializedName("dew_point")
        val dew_point: Float,
        @SerializedName("wind_speed")
        val wind_speed: Float,
        @SerializedName("wind_deg")
        val wind_deg: Float,
        @SerializedName("wind_gust")
        val wind_gust: Float,
        @SerializedName("weather")
        val weather: ArrayList<CurrWeather>,
        @SerializedName("clouds")
        val clouds: Int,
        @SerializedName("pop")
        val pop: Float,
        @SerializedName("rain")
        val rain: Float,
        @SerializedName("snow")
        val snow: Float,
        @SerializedName("uvi")
        val uvi: Float,
    )

    data class Temp(
        @SerializedName("day")
        val day:Float,
        @SerializedName("min")
        val min:Float,
        @SerializedName("max")
        val max:Float,
        @SerializedName("night")
        val night:Float,
        @SerializedName("eve")
        val eve:Float,
        @SerializedName("morn")
        val morn:Float
    )

    data class Feel_like(
        @SerializedName("day")
        val day: Float,
        @SerializedName("night")
        val night: Float,
        @SerializedName("eve")
        val eve: Float,
        @SerializedName("morn")
        val morn: Float,
    )


}

