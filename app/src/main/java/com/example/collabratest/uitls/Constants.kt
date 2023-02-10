package com.example.collabratest.uitls

import android.content.Context
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.collabratest.MyApplication
import com.example.collabratest.modals.location.CountryModel
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.roundToInt

object Constants {
    const val USER_TABLE= "user_table"
    const val USER_DATABASE= "user_database"
    const val NO_INTERNET = "No Internet Connection"
    const val API_ERROR   = "Something went wrong"
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val IMAGE_URL = "https://openweathermap.org/img/wn/"
    const val API_KEY  = ""
    const val DATE_PATTERN = "MM/dd/yyyy hh:mm a"
    const val TIME_PATTERN = "hh:mm a"
    const val DEGREE_SYMBOL = "\u2103"
    const val SUNRISE = "Sunrise Time: "
    const val SUNSET  = "Sunset Time: "
    const val Country_Name = "Country Name: "
    const val City_Name = "City Name: "
    const val Temp = "Temperature: "
    const val Desc = "Description: "
    const val IMAGE_EXT = "@2x.png"
    const val LOCATION_REQUEST = 100
    const val GPS_REQUEST = 101

    private val EMAIL_ADDRESS_PATTERN: Pattern? = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    fun isValidEmail(str: String): Boolean?{
        return EMAIL_ADDRESS_PATTERN?.matcher(str)?.matches()
   }

    fun hasInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun convertTempInToCelsius(temp:Double) : String{
        val tempInCel = (temp - 273.15).roundToInt().toBigDecimal()
        return tempInCel.toString()
    }

    fun getDate(s: String): String? {
        try {
            val sdf = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
            val netDate = Date(s.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun getTime(time : String) : String?{
        try {
            val sdf = SimpleDateFormat(TIME_PATTERN, Locale.getDefault())
            val netDate = Date(time.toLong() * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun getCountryName(lat: Double,lon: Double) : MutableList<CountryModel>{
        val geocoder = Geocoder(MyApplication.getAppInstance(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        val cityName = addresses?.get(0)?.locality
        val countryName = addresses?.get(0)?.countryName
        val countryCity = mutableListOf<CountryModel>()
        countryCity.add(CountryModel(countryName,cityName))
        return countryCity
    }

}