package com.example.navermapexample.main

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.navermapexample.R

class SplashActivity : AppCompatActivity() {

    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        requestPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE ->{
                if((grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    Log.e(TAG, "Permission granted")
                    moveToMainActivity()
                }else{ showPermissionDialog() }
            }
        }
    }

    private fun requestPermission(){

        Log.e(TAG, "requestPermission called")
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE)   //권한 요청

        } else { moveToMainActivity() }
    }

    private fun moveToMainActivity() = startActivity(Intent(this, MainActivity::class.java))

    private fun showPermissionDialog(){
        var localBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        AlertDialog.Builder(this).apply {
            setTitle("권한 설정")
            setMessage("권한 거절로 인해 일부 기능이 제한됩니다.")
            setPositiveButton("권한 설정하러 가기", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    try {
                        var intent: Intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:" + packageName))
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                        startActivity(Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS))
                    }
                }
            })
            setNegativeButton("취소하기", object : DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    Toast.makeText(application, "취소되었습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }?.let {
            it.create()
            it.show()
        }
    }

    companion object{
        private const val TAG = "SplashActivity"
        private const val PERMISSION_REQUEST_CODE = 100
    }
}