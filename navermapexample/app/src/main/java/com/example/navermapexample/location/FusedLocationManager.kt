package com.example.navermapexample.location


/** this class is for check gps sensor
 *  get permission using enableDeviceLocation after check gps using isLocationEnableOnDevice()
 * **/
interface FusedLocationManager {
    fun isLocationEnabledOnDevice(): Boolean

    fun enableDeviceLocation()
}