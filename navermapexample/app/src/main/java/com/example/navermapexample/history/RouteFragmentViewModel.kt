package com.example.navermapexample.history

import androidx.lifecycle.ViewModel
import com.example.navermapexample.room.RouteEntity
import okhttp3.Route

class RouteFragmentViewModel : ViewModel() {
    public var routeEntity: RouteEntity? = null
}