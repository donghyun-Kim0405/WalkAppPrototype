package com.example.navermapexample.room

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [RouteEntity::class], version = 1)
@TypeConverters(com.example.navermapexample.room.Converters::class)
abstract class RoomDB : RoomDatabase(){
    abstract fun routeDao(): RouteDao

    companion object{
        private var INSTANCE: RoomDB? = null

        fun getInstance(context: Context): RoomDB?{
            if (INSTANCE == null) {
                synchronized(RoomDB::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RoomDB::class.java,
                        "walkapp-database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }


}