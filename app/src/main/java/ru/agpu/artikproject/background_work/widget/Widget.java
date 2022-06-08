package ru.agpu.artikproject.background_work.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.util.ArrayList;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.GetCurrentWeekDay;
import ru.agpu.artikproject.background_work.GetCurrentWeekId_Local;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;

public class Widget implements RemoteViewsFactory {

    private ArrayList<String> data; // Список с парами

    final Context context;
    final int widgetID; // Номер виджета
    final int text_color;


    public Widget(Context ctx, Intent intent) {
        context = ctx; // Заполняем переменные данными из конструктора
        widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        text_color = intent.getIntExtra("text_color", R.color.black);
    }


    /**
     * При создании класса обнуляем список
     */
    @Override
    public void onCreate() {
        data = new ArrayList<>();
    }

    /**
     * Получаем количество пар в списке
     * @return Количество пар
     */
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    /**
     * Установка пары в свою позицию
     * @param position позиция пары
     */
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rView = new RemoteViews(context.getPackageName(),
                R.layout.item);
        rView.setTextViewText(R.id.tvItemText, data.get(position));
        rView.setTextColor(R.id.tvItemText, text_color);
        return rView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * Тут заполняем список пар
     */
    @Override
    public void onDataSetChanged() {
        try{
            data.clear(); // Обнуляем список
            DataBase_Local.sqLiteDatabase = new DataBase_Local(context).getWritableDatabase(); // Подключение к базе данных должно быть выше функций получения дня недели и недели
            int week_day = GetCurrentWeekDay.get();
            int week_id = GetCurrentWeekId_Local.get(context);

            String selectedItem_id = MySharedPreferences.get(context, widgetID + "_selected_item_id", "");

            if (!selectedItem_id.equals("")){
                Cursor r = DataBase_Local.sqLiteDatabase.rawQuery("SELECT * FROM raspisanie WHERE " +
                    "r_group_code = " + selectedItem_id + " AND " +
                    "r_week_number = " + week_id + " AND " +
                    "r_week_day = " + week_day + " ORDER BY r_para_number", null);
                if (r.getCount() != 0) {
                    String str;
                    r.moveToFirst();
                    do {
                        str = "";
                        if (r.getString(4) != null) {
                            str += r.getString(9) + "\n";
                            str += r.getString(4) + "\n";
                            if (r.getString(5) != null) str += r.getString(5) + "\n";
                            if (r.getString(6) != null) str += r.getString(6) + "\n";
                            if (r.getString(7) != null) str += r.getString(7) + "\n";
                            if (r.getString(8) != null) str += r.getString(8) + "\n";
                            if (r.getString(15) != null) str += r.getString(15) + "\n";
                            data.add(str);
                        }
                    } while (r.moveToNext());
                    if (data.isEmpty()){
                        data.add(context.getString(R.string.day_off));
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

    }

}