package com.example.weatherappkotlin.ui.ui

import android.app.Application
import com.example.weatherappkotlin.ui.ui.utils.DelegatesExt
import io.realm.Realm

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        instance = this
    }

    companion object {
        var instance: App by DelegatesExt.notNullSingleValue()
    }
}