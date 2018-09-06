package com.robotboy.weathermvvm.viewmodel

import com.jakewharton.rxrelay2.BehaviorRelay
import com.robotboy.weathermvvm.api.network.WebApi
import com.robotboy.weathermvvm.api.response.WeatherResponse
import com.robotboy.weathermvvm.misc.Constants
import io.reactivex.Observable

class WeatherViewModel : BaseViewModel() {
    val city: BehaviorRelay<String> = BehaviorRelay.create()

    val weatherRequest: Observable<WeatherResponse> = Observable.defer {
        standartRequest(WebApi.getInstance().service.getWeather(city.value, Constants.APP_ID))
    }

    val isInputValid: Observable<Boolean> = city.map { it.isNotEmpty() }
}