package com.nizamisadykhov.mapquery.api

import com.nizamisadykhov.mapquery.data.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface FoodleApi {

    @GET("test.txt")
    fun fetchContents(): Call<JsonObject>
}