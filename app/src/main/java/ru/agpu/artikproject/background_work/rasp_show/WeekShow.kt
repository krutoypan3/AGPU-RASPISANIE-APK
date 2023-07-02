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
import ru.agpu.artikproject.background_work.datebase.Raspisanie
import ru.agpu.artikproject.background_work.datebase.RaspisanieRepository
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

            val raspisanieRepository = RaspisanieRepository()

            val raspisanie = raspisanieRepository.getParaByParams(Raspisanie(
                groupCode = MainActivity.selectedItem_id.toIntOrNull(),
                weekNumber = MainActivity.week_id,
            )).sortedWith(compareBy({ it.weekDay }, { it.paraNumber }))

            if (raspisanie.isNotEmpty()) {
                var prevTime: String? = ""
                weekParaView.removeAllViews()
                var tableRow = TableRow(context) // Новый столбец
                var str: String? = ""
                var qty = TextView(context)// Новая ячейка
                var emptyCell = TextView(context) // Новая пустая ячейка
                qqty = arrayOfNulls(60)
                tableRows = arrayOfNulls(6)
                var ff = 0
                var fk = 0
                val mainText = raspisanie.first().weekDayName + " " + raspisanie.first().weekDayDate
                mainTextTV?.text = mainText
                val timeRow = TableRow(context) // Новый столбец

                // Установка заголовков
                raspisanie.take(8).forEach { r2 ->
                    qty = TextView(context) // Новая ячейка
                    qty.maxEms = 10
                    qty.textSize = table_size.toFloat()
                    qty.setPadding(5, 5, 5, 5)
                    qty.setTextColor(GetTextColor.getAppColor(context))
                    qty.setBackgroundResource(R.drawable.table_granitsa_legenda)
                    qty.gravity = Gravity.CENTER
                    qty.text = r2.paraRazmer
                    emptyCell = TextView(context) // Новая пустая ячейка
                    emptyCell.height = 1
                    emptyCell.width = 1
                    timeRow.addView(emptyCell) // Добавление пустой ячейки в столбец
                    timeRow.addView(qty)
                }

                weekParaView.addView(timeRow)
                raspisanie.forEach { r->
                    if ((prevTime == r.paraRazmer)) {
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
                            qty.setBackgroundColor(Color.parseColor(r.paraColor))
                        } catch (e: Exception) {
                            qty.setBackgroundResource(R.color.gray)
                        }
                        qty.gravity = Gravity.CENTER
                    }
                    prevTime = r.paraRazmer
                    if (r.paraName != null) {
                        str += r.paraName + "\n"
                        r.paraPrepod?.let { str += "$it\n" }
                        r.paraGroup?.let { str += "$it\n" }
                        r.paraPodgroup?.let { str += "$it\n" }
                        r.paraAud?.let { str += "$it\n" }
                        r.paraDistant?.let { str += "$it\n" }
                    }
                    if ((r.paraNumber == 0)) {
                        tableRow = TableRow(context) // Новый пустой столбец
                        emptyCell = TextView(context) // Новая пустая ячейка
                        emptyCell.height = 1
                        tableRow.addView(emptyCell) // Добавление пустой ячейки в столбец
                        weekParaView.addView(tableRow) // Добавление столбца в таблицу
                        tableRow = TableRow(context) // Новый столбец
                        tableRow.gravity = Gravity.CENTER_VERTICAL
                        str = r.weekDayName
                        str += "\n" + (r.weekDayDate)
                        weekParaView.addView(tableRow) // Добавление столбца в таблицу
                        tableRows?.set(fk, tableRow)
                        fk++
                        try {
                            qty.setBackgroundColor(Color.parseColor(r.paraColor))
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
                }
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