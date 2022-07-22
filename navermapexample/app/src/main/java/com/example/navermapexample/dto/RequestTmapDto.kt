package com.example.navermapexample.dto

import com.google.gson.annotations.SerializedName
import retrofit2.http.Part
import retrofit2.http.Query


data class RequestTmapDto(

    @SerializedName("startX")
    var startX: String,

    @SerializedName("startY")
    var startY: String,

    @SerializedName("endX")
    var endX: String,

    @SerializedName("endY")
    var endY: String,

    @SerializedName("startName")
    var startName: String,

    @SerializedName("endName")
    var endName: String
)