package com.example.navermapexample.location

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


/**
 * 해당 클래스는 오직 device가 gps센서를 사용가능한지 체크하기 위해서 사용
 * Gps센서 update관련 메서드는 GpsManager 참조
 * GpsManager를 상속받는 클래스가 활성화 됨에 따라 현재 클래스의 FusedLocationProviderClass를 메모리에서 해제할지 추후 결정
 * **/
class FusedLocationManagerImpl(private val context: Context)
    : FusedLocationManager, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private var connectionFlag: Boolean = false
    private var googleApiClient: GoogleApiClient? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    init {
        googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        googleApiClient?.connect()
    }

    override fun isLocationEnabledOnDevice(): Boolean = connectionFlag
    override fun enableDeviceLocation() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        if (context !is Activity) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        context.startActivity(intent)
    }
    override fun onConnected(p0: Bundle?) { connectionFlag = true}
    override fun onConnectionFailed(p0: ConnectionResult) { connectionFlag = false }
    override fun onConnectionSuspended(p0: Int) {}
}