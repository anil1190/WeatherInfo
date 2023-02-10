package com.example.collabratest.viewmodals

import androidx.lifecycle.ViewModel
import com.example.collabratest.MyApplication
import com.example.collabratest.uitls.LocationLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(var locationLiveData: LocationLiveData) : ViewModel(){

    init {
         locationLiveData = LocationLiveData(MyApplication.getAppInstance())
    }
    fun getLocationData() = locationLiveData

}