package com.example.weatherappkotlin.domain.model

data class ForecastList(val city: String, val country: String,
                        private val dailyForecast: List<Forecast>) {

    val size: Int
    get() = dailyForecast.size

    operator fun get(position: Int) = dailyForecast[position]
}

data class Forecast(val id:Long, val date: String, val description: String, val high: Int, val low: Int, val iconUrl: String)