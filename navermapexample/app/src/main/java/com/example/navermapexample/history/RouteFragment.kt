package com.example.navermapexample.history

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.navermapexample.R
import com.example.navermapexample.databinding.FragmentRouteBinding
import com.example.navermapexample.room.RouteEntity
import com.example.navermapexample.tracking.TrackingFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay

class RouteFragment : Fragment() {
    private lateinit var binding : FragmentRouteBinding
    private val viewModel : RouteFragmentViewModel by lazy {
        ViewModelProvider(this).get(RouteFragmentViewModel::class.java)
    }

    private var path: PathOverlay? = null
    private lateinit var globalMap: NaverMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.routeEntity = arguments?.getSerializable("routeEntity") as RouteEntity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_route, container, false)
        setMapCallback()

        return binding.root
    }

    private fun setPathConfig(){
        path = PathOverlay().apply {
            color = Color.RED
            outlineColor = Color.RED
            width = 5
            patternInterval = 30
        }
    }


    private fun setMapCallback(){
        binding.mapView.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(naverMap: NaverMap) {
                globalMap = naverMap
                setPathConfig()
                createPolyLine(viewModel.routeEntity!!)
            }
        })
    }

    private fun createPolyLine(routeEntity: RouteEntity) {
        var arr_latlng: ArrayList<LatLng> = ArrayList()
        for (locationEntity in routeEntity.gpsdata) {
            Log.e(TAG, routeEntity.gpsdata.get(0).latitute.toString())
            arr_latlng.add(LatLng(locationEntity.latitute.toDouble(), locationEntity.longitude.toDouble()))
        }
        path?.coords = arr_latlng
        path?.map = globalMap

        setMapCenter(LatLng(routeEntity.gpsdata.get(0).latitute,routeEntity.gpsdata.get(0).longitude))
    }
    private fun setMapCenter(latlng: LatLng){
        Log.e(TAG, "setMapCenter")
        globalMap.cameraPosition = CameraPosition(latlng, 17.0)
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    companion object{
        private const val TAG = "RouteFragment"

        fun getInstance(routeEntity: RouteEntity) : RouteFragment{
            val bundle = Bundle().apply {
                putSerializable("routeEntity", routeEntity)
            }
            return RouteFragment().apply { arguments = bundle }
        }
    }
}