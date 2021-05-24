package com.example.xiaomaiweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Vibrator
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.example.xiaomaiweather.logic.location.LocationService

class XiaoMaiWeatherAppliciation : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN1 = "789c43a3f46b51d87cde24d6e831ec9f"
        const val TOKEN2 = "moTSIJZAwSWX2kOu"
    }

        var locationService: LocationService? = null

        // 震动
        var mVibrator: Vibrator? = null

        override fun onCreate() {
            super.onCreate()
            context=applicationContext
            locationService = LocationService(applicationContext)
            mVibrator = applicationContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
            SDKInitializer.initialize(applicationContext)
            SDKInitializer.setCoordType(CoordType.BD09LL)
    }
}