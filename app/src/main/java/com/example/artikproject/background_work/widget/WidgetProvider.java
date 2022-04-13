package com.example.artikproject.background_work.widget;

import static com.example.artikproject.layout.MainActivity.sqLiteDatabase;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.artikproject.R;
import com.example.artikproject.background_work.CheckInternetConnection;
import com.example.artikproject.background_work.GetCurrentWeekDay;
import com.example.artikproject.background_work.GetCurrentWeekId_Local;
import com.example.artikproject.background_work.datebase.DataBase_Local;
import com.example.artikproject.background_work.site_parse.GetRasp;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class WidgetProvider extends AppWidgetProvider {
    @Override // При обновлении виджета
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) { // Пробегаемся по всем виджетам, которые необходимо обновить
            updateWidget(context, appWidgetManager, appWidgetId); // и запускаем фукнцию обновления
        }
    }

    /**
     * Функция обновления виджета. Обновляет виджет.
     */
    void updateWidget(Context context, AppWidgetManager appWidgetManager,
                      int appWidgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.widget); // Делаем привязку виджета к его графической реализации
        setUpdateTV(rv, context, appWidgetId); // Обновляем заголовок
        setList(rv, context, appWidgetId); // Обновляем внутренний список

        appWidgetManager.updateAppWidget(appWidgetId, rv); // Обновляем виджет
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
                R.id.lvList); // Обновляем данные в виджете
    }

    /**
     * Обновление заголовка виджета
     */
    void setUpdateTV(RemoteViews rv, Context context, int appWidgetId) {
        rv.setTextViewText(R.id.tvUpdate, context.getResources().getString(R.string.Click_me)); // Да, это костыль (Установка текста на кнопку)
        Intent updIntent = new Intent(context, WidgetProvider.class); // При обновлении вызываем класс WidgetProvider
        updIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE); // В этом классе мы запускаем процедуру onUpdate
        updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                new int[] { appWidgetId }); // В параметры добавляем ID обновляемых виджетов (в нашем случае 1шт)
        PendingIntent updPIntent = PendingIntent.getBroadcast(context,
                appWidgetId, updIntent, PendingIntent.FLAG_IMMUTABLE);
        rv.setOnClickPendingIntent(R.id.tvUpdate, updPIntent); // Привязываем кнопку к нашему намерению на обновление

        sqLiteDatabase = new DataBase_Local(context).getWritableDatabase(); // Подключаемся к базе данных
        int week_day = GetCurrentWeekDay.get(); // Получаем текущий день
        int week_id = GetCurrentWeekId_Local.get(); // Получаем текущую неделю

        try{ // Получаем группу, по номеру виджета из базы данных
            Cursor r = sqLiteDatabase.rawQuery("SELECT selected_item_name, selected_item_id, selected_item_type FROM widgets WHERE widget_id = " + appWidgetId, null);
            if (r.moveToFirst()) {
                String selectedItem_name = r.getString(0);
                String selectedItem_id = r.getString(1);
                String selectedItem_type = r.getString(2);
                if (CheckInternetConnection.getState(context)){ // Обновляем расписание для этой группы
                    new GetRasp(selectedItem_id, selectedItem_type, selectedItem_name, GetCurrentWeekId_Local.get(), context, "widget").start();
                }
                rv.setTextViewText(R.id.tvUpdate, selectedItem_name); // Устанавливаем название группы на кнопку
                Cursor fr = sqLiteDatabase.rawQuery("SELECT * FROM raspisanie WHERE " +
                        "r_group_code = " + selectedItem_id + " AND " +
                        "r_week_number = " + week_id + " AND " +
                        "r_week_day = " + week_day + " ORDER BY r_para_number", null); // Делаем выборку пар
                if (fr.moveToFirst()) {
                    long last_update = fr.getLong(13); // И получаем дату последнего обновления
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");

                    GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
                    calendar.setTimeInMillis(last_update); // Затем выводим информацию о текущем дне и времени последнего обновления расписания
                    rv.setTextViewText(R.id.tvUpdate_time,  fr.getString(10) + " " + fr.getString(11) + "   " + context.getResources().getString(R.string.last_update) + ": " + sdf2.format(calendar.getTime()));
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * Обновление списка виджета
     */
    void setList(RemoteViews rv, Context context, int appWidgetId) {
        Intent adapter = new Intent(context, WidgetService.class); // Создаем намерение на запуск класса WidgetService
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId); // Помещаем id виджета как параметр
        Uri data = Uri.parse(adapter.toUri(Intent.URI_INTENT_SCHEME)); // Получаем отработанный результат намерения
        adapter.setData(data); // Устанавливаем данные в адаптер
        rv.setRemoteAdapter(R.id.lvList, adapter); // Устанавливаем адаптер на список виджета с парами
    }

    @Override // При удалении виджета
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Toast.makeText(context, "Были рады помочь вам!", Toast.LENGTH_SHORT).show();
        sqLiteDatabase = new DataBase_Local(context).getWritableDatabase(); // Подключаемся к базе данных
        sqLiteDatabase.delete("widgets", "widget_id = '" + appWidgetIds[0] + "'", null); // Удаляем привязку виджета к элементам расписания
    }

    @Override // При создании виджета
    public void onEnabled(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        AppWidgetManager awm = AppWidgetManager.getInstance(context);
        ComponentName compName = new ComponentName(context, WidgetProvider.class);
        int[] widgetIds = awm.getAppWidgetIds(compName);
        for (int widgetId : widgetIds) {
            Intent intentBtnPwr = new Intent(context, WidgetConfig.class);
            intentBtnPwr.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            PendingIntent pi = PendingIntent.getActivity(context, widgetId, intentBtnPwr, PendingIntent.FLAG_IMMUTABLE);
            remoteViews.setOnClickPendingIntent(R.id.tvUpdate, pi);
            awm.updateAppWidget(widgetId, remoteViews);
        }
    }
}