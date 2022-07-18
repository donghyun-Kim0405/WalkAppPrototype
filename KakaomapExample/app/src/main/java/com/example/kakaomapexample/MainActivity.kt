package com.example.kakaomapexample

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapPOIItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.coroutines.CoroutineContext


/*
원하는 지명을 입력후 버튼을 누르면 마커로 보이기

MainActivity - RESTAPI_KEY : kakao developers에서 발급받은 REST API KEY입력
Manifest - meta data : kakao developers에서 발급받은 NATIVE API KEY입력


getPlaceData("원하는 지명")수행 -> 결과로 콜백에서 createMarker호출하여 mapView에 marker추가
 */


class MainActivity : AppCompatActivity() {

    private val PERMISSIONS=arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
         android.Manifest.permission.INTERNET,android.Manifest.permission.ACCESS_COARSE_LOCATION)

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapContainer : ViewGroup
    private val GEOCODE_URL="http://dapi.kakao.com/v2/local/search/address.json?query="
    private val BASE_URL = "https://dapi.kakao.com/"
    private val RESTAPI_KEY=""
    private lateinit var btn_search:Button
    private lateinit var editText : EditText
    private lateinit var mapView : MapView


    //---------------------------------------------------------------------------------------------------------------
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED||
            checkSelfPermission(android.Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED||
            checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                PERMISSIONS, 100)}

        mapContainer = findViewById(R.id.map_view)
        mapView = MapView(this)
        mapContainer.addView(mapView)

        editText = findViewById(R.id.editText)
        btn_search = findViewById(R.id.btn_search)
        btn_search.setOnClickListener {
            val str = editText.text.toString()
            getPlaceData(str)
        }

    }
    //---------------------------------------------------------------------------------------------------------------
    private fun createMarker(lat: Double, lon:Double) : MapPOIItem{
        var newMarker = MapPOIItem()
        newMarker.itemName = "Default Marker"
        //newMarker.mapPoint = MapPoint.mapPointWithGeoCoord(37.50433469932041, 127.07693931122469)
        newMarker.mapPoint = MapPoint.mapPointWithGeoCoord(lat, lon)
        newMarker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        newMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        return newMarker
    }

    //---------------------------------------------------------------------------------------------------------------
    private fun getPlaceData(target : String){
        Log.e("APIKEY", RESTAPI_KEY)


        val retrofit = Retrofit.Builder() // Retrofit 구성
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(RequestAPI::class.java)
        val call = api.getPlaceInfo(RESTAPI_KEY, target)

        call.enqueue(object : Callback<ResultPlaceInfo> {
            override fun onResponse(
                call: Call<ResultPlaceInfo>,
                response: Response<ResultPlaceInfo>
            ) {
                // 통신 성공 (검색 결과는 response.body()에 담겨있음)
                Log.d("Test", "Raw: ${response.raw()}")
                Log.d("Test", "Body: ${response.body()}")
                if(response.body()!=null){
                    var result : ResultPlaceInfo = response.body()!!
                    for(info in result.documents){
                           mapView.addPOIItem(createMarker(info.y.toDouble(), info.x.toDouble()))
                    }
                }
            }

            override fun onFailure(call: Call<ResultPlaceInfo>, t: Throwable) {
                // 통신 실패
                Log.w("MainActivity", "통신 실패: ${t.message}")
            }
        })
    }

    //---------------------------------------------------------------------------------------------------------------

}