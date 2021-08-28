package com.example.tinyweather

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.junit.Test

import org.junit.Assert.*
import java.io.BufferedReader
import java.io.File
import java.net.URL

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class WeatherUnitTest {
    @Test
    fun readKey_isCorrect() {
//        val result = URL().readText()
//
//        var gson = Gson()
//
//        var key = gson.fromJson(result, WeatherJSON.WeatherInfo::class.java)

    }

    @Test
    fun readData_isCorrect() {
        var path: String = System.getProperty("user.dir")
        path += "\\src\\main\\keys\\keys.json"

        val weather: Weather = Weather (path)

        val result = URL("https://api.openweathermap.org/data/2.5/weather?lat=60&lon=62&appid=${weather.key}").readText()
        println(result)

        var gson = Gson()

        var key = gson.fromJson(result, WeatherJSON.WeatherInfo::class.java)

        println(key.clouds.all)
//        var key: WeatherJSON.WeatherInfo = gson.fromJson(result, WeatherJSON.WeatherInfo::class.java)
//        print(key.clouds.all)
    }
}
