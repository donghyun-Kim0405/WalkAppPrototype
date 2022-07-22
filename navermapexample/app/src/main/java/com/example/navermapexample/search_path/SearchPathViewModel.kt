package com.example.navermapexample.search_path

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navermapexample.main.MainRepository
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchPathViewModel : ViewModel(){
    private val repository: MainRepository = MainRepository()
    public var markers: ArrayList<Marker> = ArrayList()
    public var markerLatLng = ArrayList<LatLng>()
    public var liveData: MutableLiveData<ArrayList<List<Double>>> = MutableLiveData<ArrayList<List<Double>>>().apply {
        value = ArrayList()
    }

    public fun requestGEOCoding(str: String){
        Log.e(TAG, str)
        CoroutineScope(Dispatchers.IO).launch {
            repository.searchAddress(str)
        }
    }

    /*public fun findPath(){
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 0..markerLatLng.size -2) {
                repository.findPath(markerLatLng.get(i), markerLatLng.get(i+1), liveData)
            }

        }
    }*/

    @SuppressLint("LongLogTag")
    public fun findPathByTmap(){
        for (i in 0..markers.size -2) {
            Log.e(TAG+"marker position", markers.get(i).position.toString() )
            repository.findPathByTmap(markers.get(i).position, markers.get(i+1).position, liveData)
        }
    }




    companion object{
        private const val TAG = "MainViewModel"
    }

}