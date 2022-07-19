package com.example.navermapexample.main

import android.R.attr.data
import android.util.Log
import android.widget.TextView
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.TimeUnit


class MainRepository {

    public fun searchAddress(query: String) {
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        val api = retrofit.create(RequestAPI::class.java)
        api.searchAddress(query).enqueue(object : Callback<AddressResponse> {
            override fun onResponse(
                call: Call<AddressResponse>,
                response: Response<AddressResponse>
            ) {
                Log.e(TAG, response.body().toString())
            }

            override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                Log.e(TAG, call.toString())
            }

        })

    }

    companion object{
        private const val TAG = "MainRepository"
        private const val BASE_URL = "https://naveropenapi.apigw.ntruss.com/"
    }


}