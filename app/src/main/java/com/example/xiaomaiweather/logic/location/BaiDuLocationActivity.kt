package com.example.xiaomaiweather.logic.location

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.Poi
import com.example.xiaomaiweather.R
import com.example.xiaomaiweather.XiaoMaiWeatherAppliciation
import com.example.xiaomaiweather.XiaoMaiWeatherAppliciation.Companion.context
import com.example.xiaomaiweather.logic.Repository
import com.example.xiaomaiweather.logic.location.LocationService.Companion.setLocationOption
import com.example.xiaomaiweather.logic.model.Place
import com.example.xiaomaiweather.ui.place.PlaceViewModel
import com.example.xiaomaiweather.ui.weather.WeatherActivity

import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import kotlin.reflect.KProperty

/***
 * 单点定位示例，用来展示基本的定位结果，配置在 LocationService.java 中
 * 默认配置也可以在 LocationService 中修改
 * 默认配置的内容自于开发者论坛中对开发者长期提出的疑问内容
 *
 * @author baidu
 */
class BaiDuLocationActivity : Activity() {
    private var locationService: LocationService? = null
    private var LocationResult: TextView? = null
    fun savePlace(place: Place) = Repository.savePlace(place)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baidu_location)
        LocationResult?.setMovementMethod(ScrollingMovementMethod.getInstance())
    }



    override fun onStart() {
        super.onStart()
        // -----------location config ------------
        locationService = (application as XiaoMaiWeatherAppliciation).locationService
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService?.registerListener(mListener)
        //注册监听

            setLocationOption(locationService?.defaultLocationClientOption)

            locationService?.start()
    }
//
    /***
     * Stop location service
     */
    override fun onStop() {
        locationService!!.unregisterListener(mListener) //注销掉监听
        locationService!!.stop() //停止定位服务
        super.onStop()
    }

    /**
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private val mListener: BDAbstractLocationListener = object : BDAbstractLocationListener() {
        /**
         * 定位请求回调函数
         * @param location 定位结果
         */
        override fun onReceiveLocation(location: BDLocation) {

            if (null != location && location.locType != BDLocation.TypeServerError) {
                val sb=location.longitude.toString() +","+location.latitude.toString()
                val sd=location.district.toString()
                val place=Place(sd,sb)
                savePlace(place)



                val intent = Intent(context, WeatherActivity::class.java).apply {
                    putExtra("place_center", sb)

                    putExtra("place_name", sd)
                }

                startActivity(intent)

                return
//                val sb = StringBuffer(256)
//                sb.append("time : ")
//                /**
//                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
//                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
//                 */
//                sb.append(location.time)
//                sb.append("\nlocType : ") // 定位类型
//                sb.append(location.locType)
//                sb.append("\nlocType description : ") // *****对应的定位类型说明*****
//                sb.append(location.locTypeDescription)
//                sb.append("\nlatitude : ") // 纬度
//                sb.append(location.latitude)
//                sb.append("\nlongitude : ") // 经度
//                sb.append(location.longitude)
//                sb.append("\nradius : ") // 半径
//                sb.append(location.radius)
//                sb.append("\nCountryCode : ") // 国家码
//                sb.append(location.countryCode)
//                sb.append("\nProvince : ") // 获取省份
//                sb.append(location.province)
//                sb.append("\nCountry : ") // 国家名称
//                sb.append(location.country)
//                sb.append("\ncitycode : ") // 城市编码
//                sb.append(location.cityCode)
//                sb.append("\ncity : ") // 城市
//                sb.append(location.city)
//                sb.append("\nDistrict : ") // 区
//                sb.append(location.district)
//                sb.append("\nTown : ") // 获取镇信息
//                sb.append(location.town)
//                sb.append("\nStreet : ") // 街道
//                sb.append(location.street)
//                sb.append("\naddr : ") // 地址信息
//                sb.append(location.addrStr)
//                sb.append("\nStreetNumber : ") // 获取街道号码
//                sb.append(location.streetNumber)
//                sb.append("\nUserIndoorState: ") // *****返回用户室内外判断结果*****
//                sb.append(location.userIndoorState)
//                sb.append("\nDirection(not all devices have value): ")
//                sb.append(location.direction) // 方向
//                sb.append("\nlocationdescribe: ")
//                sb.append(location.locationDescribe) // 位置语义化信息
//                sb.append("\nPoi: ") // POI信息
//                if (location.poiList != null && !location.poiList.isEmpty()) {
//                    for (i in location.poiList.indices) {
//                        val poi = location.poiList[i] as Poi
//                        sb.append("poiName:")
//                        sb.append(poi.name + ", ")
//                        sb.append("poiTag:")
//                        sb.append("""
//    ${poi.tags}
//
//    """.trimIndent())
//                    }
//                }
//                if (location.poiRegion != null) {
//                    sb.append("PoiRegion: ") // 返回定位位置相对poi的位置关系，仅在开发者设置需要POI信息时才会返回，在网络不通或无法获取时有可能返回null
//                    val poiRegion = location.poiRegion
//                    sb.append("DerectionDesc:") // 获取POIREGION的位置关系，ex:"内"
//                    sb.append(poiRegion.derectionDesc + "; ")
//                    sb.append("Name:") // 获取POIREGION的名字字符串
//                    sb.append(poiRegion.name + "; ")
//                    sb.append("Tags:") // 获取POIREGION的类型
//                    sb.append(poiRegion.tags + "; ")
//                    sb.append("\nSDK版本: ")
//                }
//                sb.append(locationService!!.sDKVersion) // 获取SDK版本
//                if (location.locType == BDLocation.TypeGpsLocation) { // GPS定位结果
//                    sb.append("\nspeed : ")
//                    sb.append(location.speed) // 速度 单位：km/h
//                    sb.append("\nsatellite : ")
//                    sb.append(location.satelliteNumber) // 卫星数目
//                    sb.append("\nheight : ")
//                    sb.append(location.altitude) // 海拔高度 单位：米
//                    sb.append("\ngps status : ")
//                    sb.append(location.gpsAccuracyStatus) // *****gps质量判断*****
//                    sb.append("\ndescribe : ")
//                    sb.append("gps定位成功")
//                } else if (location.locType == BDLocation.TypeNetWorkLocation) { // 网络定位结果
//                    // 运营商信息
//                    if (location.hasAltitude()) { // *****如果有海拔高度*****
//                        sb.append("\nheight : ")
//                        sb.append(location.altitude) // 单位：米
//                    }
//                    sb.append("\noperationers : ") // 运营商信息
//                    sb.append(location.operators)
//                    sb.append("\ndescribe : ")
//                    sb.append("网络定位成功")
//                } else if (location.locType == BDLocation.TypeOffLineLocation) { // 离线定位结果
//                    sb.append("\ndescribe : ")
//                    sb.append("离线定位成功，离线定位结果也是有效的")
//                } else if (location.locType == BDLocation.TypeServerError) {
//                    sb.append("\ndescribe : ")
//                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因")
//                } else if (location.locType == BDLocation.TypeNetWorkException) {
//                    sb.append("\ndescribe : ")
//                    sb.append("网络不同导致定位失败，请检查网络是否通畅")
//                } else if (location.locType == BDLocation.TypeCriteriaException) {
//                    sb.append("\ndescribe : ")
//                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机")
//                }
                // 切回主线程更新 UI
//                runOnUiThread { LocationResult!!.text = sb.toString() }
            }
        }
    }
}


