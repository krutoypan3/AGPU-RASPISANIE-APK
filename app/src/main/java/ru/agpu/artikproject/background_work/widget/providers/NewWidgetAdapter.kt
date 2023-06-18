package ru.agpu.artikproject.background_work.widget.providers

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import androidx.core.database.getStringOrNull
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences
import ru.agpu.artikproject.background_work.image_utils.GetRoundedCornerBitmap
import ru.agpu.artikproject.background_work.theme.ColorChanger
import ru.agpu.artikproject.background_work.widget.WidgetGridViewItem
import ru.oganesyanartem.core.data.repository.CurrentWeekDayImpl
import ru.oganesyanartem.core.data.repository.current_week_id.CurrentWeekIdImpl
import ru.oganesyanartem.core.domain.usecase.CurrentWeekDayGetUseCase
import ru.oganesyanartem.core.domain.usecase.CurrentWeekIdGetUseCase


class NewWidgetAdapter(val context: Context, intent: Intent) : RemoteViewsFactory {
    private var data: ArrayList<WidgetGridViewItem> = ArrayList() // Список с парами
    private val widgetID: Int // Номер виджета
    val textColor: Int

    /**
     * При создании класса обнуляем список
     */
    override fun onCreate() {
        data = ArrayList()
    }

    /**
     * Получаем количество пар в списке
     * @return Количество пар
     */
    override fun getCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    /**
     * Установка пары в свою позицию
     * @param position позиция пары
     */
    override fun getViewAt(position: Int): RemoteViews {
        val rView = RemoteViews(
            context.packageName,
            R.layout.widget_gv_item
        )
        val viewItem = data[position]

        rView.setTextViewText(R.id.time_range, viewItem.timeRange)
        rView.setTextViewText(R.id.item_name, viewItem.itemName)
        rView.setTextViewText(R.id.item_prepod_and_time, viewItem.itemPrepodAndTime)
        rView.setTextViewText(R.id.item_group, viewItem.itemGroup)

        rView.setTextColor(R.id.time_range, textColor)
        rView.setTextColor(R.id.item_name, textColor)
        rView.setTextColor(R.id.item_prepod_and_time, textColor)
        rView.setTextColor(R.id.item_group, textColor)

        var backgroundColor = viewItem.backgroundConstraint
        if (textColor == context.getColor(R.color.white))
            backgroundColor = ColorChanger.GetDarkColor(viewItem.backgroundConstraint, 120)

        val bitmap = GetRoundedCornerBitmap.getRoundedCornerBitmapByColor(
            900,
            300,
            backgroundColor,
            30
        )

        rView.setImageViewBitmap(R.id.background_image_view, bitmap)

// TODO Если нужно будет изменить фон ращделителя в зависимости от темы... separator
        return rView
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    /**
     * Тут заполняем список пар
     */
    override fun onDataSetChanged() {
        try {
            data.clear() // Обнуляем список
            val week_day = CurrentWeekDayGetUseCase(CurrentWeekDayImpl()).execute()
            val week_id = CurrentWeekIdGetUseCase(CurrentWeekIdImpl(context)).execute()
            val selectedItem_id =
                MySharedPreferences.get(context, widgetID.toString() + "_selected_item_id", "")
            if (selectedItem_id != "") {
                val r = DataBaseSqlite.getSqliteDatabase(context).rawQuery(
                    "SELECT * FROM raspisanie WHERE " +
                            "r_group_code = " + selectedItem_id + " AND " +
                            "r_week_number = " + week_id + " AND " +
                            "r_week_day = " + week_day + " ORDER BY r_para_number", null
                )
                if (r.count != 0) {
                    var str: String
                    r.moveToFirst()
                    do {
                        str = ""
                        if (r.getString(4) != null) {

                            if (r.getString(5) != null) str += r.getString(5).trimIndent()
                            if (r.getString(6) != null) str += r.getString(6).trimIndent()
                            if (r.getString(7) != null) str += r.getString(7).trimIndent()
                            if (r.getString(8) != null) str += r.getString(8).trimIndent()
                            if (r.getString(15) != null) str += r.getString(15).trimIndent()

                            val timeRange = r.getString(9)
                            val newTimeRange = timeRange.trimIndent().split("-")[0] +
                                    "\n\n\n\n" + timeRange.trimIndent().split("-")[1]


                            val currentItem = WidgetGridViewItem(
                                timeRange = newTimeRange,
                                separator = 0,
                                backgroundConstraint = Color.parseColor(
                                    r.getStringOrNull(14) ?: CONSTRAINT_BACKGROUND_DEFAULT
                                ),
                                itemName = r.getStringOrNull(4)?.trimIndent() ?: PARA_NAME_DEFAULT,
                                itemPrepodAndTime = str,
                                itemGroup = r.getStringOrNull(6)?.trimIndent() ?: GROUP_NAME_DEFAULT
                            )
                            data.add(currentItem)
                        }
                    } while (r.moveToNext())
                    if (data.isEmpty()) {
                        data.add(
                            WidgetGridViewItem(
                                timeRange = ">",
                                separator = 0,
                                backgroundConstraint = Color.parseColor(
                                    CONSTRAINT_BACKGROUND_DEFAULT
                                ),
                                itemName = context.getString(R.string.day_off),
                                itemPrepodAndTime = ">_<",
                                itemGroup = ""
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {}

    companion object {
        const val CONSTRAINT_BACKGROUND_DEFAULT = "#000000"
        const val PARA_NAME_DEFAULT = "LOL, I:DON'T:KNOW"
        const val GROUP_NAME_DEFAULT = ""
    }

    init {
        // Заполняем переменные данными из конструктора
        widgetID = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        textColor = intent.getIntExtra("text_color", R.color.black)
    }
}