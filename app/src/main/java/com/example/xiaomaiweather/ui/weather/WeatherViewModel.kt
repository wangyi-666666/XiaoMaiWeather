package com.example.xiaomaiweather.ui.weather


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.xiaomaiweather.logic.Repository



class WeatherViewModel:ViewModel() {

    private val centerLiveData=MutableLiveData<String>()
    var center=""
    var placeName=""
    val weatherLiveData=Transformations.switchMap(centerLiveData){ center ->
        Repository.refreshWeather(center,placeName)
    }
    fun refreshWeather(center:String){
        centerLiveData.value=center
    }
}