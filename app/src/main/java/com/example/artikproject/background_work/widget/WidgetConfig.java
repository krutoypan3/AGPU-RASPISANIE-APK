package com.example.artikproject.background_work.widget;

import static com.example.artikproject.layout.MainActivity.sqLiteDatabase;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.artikproject.R;

import com.example.artikproject.background_work.datebase.DataBase_Local;
import com.example.artikproject.background_work.main_show.WatchSaveGroupRasp;


public class WidgetConfig extends Activity {

    public static ListView ConfigWidgetListView;
    int awID;
    public static String selectedItem;
    public static String selectedItem_type;
    public static String selectedItem_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_config);
        ConfigWidgetListView = findViewById(R.id.configWidgetListView); // Список с ранее открытыми группами при создании виджета
        WatchSaveGroupRasp sap = new WatchSaveGroupRasp(getApplicationContext(), true); // Получаем список сохраненных групп из базы данных
        try{
        if(!(sap.group_list.size() == 0)){ // Если в списке есть группы \ аудитории \ преподаватели
            ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), R.layout.listviewadapterbl, sap.group_list.toArray(new String[0])); // Заполняем ими адаптер
            WidgetConfig.ConfigWidgetListView.setAdapter(adapter); // Применяем адаптер к списку конфигурационного активити
        }}
        catch (Exception e){e.printStackTrace();}
        ConfigWidgetListView.setOnItemClickListener((parent, view, position, id) -> // Обрабатываем нажатие на элементы списка
            {
                // Добавляем информацию о выбранном элементе в таблицу базы данных с id виджета
                selectedItem = sap.group_list.toArray(new String[0])[position];
                selectedItem_type = sap.group_list_type.toArray(new String[0])[position];
                selectedItem_id = sap.group_list_id.toArray(new String[0])[position];
                sqLiteDatabase = new DataBase_Local(getApplicationContext()).getWritableDatabase();
                ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                rowValues.put("widget_id", awID);
                rowValues.put("selected_item_id", selectedItem_id);
                rowValues.put("selected_item_type", selectedItem_type);
                rowValues.put("selected_item_name", selectedItem);
                sqLiteDatabase.insert("widgets", null, rowValues);

                Intent resultIntent = new Intent();
                resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, awID);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        );

        // Получение ID создаваемого виджета
        Intent intent = getIntent(); // Получаем текущее намерение
        Bundle bundleExtras = intent.getExtras(); // Получаем внутренние значения намерения
        if (bundleExtras != null){ // Если намерение не пустое
            awID = bundleExtras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID); // Получаем ID виджета
        }else // Иначе
            finish(); // Закрываем все к чертям
    }
}