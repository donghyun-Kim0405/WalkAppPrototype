package com.example.navermapexample.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.navermapexample.main.ApplicationGraph
import com.example.navermapexample.room.RoomDB
import com.example.navermapexample.room.RouteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RouteReposiotry(private val roomDB: RoomDB) {

    public fun getAllRoute(routeList: MutableLiveData<List<RouteEntity>>) {

        CoroutineScope(Dispatchers.IO).launch {
            val result = roomDB.routeDao().findAll()
            routeList.postValue(result)
        }

    }



    companion object{
        private const val TAG = "RouteRepository"
    }


}