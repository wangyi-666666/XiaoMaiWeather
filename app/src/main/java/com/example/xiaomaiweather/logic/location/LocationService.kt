package com.example.xiaomaiweather.logic.location

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import android.webkit.WebView
import com.baidu.location.LocationClientOption
import com.baidu.location.LocationClient

/**
 * 定位服务LocationClient 相关
 *
 * @author baidu
 */
class LocationService(locationContext: Context?) {
    private val objLock: Any

    /***
     * 注册定位监听
     *
     * @param listener
     * @return
     */
    fun registerListener(listener: BDAbstractLocationListener?): Boolean {
        var isSuccess = false
        if (listener != null) {
            client!!.registerLocationListener(listener)
            isSuccess = true
        }
        return isSuccess
    }

    fun unregisterListener(listener: BDAbstractLocationListener?) {
        if (listener != null) {
            client!!.unRegisterLocationListener(listener)
        }
    }

    /**
     * @return 获取sdk版本
     */
    val sDKVersion: String?
        get() = if (client != null) {
            client!!.version
        } else null

    /**
     * 开发者应用如果有H5页面使用了百度JS接口，该接口可以辅助百度JS更好的进行定位
     *
     * @param webView 传入webView控件
     */
    fun enableAssistanLocation(webView: WebView?) {
        if (client != null) {
            client!!.enableAssistantLocation(webView)
        }
    }

    /**
     * 停止H5辅助定位
     */
    fun disableAssistantLocation() {
        if (client != null) {
            client!!.disableAssistantLocation()
        }
    }// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
    // 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
    // 可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
    // 可选，设置是否需要地址信息，默认不需要
    // 可选，设置是否需要地址描述
    // 可选，设置是否需要设备方向结果
    // 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
    // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop
    // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
    // 可选，默认false，设置是否需要POI结果，可以在BDLocation
    // 可选，默认false，设置是否收集CRASH信息，默认收集
    // 可选，默认false，设置是否开启Gps定位
    // 可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
    /***
     *
     * @return DefaultLocationClientOption  默认O设置
     */
    val defaultLocationClientOption: LocationClientOption?
        get() {
            if (mOption == null) {
                mOption = LocationClientOption()
                mOption!!.locationMode = LocationClientOption.LocationMode.Hight_Accuracy // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
                mOption!!.setCoorType("bd09ll") // 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
                mOption!!.setScanSpan(0) // 可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
                mOption!!.setIsNeedAddress(true) // 可选，设置是否需要地址信息，默认不需要
                mOption!!.setIsNeedLocationDescribe(true) // 可选，设置是否需要地址描述
                mOption!!.setNeedDeviceDirect(false) // 可选，设置是否需要设备方向结果
                mOption!!.isLocationNotify = false // 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
                mOption!!.setIgnoreKillProcess(true) // 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop
                mOption!!.setIsNeedLocationDescribe(false) // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
                mOption!!.setIsNeedLocationPoiList(false) // 可选，默认false，设置是否需要POI结果，可以在BDLocation
                mOption!!.SetIgnoreCacheException(false) // 可选，默认false，设置是否收集CRASH信息，默认收集
                mOption!!.isOpenGps = true // 可选，默认false，设置是否开启Gps定位
                mOption!!.setIsNeedAltitude(false) // 可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
            }
            return mOption
        }

    /**
     * @return DIYOption 自定义Option设置
     */
    val option: LocationClientOption?
        get() {
            if (DIYoption == null) {
                DIYoption = LocationClientOption()
            }
            return DIYoption
        }

    fun start() {
        synchronized(objLock) {
            if (client != null && !client!!.isStarted) {
                client!!.start()
            }
        }
    }

    fun requestLocation() {
        if (client != null) {
            client!!.requestLocation()
        }
    }

    fun stop() {
        synchronized(objLock) {
            if (client != null && client!!.isStarted) {
                client!!.stop()
            }
        }
    }

    val isStart: Boolean
        get() = client!!.isStarted

    fun requestHotSpotState(): Boolean {
        return client!!.requestHotSpotState()
    }

    companion object {
        private var client: LocationClient? = null
        private var mOption: LocationClientOption? = null
        private var DIYoption: LocationClientOption? = null

        /***
         * 设置定位参数
         *
         * @param option
         * @return isSuccessSetOption
         */
        @JvmStatic
        fun setLocationOption(option: LocationClientOption?): Boolean {
            val isSuccess = false
            if (option != null) {
                if (client!!.isStarted) {
                    client!!.stop()
                }
                DIYoption = option
                client!!.locOption = option
            }
            return isSuccess
        }
    }

    /***
     * 初始化 LocationClient
     *
     * @param locationContext
     */
    init {
        objLock = Any()
        synchronized(objLock) {
            if (client == null) {
                client = LocationClient(locationContext)
                client!!.locOption = defaultLocationClientOption
            }
        }
    }
}