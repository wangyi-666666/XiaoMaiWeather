package com.example.xiaomaiweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


object XiaoMaiWeatherNetwork {
    private val placeService =ServiceCreator1.create(PlaceService::class.java)
    private val weatherService=ServiceCreator2.create(WeatherService::class.java)
    suspend fun getRealtimeWeather(center:String)=
        weatherService.getRealtimeWeather(center).await()
    suspend fun getDailyWeather(center:String)=
        weatherService.getDailyWeather(center).await()
    suspend fun searchPlaces(keywords:String)= placeService.searchPlaces(keywords).await()
    private final suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        java.lang.RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)

                }
            })
    }}

}