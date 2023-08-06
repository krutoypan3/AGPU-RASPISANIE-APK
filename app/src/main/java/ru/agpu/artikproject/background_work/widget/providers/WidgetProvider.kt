package ru.agpu.artikproject.background_work.widget.providers

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CheckInternetConnection.getState
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences.getPref
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences.removePref
import ru.agpu.artikproject.background_work.datebase.RaspisanieRepository
import ru.agpu.artikproject.background_work.site_parse.GetRasp
import ru.agpu.artikproject.background_work.theme.Theme.getCurrentSystemTheme
import ru.agpu.artikproject.background_work.widget.WidgetConfig
import ru.agpu.artikproject.background_work.widget.WidgetService
import ru.oganesyanartem.core.data.repository.CurrentWeekDayImpl
import ru.oganesyanartem.core.data.repository.current_week_id.CurrentWeekIdImpl
import ru.oganesyanartem.core.domain.usecase.CurrentWeekDayGetUseCase
import ru.oganesyanartem.core.domain.usecase.CurrentWeekIdGetUseCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.GregorianCalendar
import java.util.TimeZone

class WidgetProvider: AppWidgetProvider() {
    // При обновлении виджета
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (appWidgetId: Int in appWidgetIds) { // Пробегаемся по всем виджетам, которые необходимо обновить
            updateWidget(context, appWidgetManager, appWidgetId, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) // и запускаем фукнцию обновления
        }
    }

    // При удалении виджета
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        deleteWidget(context, appWidgetIds)
    }

    // При создании виджета
    override fun onEnabled(context: Context) {
        enableWidget(context, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    companion object {
        /**
         * Функция обновления виджета. Обновляет виджет.
         */
        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, widgetColor: Int) {
            val textColor: Int
            val rv: RemoteViews
            if (widgetColor == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
                val currentTheme = getCurrentSystemTheme(context)
                if (currentTheme == AppCompatDelegate.MODE_NIGHT_YES) {
                    rv = RemoteViews(context.packageName, R.layout.widget_dark)
                    textColor = context.getColor(R.color.white)
                } else {
                    rv = RemoteViews(context.packageName, R.layout.widget_light)
                    textColor = context.getColor(R.color.black)
                }
            } else if (widgetColor == AppCompatDelegate.MODE_NIGHT_YES) {
                rv = RemoteViews(context.packageName, R.layout.widget_dark)
                textColor = context.getColor(R.color.white)
            } else {
                rv = RemoteViews(context.packageName, R.layout.widget_light)
                textColor = context.getColor(R.color.black)
            }
            setUpdateTV(rv, context, appWidgetId, widgetColor) // Обновляем заголовок
            setList(rv, context, appWidgetId, textColor) // Обновляем внутренний список
            appWidgetManager.updateAppWidget(appWidgetId, rv) // Обновляем виджет
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lvList) // Обновляем данные в виджете
        }

        /**
         * Обновление заголовка виджета
         */
        @SuppressLint("SimpleDateFormat")
        fun setUpdateTV(rv: RemoteViews, context: Context, appWidgetId: Int, widgetColor: Int) {
            rv.setTextViewText(R.id.tvUpdate, context.resources.getString(R.string.Click_me)) // Да, это костыль (Установка текста на кнопку)
            val updIntent = Intent(context, getWidgetProvider(widgetColor))
            updIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE // В этом классе мы запускаем процедуру onUpdate
            updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId)) // В параметры добавляем ID обновляемых виджетов (в нашем случае 1шт)
            val updPIntent = PendingIntent.getBroadcast(context, appWidgetId, updIntent, PendingIntent.FLAG_IMMUTABLE)
            rv.setOnClickPendingIntent(R.id.tvUpdate, updPIntent) // Привязываем кнопку к нашему намерению на обновление
            val weekDay = CurrentWeekDayGetUseCase(CurrentWeekDayImpl()).execute()
            val weekId = CurrentWeekIdGetUseCase(CurrentWeekIdImpl(context)).execute()
            try { // Получаем группу, по номеру виджета из базы данных
                val selectedItemName = getPref(context, appWidgetId.toString() + "_selected_item_name", "")
                val selectedItemId = getPref(context, appWidgetId.toString() + "_selected_item_id", "")
                val selectedItemType = getPref(context, appWidgetId.toString() + "_selected_item_type", "")
                if (selectedItemId != "") {
                    if (getState(context)) { // Обновляем расписание для этой группы
                        GetRasp(selectedItemId, selectedItemType, selectedItemName, weekId, context, "widget").start()
                    }
                    rv.setTextViewText(R.id.tvUpdate, selectedItemName) // Устанавливаем название группы на кнопку

                    // Делаем выборку пар
                    val fr = RaspisanieRepository().getByGroupCodeAndWeekNumberAndWeekDay(
                        groupCode = selectedItemId.toInt(),
                        weekId = weekId,
                        weekDay = weekDay,
                    ).sortedBy { it.paraNumber }.first()

                    val lastUpdateRasp = fr.lastUpdate
                    if (lastUpdateRasp != null) {
                        val sdfCurrentTime = SimpleDateFormat("HH:mm:ss")
                        val sdfRaspisDate = SimpleDateFormat("dd.MM.yyyy")

                        val calendar = GregorianCalendar(TimeZone.getTimeZone("US/Central"))
                        calendar.timeInMillis = Date().time // Затем выводим информацию о текущем дне и времени последнего обновления расписания

                        val calendarRasp = GregorianCalendar(TimeZone.getTimeZone("US/Central"))
                        calendarRasp.timeInMillis = lastUpdateRasp

                        rv.setTextViewText(R.id.tvUpdate_time, sdfRaspisDate.format(calendarRasp.time) + " " + sdfCurrentTime.format(calendar.time))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * Обновление списка виджета
         */
        private fun setList(rv: RemoteViews, context: Context?, appWidgetId: Int, textColor: Int) {
            val adapter = Intent(context, WidgetService::class.java) // Создаем намерение на запуск класса WidgetService
            adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId) // Помещаем id виджета как параметр
            adapter.putExtra("text_color", textColor)
            val data = Uri.parse(adapter.toUri(Intent.URI_INTENT_SCHEME)) // Получаем отработанный результат намерения
            adapter.data = data // Устанавливаем данные в адаптер
            rv.setRemoteAdapter(R.id.lvList, adapter) // Устанавливаем адаптер на список виджета с парами
        }

        private fun getWidgetProvider(widgetColor: Int): Class<*> {
            return when (widgetColor) {
                AppCompatDelegate.MODE_NIGHT_NO -> { // Для светлого виджета вызываем класс WidgetProviderLight
                    WidgetProviderLight::class.java
                }
                AppCompatDelegate.MODE_NIGHT_YES -> { // Для темного виджета вызываем класс WidgetProviderDark
                    WidgetProviderDark::class.java
                }
                else -> { // Для динамического виджета вызываем класс WidgetProvider
                    WidgetProvider::class.java
                }
            }
        }

        fun deleteWidget(context: Context?, appWidgetIds: IntArray) {
            removePref(context, appWidgetIds[0].toString() + "_selected_item_id")
            removePref(context, appWidgetIds[0].toString() + "_selected_item_name")
            removePref(context, appWidgetIds[0].toString() + "_selected_item_type")
            Toast.makeText(context, "Были рады помочь вам!", Toast.LENGTH_SHORT).show()
        }

        fun enableWidget(context: Context, widgetColor: Int) {
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_light)
            val awm = AppWidgetManager.getInstance(context)
            val compName = ComponentName(context, getWidgetProvider(widgetColor))
            val widgetIds = awm.getAppWidgetIds(compName)
            for (widgetId: Int in widgetIds) {
                val intentBtnPwr = Intent(context, WidgetConfig::class.java)
                intentBtnPwr.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                val pi = PendingIntent.getActivity(context, widgetId, intentBtnPwr, PendingIntent.FLAG_IMMUTABLE)
                remoteViews.setOnClickPendingIntent(R.id.tvUpdate, pi)
                awm.updateAppWidget(widgetId, remoteViews)
            }
        }
    }
}