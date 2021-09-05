package com.example.tinyweather



import android.Manifest


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location

import android.location.LocationManager


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.location.*
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.lang.StrictMath.round
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class MainActivity() : AppCompatActivity() {
    data class CalendarInfo(val day: String, val month: String)

    //Request
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    lateinit var locationRequest: LocationRequest

    //Current location
    lateinit var location: Location

    //Weather
    private lateinit var key:String
    private lateinit var openweather: WeatherJSON.WeatherInfo
//    private lateinit var weekweather: WeatherJSON.WeekWeatherInfo

    //UI
    private lateinit var currentWeatherText: TextView
    private lateinit var weatherImage: ImageView


    //ViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        currentWeatherText = findViewById(R.id.TextView)
        weatherImage = findViewById(R.id.imageView)
        key = resources.getString(R.string.weather_key);

        recyclerView = findViewById(R.id.weekWeather)
        recyclerView.layoutManager = LinearLayoutManager(this)


        runGeo(this)


        runBlocking {
            launch(Dispatchers.IO) {
                checkLocation()

            }
        }



    }

    // ПОЛУЧИТЬ КООРДИНАТЫ ГОРОДА, А ЗАТЕМ ЭТИ КООРДИНАТЫ ОТПРАВИТЬ
    fun runGeo(activity:AppCompatActivity) = runBlocking {
        launch(Dispatchers.IO) {
            val gcd = Geocoder(activity, Locale.getDefault())
            val addresses = gcd.getFromLocationName("Ostrovtsy Moscow Region",1)
        }
    }

    fun runWeather(location: Location) = runBlocking {
        launch(Dispatchers.IO) {
            getUrlWeather(location, key)
            val Data: CalendarInfo = toDataString(openweather.dt)
            currentWeatherText.text = "${Data.day} ${monthNamesRussia[Data.month.toInt()]}\nТемпература на улице ${round(openweather.main.temp-273)}"


        }
    }


    private fun toDataString(dateFormat: Long): CalendarInfo {
        val sdfDay = SimpleDateFormat("dd")
        val sdfMonth = SimpleDateFormat("MM")
        val date = Date(dateFormat * 1000)
        return CalendarInfo(sdfDay.format(date),sdfMonth.format(date))
    }


    private fun getUrlWeather(location: Location, key: String) {
        openweather =
            openweatherData("https://api.openweathermap.org/data/2.5/weather?lat=${location.latitude}&lon=${location.longitude}&appid=${key}")
        var weekweather =
            weakweatherData("https://api.openweathermap.org/data/2.5/onecall?lat=${location.latitude}&lon=${location.longitude}&exclude=minutely&appid=${key}")

        val daily:List<WeatherJSON.Daily> = weekweather.daily

        if (weekweather!=null) {
            recyclerView.adapter = WeekRecyclerAdapter(daily)
        }

        var image: Bitmap? = null
        try {
            val `in` =
                URL("https://openweathermap.org/img/wn/${openweather.weather[0].icon}@2x.png").openStream()
            image = BitmapFactory.decodeStream(`in`)
            weatherImage.setImageBitmap(image)
        } catch (e: Exception) {
            Log.e("Error Message", e.message.toString())
            e.printStackTrace()
        }

    }



    private fun openweatherData(url: String):WeatherJSON.WeatherInfo {
        val result = URL(url).readText()
        var gson = Gson()
        return gson.fromJson(result, WeatherJSON.WeatherInfo::class.java)
    }

    private fun weakweatherData(url: String):WeatherJSON.WeekWeatherInfo {
        val result = URL(url).readText()
        var gson = Gson()
        return gson.fromJson(result, WeatherJSON.WeekWeatherInfo::class.java)
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

                    runWeather(location)

//                    addresses = geoCoder.getFromLocation(
//                        locationResult.lastLocation.latitude,
//                        locationResult.lastLocation.longitude,
//                        1
//                    )
//                    if (addresses != null && addresses.isNotEmpty()) {
//                        val address: String = addresses[0].getAddressLine(0)
//                        val city: String = addresses[0].locality
//                        val state: String = addresses[0].adminArea
//                        val country: String = addresses[0].countryName
//                        val postalCode: String = addresses[0].postalCode
//                        val knownName: String = addresses[0].featureName
//                        Log.e("location", "$address $city $state $postalCode $country $knownName")
//                    }
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

