package com.example.weatherappkotlin.data.db.RealmObjects

import io.realm.RealmList
import io.realm.RealmObject

open class CityForecastTable : RealmObject() {
    var id: String = ""
    var city: String = ""
    var country: String = ""
}

open class DayForecastTable : RealmObject() {
    var id: Long = 0
    var zipCode: String = ""
    var date: String = ""
    var description: String = ""
    var high: String = ""
    var low: String = ""
    var icon_url: String = ""
    var city_id: String = ""

}

open class ForecastResult {
    var city: City? = null
    lateinit var list: RealmList<Forecast>
}

open class City {
    var id: Long = 0
    var name: String = ""
    var coord: Coordinates? = null
    var country: String = ""
    var population: Int = 0
}

open class Coordinates {
    var lon: Float = 0F
    var lat: Float = 0F
}

open class Forecast {
    var dt: Long = 0
    var temp: Temperature? = null
    var pressure: Float = 0F
    var humidity: Float = 0F
    var weather: RealmList<Weather>? = null
    var speed: Float = 0F
    var deg: Int = 0
    var clouds: Int = 0
    var rain: Float = 0F
}

open class Temperature {
    var day: Float = 0F
    var min: Float = 0F
    var max: Float = 0F
    var night: Float = 0F
    var eve: Float = 0F
    var morn: Float = 0F
}

open class Weather {
    var id: Long = 0
    var main: String = ""
    var description: String = ""
    var icon: String = ""
}
