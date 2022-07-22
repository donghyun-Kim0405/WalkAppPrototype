package com.example.navermapexample.main

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.navermapexample.gps.GpsModule
import com.example.navermapexample.location.FusedLocationModule
import com.example.navermapexample.repository.RouteReposiotry
import com.example.navermapexample.repository.RouteRepositoryModule
import com.example.navermapexample.room.RoomDB

class ApplicationGraph(private val context: Context){
    private val fusedLocationManagerInternal by lazy { FusedLocationModule(context).createFusedLocationManager() }
    private val gpsManagerInternal by lazy { GpsModule(context).createGpsManager() }
    private val roomDBInternal by lazy { RoomDB.getInstance(context)}
    private val routeRepositoryInternal by lazy { RouteRepositoryModule().createRouteRepository() }

    companion object{
        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        private var graph: ApplicationGraph? = null

        @JvmStatic
        fun init(context: Context) {
            if (graph == null) {
                graph = ApplicationGraph(context.applicationContext)
            }
        }

        fun getFusedLocationManager() = graph!!.fusedLocationManagerInternal
        fun getGpsManager() = graph!!.gpsManagerInternal
        fun getRoomDB() = graph!!.roomDBInternal
        fun getRouteRepository() = graph!!.routeRepositoryInternal
    }//companion object
}