package com.example.navermapexample.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.navermapexample.AddressResponse
import com.example.navermapexample.ResultPath
import com.example.navermapexample.dto.RequestTmapDto
import com.example.navermapexample.main.GlobalConfig.tmap_api_key
import com.google.gson.GsonBuilder
import com.naver.maps.geometry.LatLng
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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


    public fun findPathByTmap(
        startPoint: LatLng,
        endpoint: LatLng,
        liveData: MutableLiveData<ArrayList<List<Double>>>
    ){
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_TMAP)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        val api = retrofit.create(RequestAPI::class.java)

        api.getPathByTMap(
            tmap_api_key,
            RequestTmapDto(
            startPoint.longitude.toString(),
            startPoint.latitude.toString(),
            endpoint.longitude.toString(),
            endpoint.latitude.toString(),
            "test",
            "test"
            )
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.body() != null) {
                    val str = response.body()!!.string() as String
                    val path = liveData.value
                    try {
                        val features = JSONObject(str).getJSONArray("features")


                        for (i in 0..features.length()-1) {
                            val feature = features.getJSONObject(i)

                            val geometry = feature.getJSONObject("geometry")
                            val coordinates = geometry.getJSONArray("coordinates")


                            var geocode = coordinates.toString()
                            Log.e("TEST", geocode.toString())

                            geocode = geocode.replace("[", "")
                            geocode = geocode.replace("]", "")
                            var latLng = geocode.split(",")

                            for (i in 0..latLng.size-1 step 2) {
                                path!!.add(listOf(latLng[i].toDouble(), latLng[i+1].toDouble()))
                            }

                        }

                        liveData.postValue(path)

                    } catch (e: Exception) {
                        Log.e(TAG, "ERROR TO PARSING")
                        Log.e(TAG, e.message.toString())
                        e.printStackTrace()

                    }



                }else{
                    Log.e(TAG, "response body is null!!")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, t.message.toString())

            }

        })

    }

    companion object{
        private const val TAG = "MainRepository"
        private const val BASE_URL = "https://naveropenapi.apigw.ntruss.com/"
        private const val BASE_URL_TMAP = "https://apis.openapi.sk.com/"
    }


}