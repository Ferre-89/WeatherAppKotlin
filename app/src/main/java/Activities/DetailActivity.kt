package Activities

import Utilities.Utilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.weatherappkotlin.R
import com.example.weatherappkotlin.data.db.ForecastDBbHelper
import com.example.weatherappkotlin.domain.model.Forecast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.maxTemperature
import kotlinx.android.synthetic.main.activity_detail.weatherDescription
import kotlinx.android.synthetic.main.item_forecast.icon
import kotlinx.android.synthetic.main.item_forecast.minTemperature
import java.nio.file.Files.find
import java.text.DateFormat

class DetailActivity : AppCompatActivity(), ToolbarManager {

    companion object {
        const val ID = "DetailActivity:id"
        const val CITY_NAME = "DetailActivity:cityName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        initToolBar()
        toolbarTitle = intent.getStringExtra(CITY_NAME).toString()
        enableHomeAsUp { onBackPressed() }

        val dayId = intent.getLongExtra(ID, -1)

        Log.d("Rodrigo", "DetailActivity, ID: " + intent.getLongExtra(ID, -1))
        Log.d("Rodrigo", "DetailActivity, CITY_NAME: " + intent.getStringExtra(CITY_NAME))

        title = intent.getStringExtra(CITY_NAME)

        val result = ForecastDBbHelper.FORECAST.requestForecast("94043", dayId)

        if (result != null) {
            bindForecast(result)
        }
    }

    private fun bindForecast(forecast: Forecast) = with(forecast) {
        Picasso.get().load(iconUrl).into(icon)
        supportActionBar?.subtitle = Utilities.WEATHER.toDateString(DateFormat.FULL, date)

        weatherDescription.text = description
        bindWeather(high to maxTemperature, low to minTemperature)
    }

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
                        android.R.color.holo_green_light
                    )
                }
            )
        }
    }

    override val toolbar: androidx.appcompat.widget.Toolbar by lazy {
        findViewById(R.id.toolbar)
    }

}