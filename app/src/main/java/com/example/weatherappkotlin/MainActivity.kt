package com.example.weatherappkotlin

import Activities.DetailActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappkotlin.adapters.ForecastListAdapter
import com.example.weatherappkotlin.data.db.ForecastDBbHelper
import com.example.weatherappkotlin.domain.commands.RequestForecastCommand
import com.example.weatherappkotlin.ui.ui.WeatherAppKotlinTheme
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadForecast()

        forecastList.layoutManager = LinearLayoutManager(this)
    }

    private fun loadForecast() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val forecastResult = RequestForecastCommand("94043").execute()

                    // Guardo el resultado en la base.
                    ForecastDBbHelper.FORECAST.saveForecast(forecastResult, "94043")

                    forecastResult
                }

                // Obtengo el listado de la base y se lo paso al adaptador
                val list = withContext(Dispatchers.IO) {
                    ForecastDBbHelper.FORECAST.requestAllForecast("94043")
                }

                runOnUiThread {
                    val adapter = ForecastListAdapter(list) { forecast ->
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.ID, forecast.id)
                        intent.putExtra(DetailActivity.CITY_NAME, result.city)
                        startActivity(intent)

                        Log.d("Rodrigo", "Main: " + forecast.id)
                        Log.d("Rodrigo", "Main: " + result.city)
                    }

                    forecastList.adapter = adapter
                }
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("Rodrigo", "Error loading forecast: ${e.message}")
            }
        }
    }
}