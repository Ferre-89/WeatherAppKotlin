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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadForecast()

        forecastList.layoutManager = LinearLayoutManager(this)

        //Toast.makeText(this@MainActivity, "Its a toast!", Toast.LENGTH_SHORT).show()
    }

    private fun loadForecast() {
        Thread {
            // Obtengo resultado del JSON
            val result = RequestForecastCommand("94043").execute()

            // Guardo el resultado en la base.
            ForecastDBbHelper.FORECAST.saveForecast(result, "94043")

            // Obtengo el listado de la base y se lo paso al adaptador
            val list = ForecastDBbHelper.FORECAST.requestAllForecast("94043")
            //  Log.d("Rodrigo", "Resultado: " + result)

            runOnUiThread {

//                forecastList.adapter = ForecastListAdapter(list) { forecast ->
//                    ForecastDBbHelper.FORECAST.requestForecast(
//                        "94043", forecast.id
//                    )
//                }

                val adapter = ForecastListAdapter(list) {
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.ID, it.id)
                    intent.putExtra(DetailActivity.CITY_NAME, result.city)
                    startActivity(intent)
                }

                Log.d("Rodrigo", "Main: ")
                 forecastList.adapter = adapter

                //  forecastList.adapter = ForecastListAdapter(result, object : ForecastListAdapter.OnItemClickListener{
                //   override fun invoke(forecast: Forecast){
                //      Toast.makeText(this@MainActivity, forecast.date, Toast.LENGTH_SHORT).show()
                //   }

            }

        }.start()
    }
}

fun toast(elContexto: Context, forecastDate: String) {
    Toast.makeText(elContexto, forecastDate, Toast.LENGTH_SHORT).show()
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppKotlinTheme {
        Greeting("Android")
    }
}