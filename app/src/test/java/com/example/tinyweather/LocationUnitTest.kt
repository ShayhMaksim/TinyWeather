package com.example.tinyweather


import android.location.Address
import android.location.Geocoder
import com.google.gson.Gson
import org.junit.Test
import java.net.URL
import java.util.*


class LocationUnitTest {
    @Test
    fun getLocation(){
        val result = URL("").readText()
        println(result)

        var gson = Gson()

        var key = gson.fromJson(result, WeatherJSON.WeekWeatherInfo::class.java)
        print("Hello world!")
    }


}