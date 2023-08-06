package ru.agpu.artikproject.background_work.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences.putPref
import ru.agpu.artikproject.background_work.main_show.WatchSaveGroupRasp

class WidgetConfig : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.widget_config)
        val configWidgetListView: ListView? = findViewById(R.id.configWidgetListView) // Список с ранее открытыми группами при создании виджета
        val sap = WatchSaveGroupRasp(applicationContext, true) // Получаем список сохраненных групп из базы данных
        try {
            if (sap.groupList.size != 0) { // Если в списке есть группы \ аудитории \ преподаватели
                val adapter = ListViewAdapter(applicationContext, sap.groupList, true)
                configWidgetListView?.adapter = adapter // Применяем адаптер к списку конфигурационного активити
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var awID = 0
        configWidgetListView?.onItemClickListener =
            OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                // Добавляем информацию о выбранном элементе в таблицу базы данных с id виджета
                val selectedItem = sap.groupList[position].item
                val selectedItemType = sap.groupListType[position]
                val selectedItemId = sap.groupListId[position]
                putPref(applicationContext, awID.toString() + "_selected_item_id", selectedItemId)
                putPref(applicationContext, awID.toString() + "_selected_item_type", selectedItemType)
                putPref(applicationContext, awID.toString() + "_selected_item_name", selectedItem)
                val resultIntent = Intent()
                resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, awID)
                setResult(RESULT_OK, resultIntent)
                finish()
            }

        // Получение ID создаваемого виджета
        val intent = intent // Получаем текущее намерение
        val bundleExtras = intent.extras // Получаем внутренние значения намерения
        if (bundleExtras != null) { // Если намерение не пустое
            awID = bundleExtras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID) // Получаем ID виджета
        } else  // Иначе
            finish() // Закрываем все к чертям
    }
}