package com.example.navermapexample.tracking

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.navermapexample.gps.GpsCalculator
import com.example.navermapexample.gps.GpsManager
import com.example.navermapexample.main.ApplicationGraph
import com.example.navermapexample.room.LocationEntity
import com.example.navermapexample.room.RouteEntity
import com.example.navermapexample.step.StepManager
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

class TrackingViewModel : ViewModel() {
    private val gpsManager = ApplicationGraph.getGpsManager()
    private val stepManager = ApplicationGraph.getStepManager()
    private val roomDB = ApplicationGraph.getRoomDB()

    private val gpsListener = createGpsListener()
    private val stepListener = createStepListener()

    public var routeName: String? = "TEST"
    public var route: MutableLiveData<ArrayList<LatLng>> = MutableLiveData<ArrayList<LatLng>>().apply {
        value = ArrayList()
    }
    public var currentLocation: MutableLiveData<LatLng> = MutableLiveData()

    private var baseLocation: Location? = null  //두 위치간 이동거리를 계산할때 시작점이 되는 값을 저장할 변수

    private var locationEntitysId = 0
    private var totalDistance: Int = 0

    private var locationEntitys: ArrayList<LocationEntity> = ArrayList()

    public fun start(){
        if(!gpsManager.isListening()) {
            locationEntitysId = 0
            totalDistance = 0
            route = MutableLiveData<ArrayList<LatLng>>().apply { value = ArrayList() }
            locationEntitys = ArrayList()
            registerListener()
            gpsManager.startListening()

            stepManager.registerListener(stepListener)
            stepManager.startSensor()
        }

    }

    /** 주의! data insert 한후 센서 동작 정지 및 데이터 초기화 **/
    public fun stop(){
        if (gpsManager.isListening()) {
            insertRouteData()

            unRegisterListener()
            gpsManager.stopListening()

            stepManager.unRegisterListener(stepListener)
            stepManager.stopSensor()
        }
    }

    public fun registerListener(){
        gpsManager.registerListener(gpsListener)
    }
    public fun unRegisterListener(){
        gpsManager.unregisterListener(gpsListener)
    }


    private fun insertRouteData(){
        routeName ?: return
        val routeEntity = RouteEntity(
            name = routeName!!,
            distance = totalDistance.toString(),
            step= stepManager.getStep().toString(),
            gpsdata = locationEntitys
        )

        Log.e(TAG, "insert data to db")
        CoroutineScope(Dispatchers.IO).launch {
            roomDB?.routeDao()?.insert(routeEntity) ?: Log.e(TAG, "roomDB should be initialized")
        }

    }


    /** 최소 이동거리가 1미터 이상일 경우에만 View를 갱신 & 데이터 저장 */
    private fun createGpsListener() = object : GpsManager.Listener{
        override fun onGpsUpdated(location: Location) {

            if (baseLocation == null) {   //처음으로 위치를 받은 경우 비교할 대상이 없으므로 첫위치를 갱신 후 종료
                baseLocation = location
                return
            }
            val distance = GpsCalculator.getDistance(baseLocation!!, location)

            if (distance < 1.0) {
                Log.e(TAG, "distance"+distance)
                return
            }


            Log.e(TAG, "distance"+distance)

            locationEntitysId += 1
            totalDistance += distance.toInt()

            currentLocation.value = LatLng(location.latitude, location.longitude)

            locationEntitys.add(LocationEntity(
                number = locationEntitysId,
                latitute = location.latitude,
                longitude = location.longitude
            ))

            val path = route.value
            path!!.add(LatLng(location.latitude, location.longitude))
            route.postValue(path)

            baseLocation = location
            Log.e(TAG, route.value!!.size.toString())
        }
    }

    private fun createStepListener() = object : StepManager.Listener{
        override fun onSensorDataChanged() {
            Log.e(TAG, stepManager.getStep().toString())
        }

    }


    companion object{
        private const val TAG = "TrackingViewModel"
    }

}