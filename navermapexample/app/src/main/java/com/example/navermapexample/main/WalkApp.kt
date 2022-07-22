package com.example.navermapexample.main

import android.app.Application

class WalkApp: Application() {
    override fun onCreate() {
        super.onCreate()
        ApplicationGraph.init(this)
    }
}