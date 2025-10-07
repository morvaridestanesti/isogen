package com.estanesti.isogen

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class App : Application() {
    companion object {
        lateinit var PREFERENCES: SharedPreferences
        lateinit var EDITOR: SharedPreferences.Editor
    }

    override fun onCreate() {
        super.onCreate()

        PREFERENCES = PreferenceManager.getDefaultSharedPreferences(this)
        EDITOR = PREFERENCES.edit()
    }
}