package com.example.navermapexample.repository

import android.app.Application
import com.example.navermapexample.main.ApplicationGraph
import com.example.navermapexample.room.RoomDB

class RouteRepositoryModule() {

    public fun createRouteRepository(): RouteReposiotry{
        val roomDB = ApplicationGraph.getRoomDB()
        return RouteReposiotry(roomDB!!)
    }
}