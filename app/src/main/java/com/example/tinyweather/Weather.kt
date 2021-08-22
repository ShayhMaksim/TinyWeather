package com.example.tinyweather

import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File

class Weather {

    lateinit var key: String

    constructor(filepath:String) {
        var gson = Gson()
        val bufferedReader: BufferedReader = File(filepath).bufferedReader()
        val inputString = bufferedReader.use { it.readText() }
        var key = gson.fromJson(inputString, Key::class.java)
        this.key= key.key.toString()
    }


}