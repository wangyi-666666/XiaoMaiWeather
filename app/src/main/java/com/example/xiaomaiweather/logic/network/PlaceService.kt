package com.example.xiaomaiweather.logic.network

import com.example.xiaomaiweather.XiaoMaiWeatherAppliciation
import com.example.xiaomaiweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("v3/config/district?key=${XiaoMaiWeatherAppliciation.TOKEN1}&subdistrict=0")
    fun searchPlaces(@Query("keywords") keywords:String): Call<PlaceResponse>
}