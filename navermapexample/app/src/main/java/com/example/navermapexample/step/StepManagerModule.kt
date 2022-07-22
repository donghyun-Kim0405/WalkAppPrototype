package com.example.navermapexample.step

import android.content.Context

class StepManagerModule(private val context: Context) {
    fun createStepManager(): StepManagerImpl{
        return StepManagerImpl(context = context)
    }

}