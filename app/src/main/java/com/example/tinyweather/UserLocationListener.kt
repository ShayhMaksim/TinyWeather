package com.example.tinyweather

import android.location.Location
import android.location.LocationListener
import android.os.Bundle

class UserLocationListener() : LocationListener
{
    private var mylocation: Location

    init {
        mylocation = Location("me")
        mylocation!!.longitude
        mylocation!!.latitude
    }

    override fun onLocationChanged(p0: Location) {
        TODO("Not yet implemented")
        mylocation=p0
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}
}