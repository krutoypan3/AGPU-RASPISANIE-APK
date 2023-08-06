package ru.agpu.artikproject.background_work.widget.providers

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class WidgetProviderLight : AppWidgetProvider() {
    // При обновлении виджета
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (appWidgetId in appWidgetIds) { // Пробегаемся по всем виджетам, которые необходимо обновить
            WidgetProvider.updateWidget(context, appWidgetManager, appWidgetId, AppCompatDelegate.MODE_NIGHT_NO) // и запускаем фукнцию обновления
        }
    }

    // При удалении виджета
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        WidgetProvider.deleteWidget(context, appWidgetIds)
    }

    // При создании виджета
    override fun onEnabled(context: Context) {
        WidgetProvider.enableWidget(context, AppCompatDelegate.MODE_NIGHT_NO)
    }
}