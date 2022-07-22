package com.example.navermapexample.gps

import android.graphics.Point
import android.location.Location
import com.naver.maps.geometry.LatLng

class GpsCalculator {
    companion object{
        fun getDistance(startPoint: Location, endPoint: Location): Float {
            return startPoint.distanceTo(endPoint)
        }
    }
}