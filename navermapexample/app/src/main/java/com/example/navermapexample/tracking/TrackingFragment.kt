package com.example.navermapexample.tracking

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.navermapexample.R
import com.example.navermapexample.databinding.FragmentTrackingBinding
import com.example.navermapexample.location.FusedLocationManager
import com.example.navermapexample.main.ApplicationGraph
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.PathOverlay

class TrackingFragment(val fusedLocationManager: FusedLocationManager) : Fragment() {

    private lateinit var binding: FragmentTrackingBinding
    private val viewModel: TrackingViewModel by lazy { ViewModelProvider(this).get(TrackingViewModel::class.java) }
    private lateinit var globalMap: NaverMap
    private var path: PathOverlay? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tracking, container, false)
        //checkPermission()
        setMapCallback()
        setBtnListener()
        setObserver()
        return binding.root
    }
    private fun createPolyLine(arr_latlng: ArrayList<LatLng>) {
        if(arr_latlng.size<2) return
        Log.e(TAG, "polyline called")
        path?.coords = arr_latlng
        path?.map = globalMap
    }

    private fun setObserver(){

        viewModel.route.observe(viewLifecycleOwner, Observer{
            Log.e(TAG, "observer called")
            createPolyLine(it)
        })

        viewModel.currentLocation.observe(viewLifecycleOwner, Observer {
            setMapCenter(it)
            createPolyLine(viewModel.route.value!!)
        })
    }

    private fun setMapCenter(latlng: LatLng){
        Log.e(TAG, "setMapCenter")
        globalMap.cameraPosition = CameraPosition(latlng, 17.0)
    }

    private fun setBtnListener(){
        binding.btnController.setOnClickListener {
            if (binding.btnController.text.toString().equals("start")) {
                viewModel.start()
                binding.btnController.text = "stop"
            }else{
                viewModel.stop()
                binding.btnController.text = "start"
            }
        }
    }

    private fun setMapCallback(){
        binding.mapView.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(naverMap: NaverMap) {
                globalMap = naverMap
                path = PathOverlay().apply {
                    color = Color.RED
                    outlineColor = Color.RED
                    width = 5
                    patternInterval = 30
                }
            }
        })
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


    /*private fun checkPermission(){
        if(!fusedLocationManager.isLocationEnabledOnDevice()) fusedLocationManager.enableDeviceLocation()
    }*/

    companion object{
        private const val TAG = "TrackingFragment"
        fun getInstance(): TrackingFragment{
            return TrackingFragment(ApplicationGraph.getFusedLocationManager())
        }
    }

}