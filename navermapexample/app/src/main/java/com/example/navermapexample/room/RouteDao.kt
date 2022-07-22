package com.example.navermapexample.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import okhttp3.Route

@Dao
interface RouteDao {
    @Insert
    fun insert(route: RouteEntity)

    @Delete
    fun delete(route: RouteEntity)

    @Query("SELECT * FROM route")
    fun findAll():List<RouteEntity>


    /*@Query("SELECT * FROM route WHERE date LIKE :date")
    fun selectByMonth(date: String): List<RidingEntity>*/
}