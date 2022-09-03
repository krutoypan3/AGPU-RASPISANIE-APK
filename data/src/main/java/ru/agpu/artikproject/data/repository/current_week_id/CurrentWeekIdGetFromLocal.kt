package ru.agpu.artikproject.data.repository.current_week_id

import android.content.Context

private const val SHARED_PREFS_NAME = "last_open_app_info"
private const val KEY_NAME = "week_id"

class GetCurrentWeekIdLocal {
    fun get(context: Context): Int {
        val sharedPreferences =
            context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_NAME, 0)
    }
}