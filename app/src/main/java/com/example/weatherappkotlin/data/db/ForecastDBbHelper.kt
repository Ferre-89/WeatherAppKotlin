package com.example.weatherappkotlin.data.db

import android.util.Log
import com.example.weatherappkotlin.data.db.RealmObjects.CityForecastTable
import com.example.weatherappkotlin.data.db.RealmObjects.DayForecastTable
import com.example.weatherappkotlin.domain.model.Forecast
import com.example.weatherappkotlin.domain.model.ForecastList
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.createObject

enum class ForecastDBbHelper {

    FORECAST;

    private val realmName: String = "My Project"
    private var config = RealmConfiguration.Builder().name(realmName).build()

    private var realm: Realm = Realm.getInstance(config)

    fun requestForecast(zipCode: String, id: Long): Forecast? {
        Log.d("Rodrigo", "requestForecast, zipCode: " + zipCode + ", id: " + id)

        // var forecast : DayForecastTable? = null
        var forecast: Forecast? = null

        // obtiene un forecast para una ciudad y el dia que clickee
        try {
            realm = Realm.getInstance(config)

            val dailyForecast: DayForecastTable? =
                realm.where(DayForecastTable::class.java).equalTo("id", id)
                    .equalTo("zipCode", zipCode).findFirst()

            val city: CityForecastTable? =
                realm.where(CityForecastTable::class.java).equalTo("id", zipCode).findFirst()

            if (dailyForecast != null) {
                forecast = Forecast(
                    dailyForecast.id,
                    dailyForecast.date,
                    dailyForecast.description,
                    dailyForecast.high.toInt(),
                    dailyForecast.low.toInt(),
                    dailyForecast.icon_url,
                )
            }

            //  Log.d("Rodrigo", "daily: " + dailyForecast)
            Log.d("Rodrigo", "city: " + city)


        } catch (e: Exception) {
            Log.d("Rodrigo", "La excepcion: " + e.message)
        }
        return forecast
    }

    fun requestAllForecast(zipCode: String): ForecastList {
        val realm = Realm.getInstance(config)
        val forecastResults: RealmResults<DayForecastTable>? =
            realm.where(DayForecastTable::class.java).findAll()

        val forecastList = mutableListOf<Forecast>()

        forecastResults?.forEach { dayForecastTable ->
            val forecast = Forecast(
                dayForecastTable.id,
                dayForecastTable.date,
                dayForecastTable.description,
                dayForecastTable.high.toInt(),
                dayForecastTable.low.toInt(),
                dayForecastTable.icon_url,
            )
            forecastList.add(forecast)
        }

        val cityForecast: CityForecastTable =
            realm.where(CityForecastTable::class.java).equalTo("id", zipCode).findFirst()!!

        val city = cityForecast.city // Set the city name
        val country = cityForecast.country // Set the country name

        return ForecastList(city, country, forecastList)
    }

//    fun requestAllForecast(): RealmResults<DayForecastTable>? {
//        realm = Realm.getInstance(config)
//        val list = realm.where(DayForecastTable::class.java).findAll()
//
//        return list
//    }


    fun saveForecast(forecastList: ForecastList, zipCode: String) {
//guarda el listado, sobreescribiendo el que ya estaba. Inserta una forecastCity y todos los dailyForecast

        realm = Realm.getInstance(config)
        realm.beginTransaction()
        realm.deleteAll()

        val cityForecastTable = realm.createObject<CityForecastTable>()

        cityForecastTable.country = forecastList.country
        cityForecastTable.id = zipCode
        cityForecastTable.city = forecastList.city

        realm.insert(cityForecastTable)

        var i = 0

        while (i < forecastList.size) {

            val dayForecastTable = realm.createObject<DayForecastTable>()

            dayForecastTable.id = i.toLong()
            dayForecastTable.zipCode = zipCode
            dayForecastTable.city_id = forecastList.city
            dayForecastTable.date = forecastList[i].date
            dayForecastTable.description = forecastList[i].description
            dayForecastTable.high = forecastList[i].high.toString()
            dayForecastTable.low = forecastList[i].low.toString()
            dayForecastTable.icon_url = forecastList[i].iconUrl

            realm.insert(dayForecastTable)

            Log.d("Rodrigo", "Forecast - descripcion: " + forecastList[i].description + ", date: ")

            i++
        }
        realm.commitTransaction()
    }

}

