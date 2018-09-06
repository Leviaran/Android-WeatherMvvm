package com.robotboy.weathermvvm.model

data class Weather(val temp: Float) {

    val tempCelsius: String
        get() {
            val celsius = temp - 273.15
            return "%.2f".format(celsius).plus(" C")
        }
}
