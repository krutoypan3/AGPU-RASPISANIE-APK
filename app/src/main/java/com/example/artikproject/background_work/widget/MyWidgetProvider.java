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
import android.os.Handler;
import android.os.Looper;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.artikproject.R;
import com.example.artikproject.background_work.CheckInternetConnection;
import com.example.artikproject.background_work.GetCurrentWeekDay;
import com.example.artikproject.background_work.GetCurrentWeekId_Local;
import com.example.artikproject.background_work.datebase.DataBase_Local;
import com.example.artikproject.background_work.main_show.ListViewGroupListener;
import com.example.artikproject.background_work.site_parse.GetRasp;
import com.example.artikproject.layout.MainActivity;
import com.example.artikproject.layout.Raspisanie_show;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MyWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    void updateWidget(Context context, AppWidgetManager appWidgetManager,
                      int appWidgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.widget);
        setUpdateTV(rv, context, appWidgetId);
        setList(rv, context, appWidgetId);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
                R.id.lvList);
    }

    void setUpdateTV(RemoteViews rv, Context context, int appWidgetId) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        rv.setTextViewText(R.id.tvUpdate, context.getResources().getString(R.string.Click_me)); // Да, это костыль
        Intent updIntent = new Intent(context, MyWidgetProvider.class);
        updIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
                new int[] { appWidgetId });
        PendingIntent updPIntent = PendingIntent.getBroadcast(context,
                appWidgetId, updIntent, 0);
        rv.setOnClickPendingIntent(R.id.tvUpdate, updPIntent);

        sqLiteDatabase = new DataBase_Local(context).getWritableDatabase();
        int week_day = GetCurrentWeekDay.get();
        int week_id = GetCurrentWeekId_Local.get();

        try{
            Cursor r = sqLiteDatabase.rawQuery("SELECT selected_item_name, selected_item_id, selected_item_type FROM widgets WHERE widget_id = " + appWidgetId, null);
            if (r.moveToFirst()) {
                String selectedItem_name = r.getString(0);
                String selectedItem_id = r.getString(1);
                String selectedItem_type = r.getString(2);
                if (CheckInternetConnection.getState(context)){
                    new GetRasp(selectedItem_id, selectedItem_type, selectedItem_name, GetCurrentWeekId_Local.get(), context, true).start();
                }
                rv.setTextViewText(R.id.tvUpdate, selectedItem_name);
                Cursor fr = sqLiteDatabase.rawQuery("SELECT * FROM raspisanie WHERE " +
                        "r_group_code = " + selectedItem_id + " AND " +
                        "r_week_number = " + week_id + " AND " +
                        "r_week_day = " + week_day + " ORDER BY r_para_number", null);
                if (fr.moveToFirst()) {
                    long last_update = fr.getLong(13);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");

                    GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
                    calendar.setTimeInMillis(last_update);
                    rv.setTextViewText(R.id.tvUpdate_time,  fr.getString(10) + " " + fr.getString(11) + "   " + context.getResources().getString(R.string.last_update) + ": " + sdf2.format(calendar.getTime()));
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    void setList(RemoteViews rv, Context context, int appWidgetId) {
        Intent adapter = new Intent(context, WidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        Uri data = Uri.parse(adapter.toUri(Intent.URI_INTENT_SCHEME));
        adapter.setData(data);
        rv.setRemoteAdapter(R.id.lvList, adapter);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Toast.makeText(context, "Были рады помочь вам!", Toast.LENGTH_SHORT).show();
        sqLiteDatabase = new DataBase_Local(context).getWritableDatabase();
        sqLiteDatabase.delete("widgets", "widget_id = '" + appWidgetIds[0] + "'", null);
    }

    @Override // При создании виджета
    public void onEnabled(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        AppWidgetManager awm = AppWidgetManager.getInstance(context);
        ComponentName compName = new ComponentName(context, MyWidgetProvider.class);
        int[] widgetIds = awm.getAppWidgetIds(compName);
        for (int widgetId : widgetIds) {
            Intent intentBtnPwr = new Intent(context, MyWidgetConfig.class);
            intentBtnPwr.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            PendingIntent pi = PendingIntent.getActivity(context, widgetId, intentBtnPwr, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.tvUpdate, pi);
            awm.updateAppWidget(widgetId, remoteViews);
        }
    }
}