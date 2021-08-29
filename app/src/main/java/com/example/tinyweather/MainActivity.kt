package com.example.tinyweather



import android.Manifest
import android.annotation.SuppressLint

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location

import android.location.LocationManager
import android.os.AsyncTask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Contacts

import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

import com.google.android.gms.location.*
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.net.URL
import java.util.*


class MainActivity() : AppCompatActivity() {
    //Request
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    lateinit var locationRequest: LocationRequest

    //Current location
    lateinit var location: Location

    //Weather
    private lateinit var key:String
    private lateinit var openweather: WeatherJSON.WeatherInfo

    //UI
    private lateinit var currentWeatherText: TextView
    private lateinit var weatherImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        currentWeatherText = findViewById(R.id.TextView)
        weatherImage = findViewById(R.id.imageView)

        key = resources.getString(R.string.weather_key);


        runBlocking {
            launch(Dispatchers.IO) {
                checkLocation()
            }
        }
    }


    fun runWeather() = runBlocking {
        launch(Dispatchers.IO) {
            getUrlWeather(location, key)
            currentWeatherText.text = openweather.main.temp.toString()

        }
    }


    private fun getUrlWeather(location: Location, key: String) = runBlocking  {
         val job = launch(Dispatchers.IO) {
            openweather  = openweatherData("https://api.openweathermap.org/data/2.5/weather?lat=${location.latitude}&lon=${location.longitude}&appid=${key}")

             launch {
                 var image: Bitmap? = null
                 try {
                     val `in` = URL("https://openweathermap.org/img/wn/${openweather.weather[0].icon}@2x.png").openStream()
                     image = BitmapFactory.decodeStream(`in`)
                     weatherImage.setImageBitmap(image)
                 } catch (e: Exception) {
                     Log.e("Error Message", e.message.toString())
                     e.printStackTrace()
                 }
             }

        }
    }




    private fun openweatherData(url: String):WeatherJSON.WeatherInfo {
        val result = URL(url).readText()
        var gson = Gson()
        return gson.fromJson(result, WeatherJSON.WeatherInfo::class.java)
    }

    private fun checkLocation(){
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showAlertLocation()
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocationUpdates()
    }

    private fun showAlertLocation() {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage("Your location settings is set to Off, Please enable location to use this application")
        dialog.setPositiveButton("Settings") { _, _ ->
            val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(myIntent)
        }
        dialog.setNegativeButton("Cancel") { _, _ ->
            finish()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun getLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
        locationRequest.interval = 50000
        locationRequest.fastestInterval = 50000
        locationRequest.smallestDisplacement = 170f //170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //according to your app
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                if (locationResult.locations.isNotEmpty()) {
                    /*val location = locationResult.lastLocation
                    Log.e("location", location.toString())*/
                    val addresses: List<Address>?
                    val geoCoder = Geocoder(applicationContext, Locale.getDefault())

                    location = locationResult.locations[0]

                    runWeather()

                    addresses = geoCoder.getFromLocation(
                        locationResult.lastLocation.latitude,
                        locationResult.lastLocation.longitude,
                        1
                    )
                    if (addresses != null && addresses.isNotEmpty()) {
                        val address: String = addresses[0].getAddressLine(0)
                        val city: String = addresses[0].locality
                        val state: String = addresses[0].adminArea
                        val country: String = addresses[0].countryName
                        val postalCode: String = addresses[0].postalCode
                        val knownName: String = addresses[0].featureName
                        Log.e("location", "$address $city $state $postalCode $country $knownName")
                    }
                }
            }
        }
    }

    // Start location updates
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    // Stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // Stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    // Start receiving location update when activity  visible/foreground
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }


}

