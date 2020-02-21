package com.example.mobxexample

import android.app.Application
import android.util.Log
import mobx.core.spy

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        spy {
            Log.d("TODO_LIST", "Action '$it' is executed")
            Log.d("TODO_LIST", "---------")
        }
    }
}
