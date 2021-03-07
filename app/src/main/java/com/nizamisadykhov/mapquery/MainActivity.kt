package com.nizamisadykhov.mapquery

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nizamisadykhov.mapquery.api.FoodleApi
import com.nizamisadykhov.mapquery.data.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var jsonObject: JsonObject? = null

    private var mapFragment: SupportMapFragment? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchContents()

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            mapFragment?.getMapAsync(this)
        }

    }

    private fun fetchContents(){
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://foodleapp.com/img/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val foodleApi = retrofit.create(FoodleApi::class.java)

        val contents = foodleApi.fetchContents()

        contents.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d(TAG, "onResponse: ${response.body()}")
                jsonObject = response.body()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e(TAG, "onFailure: ", t)
            }
        })
    }

    override fun onMapReady(map: GoogleMap?) {
        val restaurant = jsonObject?.data
        val coordinates = LatLng(restaurant!!.lat, restaurant.lng)
        val marker = map?.addMarker(
            MarkerOptions()
                .position(coordinates)
                .title(restaurant.name)
        )
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15F))

        findViewById<TextView>(R.id.restaurant_name).text = restaurant.name
        findViewById<TextView>(R.id.coordinates_on_the_map).text = "lat = ${restaurant.lat}, lng = ${restaurant.lng}"
    }


}