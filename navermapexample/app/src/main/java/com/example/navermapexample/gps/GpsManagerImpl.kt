package com.example.navermapexample.gps

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class GpsManagerImpl(val context: Context) : GpsManager{

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private val listeners = ArrayList<GpsManager.Listener>()
    private val locationCallback: LocationCallback = object : LocationCallback(){

        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            locationResult?.let {
                for (location in it.locations)
                    for(listener in listeners)
                        listener.onGpsUpdated(location)
            } ?: return
        }
    }

    init {
        locationRequest = LocationRequest().apply {
            interval = REQUEST_INTERVAL
            priority = REQUEST_PRIORITY
        }
    }

    override fun registerListener(listener: GpsManager.Listener) {
        if(listeners.contains(listener)) return
        listeners.add(listener)
    }
    override fun unregisterListener(listener: GpsManager.Listener) { listeners.remove(listener) }

    override fun startListening() {
        if(isListening()) return
        locationRequest ?: throw java.lang.Exception("locationRequest is null : LocationManager")

        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            requestLocationUpdate()
        }
        task.addOnFailureListener{exception: Exception ->
            // location settings are not satisfied
            if (exception is ResolvableApiException) {
                //show dialog for fix this
                try {
                } catch (e: java.lang.Exception) { e.printStackTrace() }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate(){
        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    override fun stopListening() {
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        fusedLocationProviderClient = null
    }

    override fun isListening(): Boolean {
        return fusedLocationProviderClient != null
    }

    companion object{
        private const val TAG = "GpsManagerImpl"
        private const val REQUEST_INTERVAL = 100L
        //private const val REQUEST_FAST_INTERVAL = 100L
        private const val REQUEST_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
}
