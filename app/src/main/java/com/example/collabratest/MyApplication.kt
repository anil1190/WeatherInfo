package com.example.collabratest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
    companion object{
        private var INSTANCE : MyApplication? = null

        fun getAppInstance() : MyApplication{

            return INSTANCE as MyApplication
        }
    }
}