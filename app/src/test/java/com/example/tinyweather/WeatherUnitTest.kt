package com.example.tinyweather

import com.google.gson.Gson
import org.json.JSONArray
import org.junit.Test

import org.junit.Assert.*
import java.io.BufferedReader
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class WeatherUnitTest {
    @Test
    fun readKey_isCorrect() {
        var path: String = System.getProperty("user.dir")
        path += "\\src\\main\\keys\\keys.json"

        val value: Weather = Weather (path)

        assertEquals(value.key, "f69b625a585aaf6eef616683810146f3")

    }
}
