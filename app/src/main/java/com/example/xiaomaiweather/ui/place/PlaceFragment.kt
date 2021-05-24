package com.example.xiaomaiweather.ui.place

import android.Manifest
import android.annotation.TargetApi
import android.content.Context

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.example.xiaomaiweather.MainActivity
import com.example.xiaomaiweather.R
import com.example.xiaomaiweather.XiaoMaiWeatherAppliciation
import com.example.xiaomaiweather.logic.location.BaiDuLocationActivity
import com.example.xiaomaiweather.logic.location.LocationService
import com.example.xiaomaiweather.logic.location.LocationService.Companion.setLocationOption
import com.example.xiaomaiweather.ui.weather.WeatherActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_place.*
import android.widget.Toast.makeText as makeText1


class PlaceFragment : Fragment() {
    private var locationbtn: Button? = null

//    private var locationService: LocationService? = null



    val viewModel by lazy { ViewModelProviders.of(this).get(PlaceViewModel::class.java) }

    private lateinit var adapter: PlaceAdapter

//    @get:TargetApi(23)
//     private val permissions: Unit
//        private get() {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                Dexter.withActivity()
//                    .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
//                    .withListener(CompositeMultiplePermissionsListener())
//                    .check()
//            }
//        }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_place, container, false)
    }


        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
        if (activity is MainActivity && viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("place_center", place.center)

                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }

//            locationService= (context as XiaoMaiWeatherAppliciation).locationService
//            setLocationOption(locationService?.defaultLocationClientOption)

            locationbtn = getActivity()?.findViewById(R.id.locationbtn) as Button
            val value = locationbtn!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val intent = Intent(context, BaiDuLocationActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    return
//                    if (ContextCompat.checkSelfPermission(
//                            context as XiaoMaiWeatherAppliciation,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        ) != PackageManager.PERMISSION_GRANTED
//                    ) {
//                        requestPermissions(
//                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                            REQUEST_WRITE_EXTERNAL_STORAGE
//                        )
//                    } else {
//                        Toast.makeText(context, "已申请权限", Toast.LENGTH_SHORT).show()
//                    }





//                    var locationService: LocationService? = null
//                    locationService = (this as XiaoMaiWeatherAppliciation).locationService
//                    setLocationOption(locationService?.defaultLocationClientOption)
//
//
//
//                    val mListener: BDAbstractLocationListener =
//                        object : BDAbstractLocationListener() {
//                            /**
//                             * 定位请求回调函数
//                             * @param location 定位结果
//                             */
//                            override fun onReceiveLocation(location: BDLocation) {
//
//                                if (null != location && location.locType != BDLocation.TypeServerError) {
//                                    val sb =
//                                        location.longitude.toString() + "," + location.latitude.toString()
//                                    val sd = location.district.toString()
//                                    Toast.makeText(context, sb, Toast.LENGTH_LONG).show()
//
//                                    val intent = Intent(context, WeatherActivity::class.java).apply {
//                                            putExtra("place_center", sb)
//
//                                            putExtra("place_name", sd)
//                                        }}}
////                                    startActivity(intent)
////                                    activity?.finish()
////                                    return
//                                }
//
//                    locationService?.registerListener(mListener)
//
//
//                    locationService?.start()
                }})










//                    locationService =  (appliciation as XiaoMaiWeatherAppliciation).locationService
//                    //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
//
//                    val type = intent.getIntExtra("from", 0)
//                    if (type == 0) {
//                        setLocationOption(locationService?.defaultLocationClientOption)
//                    } else if (type == 1) {
//                        locationService?.start()
//                    }
////
////                // TODO Auto-generated method stub
////                    val location1=
////
////                    if (null != location1 && location1.locType != .TypeServerError) {
////                        val s1=location1.longitude.toString() +","+location1.latitude.toString()
////                        val s2=location1.district.toString()
////                        val intent = Intent(context, WeatherActivity::class.java).apply {
////                            putExtra("place_center", s1)
////
////                            putExtra("place_name", s2)
////                    }
////                startActivity(intent)
////                activity?.finish()
////                return

            val layoutManager = LinearLayoutManager (activity)
            recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        recyclerView.adapter = adapter
        searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.placeLiveData.observe(this, Observer{ result ->
            val places = result.getOrNull()
            if (places != null) {
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                makeText1(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }


    }




