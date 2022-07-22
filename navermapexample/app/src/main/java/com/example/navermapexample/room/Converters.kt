package com.example.navermapexample.room

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    public fun LocationEntityToJson(value: List<LocationEntity>) = Gson().toJson(value)

    @TypeConverter
    public fun JsonToLocationEntity(value: String) = Gson().fromJson(value, Array<LocationEntity>::class.java).toList()
}