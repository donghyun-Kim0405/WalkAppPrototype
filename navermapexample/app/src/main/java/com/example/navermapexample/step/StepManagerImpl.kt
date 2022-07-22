package com.example.navermapexample.step

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 *
 * <옵션 사항>
 * SENSOR_DELAY - microsecond delay
 * NORMAL = 200,000
 * UI = 60,000
 * GAME = 20,000
 * FASTEST = 0
 *
 * <사용방법>
 *     1. class생성시 context주입
 *     2. registerListener : register가 먼저 수행되므로 listener variable init 먼주 수행되어야 함
 *     3. startSensor
 *     4. unRegisterListener
 *     5. stopSensor
 *
 * <고려사항>
 * stepCount 초기화 시점 고려
 * **/
class StepManagerImpl(val context: Context) : StepManager {

    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    private lateinit var listeners: ArrayList<StepManager.Listener>

    private var isSensorWorking: Boolean = false
    private var stepCount: Int = 0

    private var sensorListener = createSensorListener()

    init {
        listeners = ArrayList()
        setStepDetector()
    }

    public fun getStep() = stepCount

    override fun setStepDetector() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    }

    override fun startSensor() {
        if(!isSensorWorking){   //sensor동작중이지않을 때
            sensorManager!!.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_UI)
            isSensorWorking = true
        }
    }

    override fun stopSensor() {
        if(isSensorWorking){    //sensor동작 중일 때
            sensorManager!!.unregisterListener(sensorListener)
            isSensorWorking = false
            stepCount = 0
        }
    }

    override fun registerListener(listener: StepManager.Listener) { if(!listeners.contains(listener)) listeners.add(listener) }

    override fun unRegisterListener(listener: StepManager.Listener) { listeners.remove(listener) }

    override fun notifyListener() { for (listener in listeners) listener.onSensorDataChanged() }

    private fun createSensorListener() = object : SensorEventListener{
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                if(event.values[0] == 1.0f) stepCount += 1
                notifyListener()
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    }

    companion object{
        private const val TAG = "StepManagerImpl"
    }
}