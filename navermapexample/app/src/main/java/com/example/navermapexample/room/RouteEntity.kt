package com.example.navermapexample.room

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "route")
data class RouteEntity (
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "distance") var distance: String, //insert시점 시스템 시간
    @ColumnInfo(name = "step") var step: String,
    @ColumnInfo(name = "gpsdata") var gpsdata: List<LocationEntity> //insert시점 시스템 시간
    ) : Serializable{
        @PrimaryKey(autoGenerate = true) var id: Int = 0
    }

