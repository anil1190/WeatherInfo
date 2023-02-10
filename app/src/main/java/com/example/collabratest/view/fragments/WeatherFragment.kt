package com.example.collabratest.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.collabratest.MyApplication
import com.example.collabratest.R
import com.example.collabratest.databinding.FragmentWeatherBinding
import com.example.collabratest.uitls.*
import com.example.collabratest.uitls.Constants.API_KEY
import com.example.collabratest.uitls.Constants.City_Name
import com.example.collabratest.uitls.Constants.Country_Name
import com.example.collabratest.uitls.Constants.DEGREE_SYMBOL
import com.example.collabratest.uitls.Constants.Desc
import com.example.collabratest.uitls.Constants.IMAGE_EXT
import com.example.collabratest.uitls.Constants.IMAGE_URL
import com.example.collabratest.uitls.Constants.SUNRISE
import com.example.collabratest.uitls.Constants.SUNSET
import com.example.collabratest.uitls.Constants.Temp
import com.example.collabratest.uitls.Constants.convertTempInToCelsius
import com.example.collabratest.uitls.Constants.getCountryName
import com.example.collabratest.uitls.Constants.getTime
import com.example.collabratest.view.MainActivity
import com.example.collabratest.viewmodals.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WeatherFragment : Fragment() {
    private lateinit var weatherBinding: FragmentWeatherBinding
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherViewModel = activity?.run {
            ViewModelProvider(this)[WeatherViewModel::class.java]
        }?: throw Exception(getString(R.string.invalid_activity))

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        weatherBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_weather,container,false)
        weatherBinding.wViewModel = weatherViewModel
        (activity as MainActivity?)?.setWeatherLocationUpdates(object : WeatherLocationUpdates {
            override fun getUpdatedWeatherLocation(lat: Double, lon: Double) {
                weatherViewModel.getCurrentWeather(lat,lon,API_KEY)
                weatherViewModel.getLatLong("$lat,$lon")
            }
        })
        return weatherBinding.root
    }

    override fun onStart() {
        super.onStart()
       setObservers()
    }

    private fun setObservers(){
       weatherViewModel.mResponse.observe(this){ response ->
           when(response){
               is NetworkResult.Success->{
                   weatherBinding.progressCircular.visibility = View.GONE
                   response.data.let {
                      Glide.with(this)
                          .load(IMAGE_URL+it!!.weather[0].icon+IMAGE_EXT)
                          .into(weatherBinding.weatherIcon)

                       val mTemp = convertTempInToCelsius(it.main.temp)
                       val tempInDegree = Temp+mTemp+DEGREE_SYMBOL
                       weatherBinding.temp.text =  tempInDegree
                       val desc = Desc+it.weather[0].description
                       weatherBinding.description.text = desc
                       val country =  getCountryName(it.coord.lat,it.coord.lon)
                       val countryN = Country_Name+ country[0].countryName
                       weatherBinding.countryName.text = countryN
                       val city = City_Name+country[0].cityName
                       weatherBinding.cityName.text = city
                       val sunriseTime = getTime(it.sys.sunrise.toString())
                       val sunrise = SUNRISE+sunriseTime
                       weatherBinding.sunriseTime.text = sunrise
                       val sunsetTime = getTime(it.sys.sunset.toString())
                       val sunset = SUNSET+sunsetTime
                       weatherBinding.sunsetTime.text = sunset
                   }
               }
               is NetworkResult.Error->{
                   Constants.API_ERROR.toast(MyApplication.getAppInstance())
               }
               is NetworkResult.Loading->{
                  weatherBinding.progressCircular.visibility = View.VISIBLE
               }
           }
       }
    }

}