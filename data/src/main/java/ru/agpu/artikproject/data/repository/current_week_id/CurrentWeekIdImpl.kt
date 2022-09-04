package ru.agpu.artikproject.data.repository.current_week_id

import android.content.Context
import ru.agpu.artikproject.domain.repository.CurrentWeekIdRepository
import java.lang.Exception

private const val SHARED_PREFS_NAME = "last_open_app_info"
private const val KEY_NAME = "week_id"

class CurrentWeekIdImpl(private val context: Context) : CurrentWeekIdRepository {
    override fun getWeekId(): Int {
        var weekId = CurrentWeekIdGetFromLocal().get(context)
        if (weekId == 0) {
            weekId = CurrentWeekIdGetFromApi().get() ?: 0
            setWeekId(week_id = weekId)
        }
        return weekId
    }

    private fun setWeekId(week_id: Int): Boolean {
        return try {
            val sharedPreferences =
                context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt(KEY_NAME, week_id).apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}