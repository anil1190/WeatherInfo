package com.example.collabratest.viewmodals


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collabratest.MyApplication
import com.example.collabratest.modals.WeatherModel
import com.example.collabratest.modals.forecast.ForecastResponse
import com.example.collabratest.repository.RegistrationRepository
import com.example.collabratest.uitls.Constants
import com.example.collabratest.uitls.Constants.hasInternetConnection
import com.example.collabratest.uitls.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val registrationRepository: RegistrationRepository) :
    ViewModel(){

    var mResponse: MutableLiveData<NetworkResult<WeatherModel>> = MutableLiveData()
    var weatherListResponse: MutableLiveData<NetworkResult<ForecastResponse>> = MutableLiveData()
    var latLong  = MutableLiveData<String>()

    fun getLatLong(item : String){
        latLong.value = item
    }

    fun getCurrentWeather(lat: Double, lon: Double, appId: String) {
        mResponse.value = NetworkResult.Loading()

        if (hasInternetConnection(MyApplication.getAppInstance())) {
            try {
                viewModelScope.launch {
                    val response = registrationRepository.getCurrentWeather(lat, lon, appId)
                    if (response.cod == 200) {
                        mResponse.value = NetworkResult.Success(response)

                    }else{
                        mResponse.value = NetworkResult.Error(Constants.API_ERROR)
                    }
                }
            }catch (e:Exception){
                mResponse.value = NetworkResult.Error(e.message)
            }
        }else{
            mResponse.value = NetworkResult.Error(Constants.NO_INTERNET)
        }
    }

    fun getWeatherList(lat: Double, lon: Double, appId: String) {
        weatherListResponse.value = NetworkResult.Loading()

        if (hasInternetConnection(MyApplication.getAppInstance())) {
            try {
                viewModelScope.launch {
                    val response = registrationRepository.getWeatherList(lat, lon, appId)
                    if (response.cod == "200") {
                        weatherListResponse.value = NetworkResult.Success(response)

                    }else{
                        weatherListResponse.value = NetworkResult.Error(Constants.API_ERROR)
                    }
                }
            }catch (e:Exception){
                weatherListResponse.value = NetworkResult.Error(e.message)
            }
        }else{
            weatherListResponse.value = NetworkResult.Error(Constants.NO_INTERNET)
        }
    }


}