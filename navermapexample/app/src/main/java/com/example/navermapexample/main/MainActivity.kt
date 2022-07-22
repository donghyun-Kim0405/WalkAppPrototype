package com.example.navermapexample.main

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.navermapexample.R
import com.example.navermapexample.databinding.ActivityMainBinding
import com.example.navermapexample.history.HistoryFragment
import com.example.navermapexample.history.RouteFragment
import com.example.navermapexample.room.RouteEntity
import com.example.navermapexample.search_path.SearchPathFragment
import com.example.navermapexample.tracking.TrackingFragment

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay

class MainActivity : AppCompatActivity(), HistoryFragment.Listener{
    private lateinit var binding: com.example.navermapexample.databinding.ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(supportFragmentManager.findFragmentById(R.id.fragment_container)==null) supportFragmentManager.beginTransaction().add(R.id.fragment_container, SearchPathFragment.getInstance()).commit()
        setBottomNav()
    }//onCreate()

    private fun setBottomNav(){
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.icon_search ->{
                    changeFragment(SearchPathFragment.getInstance())
                    true
                }
                R.id.icon_tracking ->{
                    changeFragment(TrackingFragment.getInstance())
                    true
                }
                R.id.icon_history ->{
                    changeFragment(HistoryFragment.getInstance())
                    true
                }
                else -> true
            } //when
        }//setOnItemSelectedListener
    }//setBottomNav()

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    override fun onRouteSelected(item: RouteEntity) {
        changeFragment(RouteFragment.getInstance(item))
    }

}