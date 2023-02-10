package com.example.collabratest.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.lifecycle.ViewModelProvider
import com.example.collabratest.R
import com.example.collabratest.adapters.ViewPagerAdapter
import com.example.collabratest.databinding.ActivityMainBinding
import com.example.collabratest.uitls.*
import com.example.collabratest.uitls.Constants.LOCATION_REQUEST
import com.example.collabratest.view.fragments.WeatherFragment
import com.example.collabratest.view.fragments.WeatherListFragment
import com.example.collabratest.viewmodals.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var mainBinding: ActivityMainBinding? = null
    private lateinit var mainViewModel : MainViewModel
    private lateinit var weatherLocationUpdates: WeatherLocationUpdates
    private var isGPSEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContentView(mainBinding!!.root)
        setSupportActionBar(mainBinding!!.toolbar)

        initViewPager()
        initGPS()
    }

    private fun initViewPager(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(WeatherFragment(),getString(R.string.current_weather))
        adapter.addFragment(WeatherListFragment(),getString(R.string.weather_list))

        mainBinding?.viewPager?.adapter = adapter
        mainBinding?.tabs?.setupWithViewPager(mainBinding?.viewPager)
    }

    private fun initGPS(){
        LocationUtils(this).turnGPSOn(object : LocationUtils.OnGpsListener{
            override fun gpsStatus(isGPSEnable: Boolean) {
                this@MainActivity.isGPSEnabled = isGPSEnable
            }
        })
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.GPS_REQUEST) {
                isGPSEnabled = true
                invokeLocationAction()
            }
        }
    }

    private fun invokeLocationAction() {
        when {
            !isGPSEnabled -> getString(R.string.enable_gps).toast(this)

            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> getString(R.string.allow_permission).toast(this)

            else -> requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST
            )
        }
    }

    private fun startLocationUpdate() {
       mainViewModel.getLocationData().observe(this){
           weatherLocationUpdates.getUpdatedWeatherLocation(it.latitude,it.longitude)
       }
    }


    fun setWeatherLocationUpdates(weatherLocationUpdates: WeatherLocationUpdates){
        this.weatherLocationUpdates = weatherLocationUpdates
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }

    override fun onDestroy() {
        mainBinding = null
        super.onDestroy()
    }
}