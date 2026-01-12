package com.example.rountinegps

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.location.provider.ProviderProperties
import android.util.Log
import kotlinx.coroutines.*

class ReplayManager(context: Context) {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private var job: Job? = null
    private var speed = 1.0

    init {
        try {
            locationManager.addTestProvider(
                LocationManager.GPS_PROVIDER,
                false,  // requiresNetwork
                false,  // requiresSatellite
                false,  // requiresCell
                false,  // hasMonetaryCost
                true,   // supportsAltitude
                true,   // supportsSpeed
                true,   // supportsBearing
                ProviderProperties.POWER_USAGE_LOW,      // powerRequirement
                ProviderProperties.ACCURACY_FINE       // accuracy

            )
            Log.d("test", "1111111111111111111111111111")
        } catch (e: Exception) {
            // Provider đã tồn tại → bỏ qua
            Log.d("test", "333333333333333333333333333333")
        }

        try {
            locationManager.setTestProviderEnabled(
                LocationManager.GPS_PROVIDER, true
            )
            Log.d("test", "222222222222222222222222222222")
        } catch (e: Exception) {
            // ignore
        }
    }

    fun setSpeed(multiplier: Double) {
        speed = multiplier
    }

    fun play(points: List<GpsPoint>, onFinished: () -> Unit) {
        if (points.isEmpty()) return

        job = CoroutineScope(Dispatchers.IO).launch {
            for (i in points.indices) { // using for with purpose are run list in gpslog
                val p = points[i]

                val loc = Location(LocationManager.GPS_PROVIDER).apply {
                    latitude = p.latitude
                    longitude = p.longitude
                    accuracy = 5f
                    time = System.currentTimeMillis()
                }

                try {
                    locationManager.setTestProviderLocation(
                        LocationManager.GPS_PROVIDER, loc
                    )
                } catch (e: Exception) {
                    break
                }

                if (i < points.size - 1) {
                    val delayTime =
                        ((points[i + 1].timestamp - p.timestamp) / speed)
                            .toLong()
                            .coerceAtLeast(500)
                    delay(delayTime)
                }
            }
        }
    }

    fun pause() {
        job?.cancel()
    }
}
