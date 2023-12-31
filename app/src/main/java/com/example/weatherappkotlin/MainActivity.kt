package com.example.weatherappkotlin

import Activities.DetailActivity
import Activities.SettingsActivity
import Activities.ToolbarManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappkotlin.adapters.ForecastListAdapter
import com.example.weatherappkotlin.data.db.ForecastDBbHelper
import com.example.weatherappkotlin.domain.commands.RequestForecastCommand
import com.example.weatherappkotlin.domain.model.ForecastList
import com.example.weatherappkotlin.ui.ui.utils.DelegatesExt
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), ToolbarManager {

    private val zipCode: Long by DelegatesExt.preference(
        this,
        SettingsActivity.ZIP_CODE,
        SettingsActivity.DEFAULT_ZIP
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolBar()

        forecastList.layoutManager = LinearLayoutManager(this)
        attachToScroll(forecastList)
    }

    private fun loadForecast() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val forecastResult = RequestForecastCommand(zipCode.toString()).execute()

                    // Guardo el resultado en la base.
                    ForecastDBbHelper.FORECAST.saveForecast(forecastResult, "94043")

                    forecastResult
                }

                val listadoForecast: ForecastList? = null

                // Obtengo el listado de la base y se lo paso al adaptador
                val list = withContext(Dispatchers.IO) {
                    listadoForecast.let { ForecastDBbHelper.FORECAST.requestAllForecast("94043") }

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
                    toolbarTitle = "${result.city} (${result.country})"
                }
            } catch (e: Exception) {
                // Handle exceptions
                Log.e("Rodrigo", "Error loading forecast: ${e.message}")
            }
        }
    }

    override val toolbar: Toolbar by lazy {
        findViewById(R.id.toolbar)
    }

    override fun onResume() {
        super.onResume()
        loadForecast()
    }
}