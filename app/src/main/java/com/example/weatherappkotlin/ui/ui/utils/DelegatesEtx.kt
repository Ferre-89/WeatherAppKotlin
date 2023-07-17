package com.example.weatherappkotlin.ui.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object DelegatesExt {
    fun <T : Any> notNullSingleValue(): ReadWriteProperty<Any?, T> = NotNullSingleValueVar()
    fun <T : Any> preference(context: Context, name: String, default: T): ReadWriteProperty<Any?, T> =
        Preference(context, name, default)
}

class NotNullSingleValueVar<T : Any> : ReadWriteProperty<Any?, T> {
    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("${property.name} not initialized")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value
        else throw IllegalStateException("${property.name} already initialized")
    }
}

class Preference<T : Any>(
    private val context: Context,
    private val name: String,
    private val default: T
) : ReadWriteProperty<Any?, T> {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("default", Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    @Suppress("UNCHECKED_CAST")
    private fun findPreference(name: String, default: T): T {
        val res: Any? = when (default) {
            is Long -> prefs.getLong(name, default)
            is String -> prefs.getString(name, default)
            is Int -> prefs.getInt(name, default)
            is Boolean -> prefs.getBoolean(name, default)
            is Float -> prefs.getFloat(name, default)
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }

        return res as? T ?: default
    }

    @SuppressLint("CommitPrefEdits")
    private fun putPreference(name: String, value: T) {
        with(prefs.edit()) {
            when (value) {
                is Long -> putLong(name, value)
                is String -> putString(name, value)
                is Int -> putInt(name, value)
                is Boolean -> putBoolean(name, value)
                is Float -> putFloat(name, value)
                else -> throw IllegalArgumentException("This type can't be saved into Preferences")
            }
            apply()
        }
    }
}
