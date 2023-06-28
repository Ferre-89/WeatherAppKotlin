package Activities

import Utilities.Utilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.weatherappkotlin.R
import com.example.weatherappkotlin.data.db.ForecastDBbHelper
import com.example.weatherappkotlin.domain.model.Forecast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.maxTemperature
import kotlinx.android.synthetic.main.activity_detail.weatherDescription
import kotlinx.android.synthetic.main.item_forecast.icon
import kotlinx.android.synthetic.main.item_forecast.minTemperature
import java.text.DateFormat

class DetailActivity : AppCompatActivity () {

    companion object {
        const val ID = "DetailActivity:id"
        const val CITY_NAME = "DetailActivity:citiName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        title = intent.getStringExtra(CITY_NAME)

        Thread {
            // val result = RequestDayForecastCommand(intent.getLongExtra(ID, -1)).execute
            val result = ForecastDBbHelper.FORECAST.requestForecast("94043", 1)

            if (result != null) {
                runOnUiThread {
                    bindForecast(result)
                }
            }
        }

    }

    private fun bindForecast(forecast: Forecast) = with(forecast) {
        Picasso.get().load(iconUrl).into(icon)
        supportActionBar?.subtitle = Utilities.WEATHER.toDateString(DateFormat.FULL)
        weatherDescription.text = description
        bindWeather(high to maxTemperature, low to minTemperature)
    }

//    private fun bindWeather(vararg views: Pair<Int,TextView>) = views.forEach{
//        it.second.text = "${it.first}"
//        it.second.textcolor = color(when(it.first)){
//            in -50..0 -> android.R.color.holo_red_dark
//            in 0..15 -> android.R.color.holo_orange_dark
//            else -> android.R.color.holo_green_dark
//        })
//    }


    private fun bindWeather(vararg views: Pair<Int, TextView>) {
        views.forEach { (value, textView) ->
            textView.text = value.toString()
            textView.setTextColor(
                when (value) {
                    in -50..0 -> ContextCompat.getColor(
                        textView.context,
                        android.R.color.holo_red_dark
                    )

                    in 0..15 -> ContextCompat.getColor(
                        textView.context,
                        android.R.color.holo_orange_dark
                    )

                    else -> ContextCompat.getColor(
                        textView.context,
                        android.R.color.holo_green_dark
                    )
                }
            )
        }
    }
}