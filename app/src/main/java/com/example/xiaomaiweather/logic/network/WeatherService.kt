package com.example.xiaomaiweather.logic.network


import com.example.xiaomaiweather.XiaoMaiWeatherAppliciation
import com.example.xiaomaiweather.logic.model.DailyResponse
import com.example.xiaomaiweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface WeatherService {
    @GET("v2.5/${XiaoMaiWeatherAppliciation.TOKEN2}/{center}/realtime.json")
    fun getRealtimeWeather(@Path("center") center:String):
            Call<RealtimeResponse>
    @GET("v2.5/${XiaoMaiWeatherAppliciation.TOKEN2}/{center}/daily.json")
    fun getDailyWeather(@Path("center") center: String):
            Call<DailyResponse>

}