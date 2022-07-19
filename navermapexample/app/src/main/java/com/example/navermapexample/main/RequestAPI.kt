package com.example.navermapexample.main

import android.os.Build.ID
import android.provider.Contacts.SettingsColumns.KEY
import com.example.navermapexample.main.GlobalConfig.naver_api_id
import com.example.navermapexample.main.GlobalConfig.naver_api_key
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface RequestAPI {

    @Headers("X-NCP-APIGW-API-KEY-ID: $naver_api_id ", "X-NCP-APIGW-API-KEY: $naver_api_key ")
    @GET("map-geocode/v2/geocode")
    fun searchAddress(@Query("query") query: String): Call<AddressResponse>

}