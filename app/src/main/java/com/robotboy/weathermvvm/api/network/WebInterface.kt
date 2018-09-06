package com.robotboy.weathermvvm.api.network

import com.robotboy.weathermvvm.api.response.WeatherResponse
import io.reactivex.Observable
import retrofit2.http.*

interface WebInterface {

    @GET("/data/2.5/weather")
    fun getWeather(@Query("q") city: String, @Query("appid") appId: String): Observable<WeatherResponse>

}