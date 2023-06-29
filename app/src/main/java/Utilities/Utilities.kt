package Utilities

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Utilities {
    WEATHER;

    fun toDateString(dateFormat: Int = DateFormat.MEDIUM, date:String): String {

        val inputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val outputFormat = DateFormat.getDateInstance(dateFormat, Locale.getDefault())

        val parsedDate: Date = inputFormat.parse(date) as Date

        return outputFormat.format(parsedDate)
    }

    fun showToast(elContexto: Context, text: String, duration: Int) {
        Toast.makeText(elContexto, text, duration).show()
    }
}