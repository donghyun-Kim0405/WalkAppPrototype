package com.example.navermapexample.location

import android.content.Context

class FusedLocationModule(private val context: Context) {
    fun createFusedLocationManager(): FusedLocationManager {
        return FusedLocationManagerImpl(context = context)
    }
}