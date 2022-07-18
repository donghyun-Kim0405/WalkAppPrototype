package com.example.kakaomapexample

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RequestAPI {

    //http://dapi.kakao.com/v2/local/search/address.json?query=
    @GET("v2/local/search/keyword.json")    // Keyword.json의 정보를 받아옴
    fun getPlaceInfo(
        @Header("Authorization") key: String,     // 카카오 API 인증키 [필수]
        @Query("query") query: String             // 검색을 원하는 질의어 [필수]
        // 매개변수 추가 가능
        // @Query("category_group_code") category: String

    ): Call<ResultPlaceInfo>

}