package Utilities

import android.util.Log
import com.example.weatherappkotlin.domain.model.Forecast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_forecast.dateText
import kotlinx.android.synthetic.main.item_forecast.descriptionText
import kotlinx.android.synthetic.main.item_forecast.icon
import kotlinx.android.synthetic.main.item_forecast.maxTemperature
import kotlinx.android.synthetic.main.item_forecast.minTemperature
import kotlinx.android.synthetic.main.item_forecast.theId
import java.text.DateFormat
import java.util.Locale

enum class Utilities {
    WEATHER;

//    fun Long.toDateString(dateFormat: Int = DateFormat.MEDIUM): String {
//        val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
//        return df.format(this)
//    }

    fun toDateString(dateFormat: Int = DateFormat.MEDIUM): String {
        val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
        return df.format(this)
    }
}