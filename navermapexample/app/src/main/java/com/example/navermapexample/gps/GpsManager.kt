package com.example.navermapexample.gps


import android.location.Location


interface GpsManager {

    fun registerListener(listener: GpsManager.Listener)

    fun unregisterListener(listener: Listener)

    fun startListening()

    fun stopListening()

    fun isListening(): Boolean

    interface Listener{
        fun onGpsUpdated(location: Location)
    }
}