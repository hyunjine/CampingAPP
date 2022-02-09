package com.example.camping.util.data

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.example.camping.util.data.SERVICE.MIN_DISTANCE_CHANGE_FOR_UPDATES
import com.example.camping.util.data.SERVICE.MIN_TIME_BW_UPDATES

class GPSTracker(private val context: Context): Service(), LocationListener {

    private lateinit var locationManager: LocationManager
    private var location: Location? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    init {
        try {
            locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (isGPSEnabled && isNetworkEnabled) {
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this
                        )

                        if (locationManager != null) {
                            location =
                                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            setLocation()
                        }
                    }
                    if (isGPSEnabled) {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this
                        )
                        if (locationManager != null) {
                            location =
                                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            setLocation()
                        }
                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun setLocation() {
        if (location != null) {
            latitude = location!!.latitude
            longitude = location!!.longitude
        }
    }

    fun getLatitude(): Double? = if (location != null) latitude else null
    fun getLongitude(): Double? = if (location != null) longitude else null

    fun stopUsingGps() {
        if (locationManager != null)
            locationManager.removeUpdates(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null
    override fun onLocationChanged(location: Location) {}
}