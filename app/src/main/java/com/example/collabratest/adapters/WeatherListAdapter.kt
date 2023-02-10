package com.example.collabratest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.collabratest.MyApplication
import com.example.collabratest.databinding.WeatherListItemBinding
import com.example.collabratest.modals.forecast.WeatherList
import com.example.collabratest.uitls.Constants

class WeatherListAdapter(private val weatherList : List<WeatherList>,private val lat:Double,
    private val lon:Double,private val sunrise : String,private val sunset:String) : RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = WeatherListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
       with(holder){
             with(weatherList[position]){
                 Glide.with(MyApplication.getAppInstance())
                     .load(Constants.IMAGE_URL +this.weather[0].icon+ Constants.IMAGE_EXT)
                     .into(binding.weatherIcon)
                 val mTemp = Constants.convertTempInToCelsius(this.main.temp)
                 val tempInDegree = mTemp+ Constants.DEGREE_SYMBOL
                 binding.temperatureV.text = tempInDegree
                 val dateTime = Constants.getDate(this.dt.toString())
                 binding.currentDateTimeTv.text = dateTime
                 if (lat != 0.0 && lon != 0.0){
                     val country = Constants.getCountryName(lat,lon)
                     binding.countryV.text = country[0].countryName
                     binding.cityV.text = country[0].cityName
                 }
                 val sunriseTime = Constants.getTime(sunrise)
                 binding.sunriseV.text = sunriseTime
                 val sunsetTime = Constants.getTime(sunset)
                 binding.sunsetV.text = sunsetTime
             }
       }
    }

    override fun getItemCount(): Int {
       return weatherList.size
    }

    inner class WeatherViewHolder(val binding: WeatherListItemBinding) : RecyclerView.ViewHolder(binding.root)
}