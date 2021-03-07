package com.nizamisadykhov.mapquery.data

data class JsonObject(
    var code: Int,
    var name: String,
    var message: String,
    var data: RestaurantData
)