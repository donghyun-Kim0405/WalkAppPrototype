package com.example.navermapexample.step

interface StepManager {

    fun setStepDetector()

    fun startSensor()

    fun stopSensor()

    fun registerListener(listener: Listener)

    fun unRegisterListener(listener: Listener)

    fun notifyListener()

    interface Listener{
        fun onSensorDataChanged()
    }
}