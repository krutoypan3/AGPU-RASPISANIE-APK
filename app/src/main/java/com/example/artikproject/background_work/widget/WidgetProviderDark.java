package com.example.artikproject.background_work.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

public class WidgetProviderDark extends AppWidgetProvider {
    @Override // При обновлении виджета
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) { // Пробегаемся по всем виджетам, которые необходимо обновить
            WidgetProvider.updateWidget(context, appWidgetManager, appWidgetId, AppCompatDelegate.MODE_NIGHT_YES); // и запускаем фукнцию обновления
        }
    }

    @Override // При удалении виджета
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        WidgetProvider.deleteWidget(context, appWidgetIds);
    }

    @Override // При создании виджета
    public void onEnabled(Context context) {
        WidgetProvider.enableWidget(context, AppCompatDelegate.MODE_NIGHT_YES);
    }
}