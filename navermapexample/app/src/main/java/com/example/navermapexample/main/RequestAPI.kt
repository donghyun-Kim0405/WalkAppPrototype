package com.example.navermapexample.main

import com.example.navermapexample.AddressResponse
import com.example.navermapexample.dto.RequestTmapDto
import com.example.navermapexample.main.GlobalConfig.naver_api_id
import com.example.navermapexample.main.GlobalConfig.naver_api_key
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface RequestAPI {

    @Headers("X-NCP-APIGW-API-KEY-ID: $naver_api_id ", "X-NCP-APIGW-API-KEY: $naver_api_key ")
    @GET("map-geocode/v2/geocode")
    fun searchAddress(@Query("query") query: String): Call<AddressResponse>

    //@Headers("Accept: application/json", "Content-Type: application/json", "appKey: l7xx1317e6cad24d4f0d8048aa7336e5623b")

    @POST("tmap/routes/pedestrian?version=1")
    fun getPathByTMap(
        @Header("appKey") appKey: String,
        @Body dto : RequestTmapDto
    ) : Call<ResponseBody>


}