package com.example.rountinegps

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.*

class Recorder(context: Context) {

    private val client = LocationServices.getFusedLocationProviderClient(context)
    private val points = mutableListOf<GpsPoint>()

    private val request = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 2000
    ).build()

    private val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val loc = result.lastLocation ?: return
            points.add(
                GpsPoint(
                    loc.latitude,
                    loc.longitude,
                    System.currentTimeMillis()
                )
            )
            Log.d("Status of gps","~~~~latitude = ${loc.latitude}~ Longitude = ${loc.longitude}~ Time = ${System.currentTimeMillis()}~~~~~~~~~~~~~")
        }
    }

    @SuppressLint("MissingPermission")
    fun start() {
        points.clear()
        client.requestLocationUpdates(request, callback, null)
    }

    fun stop(): List<GpsPoint> {
        client.removeLocationUpdates(callback)
        return points.toList()
    }
}