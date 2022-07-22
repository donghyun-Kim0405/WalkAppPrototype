package com.example.navermapexample.room

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.*


@Database(version = 1, entities = [RouteEntity::class])
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