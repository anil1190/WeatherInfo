package com.example.collabratest.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collabratest.MyApplication
import com.example.collabratest.R
import com.example.collabratest.adapters.WeatherListAdapter
import com.example.collabratest.databinding.FragmentWeatherListBinding
import com.example.collabratest.uitls.Constants
import com.example.collabratest.uitls.NetworkResult
import com.example.collabratest.uitls.toast
import com.example.collabratest.viewmodals.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherListFragment : Fragment() {
   private lateinit var weatherListBinding : FragmentWeatherListBinding
   private lateinit var weatherViewModel: WeatherViewModel
   private lateinit var weatherListAdapter: WeatherListAdapter

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
        weatherListBinding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_weather_list,container,false)
        weatherListBinding.wViewModel = weatherViewModel
        weatherListBinding.weatherListRv.layoutManager = LinearLayoutManager(activity)
        weatherListBinding.weatherListRv.setHasFixedSize(true)
        return weatherListBinding.root
    }

    override fun onStart() {
        super.onStart()
        setObservers()
    }
    private fun setObservers(){
        weatherViewModel.latLong.observe(this){
            val latLong = it.split(",").toTypedArray()
            weatherViewModel.getWeatherList(latLong[0].toDouble(),latLong[1].toDouble(), Constants.API_KEY)
        }
        weatherViewModel.weatherListResponse.observe(this){ response->
            when(response){
                is NetworkResult.Success->{
                    weatherListBinding.progressWeatherList.visibility = View.GONE
                    response.data.let {
                       weatherListAdapter = WeatherListAdapter(it!!.list,it.city.coord.lat,it.city.coord.lon,
                                    it.city.sunrise.toString(),it.city.sunset.toString())
                        weatherListBinding.weatherListRv.adapter = weatherListAdapter
                    }
                }
                is NetworkResult.Error->{
                    Constants.API_ERROR.toast(MyApplication.getAppInstance())
                }
                is NetworkResult.Loading->{
                    weatherListBinding.progressWeatherList.visibility = View.VISIBLE
                }
            }
        }
    }

}