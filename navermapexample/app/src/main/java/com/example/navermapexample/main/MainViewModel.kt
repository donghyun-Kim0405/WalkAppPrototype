package com.example.navermapexample.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.navermapexample.main.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository: MainRepository = MainRepository()

    public fun requestGEOCoding(str: String){
        Log.e(TAG, str)

        CoroutineScope(Dispatchers.IO).launch {
            repository.searchAddress(str)
        }

    }




    companion object{
        private const val TAG = "MainViewModel"
    }

}