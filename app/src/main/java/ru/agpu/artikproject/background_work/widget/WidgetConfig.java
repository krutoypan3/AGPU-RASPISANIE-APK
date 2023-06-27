package ru.agpu.artikproject.background_work.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;

import android.widget.ListView;

import ru.agpu.artikproject.R;

import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.agpu.artikproject.background_work.main_show.WatchSaveGroupRasp;


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
        if(!(sap.getGroupList().size() == 0)){ // Если в списке есть группы \ аудитории \ преподаватели
            ListViewAdapter adapter = new ListViewAdapter(getApplicationContext(), sap.getGroupList(), true);
            WidgetConfig.ConfigWidgetListView.setAdapter(adapter); // Применяем адаптер к списку конфигурационного активити
        }}
        catch (Exception e){e.printStackTrace();}
        ConfigWidgetListView.setOnItemClickListener((parent, view, position, id) -> // Обрабатываем нажатие на элементы списка
            {
                // Добавляем информацию о выбранном элементе в таблицу базы данных с id виджета
                selectedItem = sap.getGroupList().get(position).getItem();
                selectedItem_type = sap.getGroupListType().toArray(new String[0])[position];
                selectedItem_id = sap.getGroupListId().toArray(new String[0])[position];

                MySharedPreferences.INSTANCE.put(getApplicationContext(), awID + "_selected_item_id", selectedItem_id);
                MySharedPreferences.INSTANCE.put(getApplicationContext(), awID + "_selected_item_type", selectedItem_type);
                MySharedPreferences.INSTANCE.put(getApplicationContext(), awID + "_selected_item_name", selectedItem);

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