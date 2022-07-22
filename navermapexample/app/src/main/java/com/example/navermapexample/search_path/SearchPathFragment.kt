package com.example.navermapexample.search_path

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
import com.example.navermapexample.databinding.FragmentSearchPathBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay

class SearchPathFragment : Fragment() {

    private lateinit var binding: FragmentSearchPathBinding
    private val viewModel: SearchPathViewModel by lazy { ViewModelProvider(this).get(SearchPathViewModel::class.java) }

    //map api variable
    private var path: PathOverlay? = null
    private lateinit var globalMap: NaverMap
    private var pathPoint = ArrayList<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_path, container, false)
        setMapCallback()
        setBtnListener()
        setObserver()

        return binding.root
    }

    private fun setObserver(){
        viewModel.liveData.observe(viewLifecycleOwner, Observer{
            if(it.size > 0){
                createPolyLine(it)
            }
        })
    }

    private fun setBtnListener(){
        binding.btnSearch.setOnClickListener { viewModel.requestGEOCoding(binding.editText.text.toString()) }
        binding.btnRemoveMarker.setOnClickListener {
            for (marker in viewModel.markers) marker.map = null
            for(marker in pathPoint) marker.map = null
            pathPoint = ArrayList()

            viewModel.liveData.value = ArrayList()
            viewModel.markers = ArrayList()
            viewModel.markerLatLng = ArrayList()
            path!!.map = null

        }
        binding.btnFindPath.setOnClickListener {
            //viewModel.findPath()
            viewModel.findPathByTmap()
        }
    }

    private fun setMapClickListener(){
        globalMap.setOnMapClickListener { pointF, latLng ->
            Log.e(latLng.latitude.toString(), latLng.longitude.toString())
            Marker().apply {
                position = LatLng(latLng.latitude, latLng.longitude)
                map = globalMap
            }.also {
                viewModel.markers.add(it)
                viewModel.markerLatLng.add(latLng)
            }
        }
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
                setMapClickListener()
                setPathConfig()
            }
        })
    }


    private fun createPolyLine(elements: ArrayList<List<Double>>){
        var latlngs: ArrayList<LatLng> = ArrayList()


        for (ele in elements) {
            Log.e(ele.get(1).toString(), ele.get(0).toString())
            latlngs.add(LatLng(ele.get(1).toDouble(), ele.get(0).toDouble()))

        }
        path?.coords = latlngs as List<LatLng>
        path?.map = globalMap
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
        fun getInstance(): SearchPathFragment {
            return SearchPathFragment()
        }
    }
}