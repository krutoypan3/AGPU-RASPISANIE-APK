package ru.agpu.artikproject.background_work.datebase

import android.content.Context
import android.preference.PreferenceManager

object MySharedPreferences {
    fun put(context: Context?, name: String?, value: Int) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val edit = sharedPreferences.edit()
        edit.putInt(name, value)
        edit.apply() //apply
    }

    fun put(context: Context?, name: String?, value: String?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val edit = sharedPreferences.edit()
        edit.putString(name, value)
        edit.apply() //apply
    }

    fun put(context: Context?, name: String?, value: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val edit = sharedPreferences.edit()
        edit.putBoolean(name, value)
        edit.apply() //apply
    }

    fun remove(context: Context?, name: String?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val edit = sharedPreferences.edit()
        edit.remove(name)
        edit.apply() //apply
    }

    operator fun get(context: Context?, name: String?, default_value: String): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(name, default_value)
    }

    operator fun get(context: Context?, name: String?, default_value: Int): Int {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(name, default_value)
    }

    operator fun get(context: Context?, name: String?, default_value: Boolean): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(name, default_value)
    }
}