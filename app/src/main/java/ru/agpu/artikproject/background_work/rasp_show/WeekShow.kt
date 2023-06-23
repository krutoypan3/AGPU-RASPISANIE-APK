package ru.agpu.artikproject.background_work.rasp_show

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite.Companion.getSqliteDatabase
import ru.agpu.artikproject.background_work.theme.GetTextColor
import ru.agpu.artikproject.presentation.layout.MainActivity

class WeekShow(context: Context) {
    companion object {
        var qqty: Array<TextView?>? = null
        var table_size = 14
    }

    var mainTextTV: TextView? = null
    var tableRows: Array<TableRow?>? = null
    init {
        try {
            val activity = context as Activity
            mainTextTV = activity.findViewById(R.id.main_text)
            val weekParaView = activity.findViewById<TableLayout>(R.id.week_para_view)

            val r = getSqliteDatabase(context).rawQuery(
                "SELECT * FROM raspisanie WHERE " +
                        "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                        "r_week_number = " + MainActivity.week_id + " ORDER BY r_week_day, r_para_number",
                null
            )
            val f = getSqliteDatabase(context).rawQuery(
                ("SELECT DISTINCT r_razmer FROM raspisanie WHERE " +
                        "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                        "r_week_number = " + MainActivity.week_id + " ORDER BY r_week_day, r_para_number"),
                null
            )

            if (r.count != 0) {
                var prevTime: String? = ""
                weekParaView.removeAllViews()
                var tableRow = TableRow(context) // Новый столбец
                var str: String? = ""
                var qty: TextView // Новая ячейка
                var emptyCell: TextView // Новая пустая ячейка
                qqty = arrayOfNulls(60)
                tableRows = arrayOfNulls(6)
                var ff = 0
                var fk = 0
                r.moveToFirst()
                val mainText = r.getString(10) + " " + r.getString(11)
                mainTextTV?.text = mainText
                f.moveToFirst()
                val timeRow = TableRow(context) // Новый столбец
                do {
                    qty = TextView(context) // Новая ячейка
                    qty.maxEms = 10
                    qty.textSize = table_size.toFloat()
                    qty.setPadding(5, 5, 5, 5)
                    qty.setTextColor(GetTextColor.getAppColor(context))
                    qty.setBackgroundResource(R.drawable.table_granitsa_legenda)
                    qty.gravity = Gravity.CENTER
                    qty.text = f.getString(0)
                    emptyCell = TextView(context) // Новая пустая ячейка
                    emptyCell.height = 1
                    emptyCell.width = 1
                    timeRow.addView(emptyCell) // Добавление пустой ячейки в столбец
                    timeRow.addView(qty)
                } while (f.moveToNext())
                weekParaView.addView(timeRow)
                do {
                    if ((prevTime == r.getString(9))) {
                        str += "\n"
                        tableRow.removeView(qty)
                        tableRow.removeView(emptyCell)
                    } else {
                        str = ""
                        qty = TextView(context) // Новая ячейка
                        qty.maxEms = 10
                        qty.textSize = table_size.toFloat()
                        qty.setPadding(0, 5, 5, 5)
                        qty.setTextColor(ContextCompat.getColor(context, R.color.black))
                        try {
                            qty.setBackgroundColor(Color.parseColor(r.getString(14)))
                        } catch (e: Exception) {
                            qty.setBackgroundResource(R.color.gray)
                        }
                        qty.gravity = Gravity.CENTER
                    }
                    prevTime = r.getString(9)
                    if (r.getString(4) != null) {
                        str += r.getString(4) + "\n"
                        if (r.getString(5) != null) str += r.getString(5) + "\n"
                        if (r.getString(6) != null) str += r.getString(6) + "\n"
                        if (r.getString(7) != null) str += r.getString(7) + "\n"
                        if (r.getString(8) != null) str += r.getString(8) + "\n"
                        if (r.getString(15) != null) str += r.getString(15) + "\n"
                    }
                    if ((r.getString(3) == "0")) {
                        tableRow = TableRow(context) // Новый пустой столбец
                        emptyCell = TextView(context) // Новая пустая ячейка
                        emptyCell.height = 1
                        tableRow.addView(emptyCell) // Добавление пустой ячейки в столбец
                        weekParaView.addView(tableRow) // Добавление столбца в таблицу
                        tableRow = TableRow(context) // Новый столбец
                        tableRow.gravity = Gravity.CENTER_VERTICAL
                        str = r.getString(10)
                        str += "\n" + (r.getString(11))
                        weekParaView.addView(tableRow) // Добавление столбца в таблицу
                        tableRows?.set(fk, tableRow)
                        fk++
                        try {
                            qty.setBackgroundColor(Color.parseColor(r.getString(14)))
                        } catch (e: Exception) {
                            qty.setBackgroundResource(R.drawable.table_granitsa_legenda)
                        }
                        qty.setTextColor(GetTextColor.getAppColor(context))
                    }
                    qty.text = str
                    emptyCell = TextView(context) // Новая пустая ячейка
                    emptyCell.height = 1
                    emptyCell.width = 1
                    tableRow.addView(emptyCell) // Добавление пустой ячейки в столбец
                    tableRow.addView(qty) // Добавление ячейки в столбец
                    qqty?.set(ff,qty)
                    ff++
                } while (r.moveToNext())
                tableRow.removeView(qty) // Удаление последней ячейки(хз что за ячейка (мусор какой-то))
                WeekShowResize().resize()
                mainTextTV?.text = ""
            }
        } catch (e: Exception) { // Если недели нет в базе то ...
            e.printStackTrace()
            mainTextTV?.setText(R.string.rasp_error)
        }
    }
}