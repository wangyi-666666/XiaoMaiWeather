package com.example.xiaomaiweather.ui.place

import androidx.lifecycle.*
import com.example.xiaomaiweather.logic.Repository
import com.example.xiaomaiweather.logic.model.Place


class PlaceViewModel:ViewModel() {
    private val searchLiveData=MutableLiveData<String>()
    val placeList=ArrayList<Place>()
    val placeLiveData=Transformations.switchMap(searchLiveData){ keywords ->
            Repository.searchPlaces(keywords)
        }

    fun searchPlaces(keywords:String) {
        searchLiveData.value= keywords
    }
    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()
}