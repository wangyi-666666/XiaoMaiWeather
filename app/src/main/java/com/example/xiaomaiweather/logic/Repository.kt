package com.example.xiaomaiweather.logic

import androidx.lifecycle.liveData
import com.example.xiaomaiweather.logic.dao.PlaceDao
import com.example.xiaomaiweather.logic.model.Place
import com.example.xiaomaiweather.logic.model.Weather
import com.example.xiaomaiweather.logic.network.XiaoMaiWeatherNetwork

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {

    fun searchPlaces(keywords: String) = fire(Dispatchers.IO) {
        val placeResponse = XiaoMaiWeatherNetwork.searchPlaces(keywords)
        if (placeResponse.status == "1") {
            val places = placeResponse.districts
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(center:String, placeName: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                XiaoMaiWeatherNetwork.getRealtimeWeather(center)
            }
            val deferredDaily = async {
                XiaoMaiWeatherNetwork.getDailyWeather(center)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

}



