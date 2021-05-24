package com.example.xiaomaiweather.logic.model



data class PlaceResponse(val status: String,val districts:List<Place>)
data class Place(val name:String, val center: String)
