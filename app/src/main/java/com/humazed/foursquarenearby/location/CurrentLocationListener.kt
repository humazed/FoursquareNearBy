package com.humazed.foursquarenearby.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.*

class CurrentLocationListener private constructor(context: Context) : LiveData<Location>() {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            value = result?.lastLocation
        }
    }

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient?.lastLocation?.addOnSuccessListener { value = it }
        createLocationRequest()
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest().apply {
            smallestDisplacement = 10f
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        }
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }


    companion object {

        @Volatile
        private var INSTANCE: CurrentLocationListener? = null

        fun getInstance(context: Context): CurrentLocationListener =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildLocationUpdateListener(context).also { INSTANCE = it }
            }

        private fun buildLocationUpdateListener(context: Context) =
            CurrentLocationListener(context)
    }
}