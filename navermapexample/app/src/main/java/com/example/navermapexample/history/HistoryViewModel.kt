package com.example.navermapexample.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navermapexample.main.ApplicationGraph
import com.example.navermapexample.room.RouteEntity

class HistoryViewModel : ViewModel() {
    val reposiotry = ApplicationGraph.getRouteRepository()

    var routeList: MutableLiveData<List<RouteEntity>> = MutableLiveData<List<RouteEntity>>().apply {
        value = ArrayList()
    }

    public fun getAllRoute(){
        reposiotry.getAllRoute(routeList)
    }



}