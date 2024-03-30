package ru.agpu.artikproject.background_work.main_show

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.widget.DatePicker
import android.widget.Toast
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.AppData.AppDate.weekDay
import ru.agpu.artikproject.background_work.datebase.AppData.AppDate.weekId
import ru.oganesyanartem.core.data.repository.weeks_list.WeeksListImpl
import ru.oganesyanartem.core.domain.repository.WeeksListRepository
import ru.oganesyanartem.core.domain.usecase.weeks_list.WeeksListGetByLikeStartDateUseCase
import java.text.SimpleDateFormat
import java.util.Calendar

class ChangeDay(var act: Activity) {
    companion object {
        var chosenDateCalendar: Calendar = Calendar.getInstance() // Календарь текущей даты на все приложение
    }

    init {
        setInitialDateTime()
    }

    // отображаем диалоговое окно для выбора даты
    fun setDate() {
        DatePickerDialog(
            act, d,
            chosenDateCalendar.get(Calendar.YEAR),
            chosenDateCalendar.get(Calendar.MONTH),
            chosenDateCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    /**
     * Установка даты (выбор недели)
     */
    private fun setInitialDateTime() {
        val dayOfWeek = 2 // Конкретно нам нужен понедельник (начало недели)
        var weekday = chosenDateCalendar[Calendar.DAY_OF_WEEK] // Текущий день недели (1..7)

        // Нельзя просто взять и одному календарю присвоить другой
        val weekStartCalendar = Calendar.getInstance()
        weekStartCalendar.timeInMillis = chosenDateCalendar.timeInMillis

        if (weekday == 1) weekDay = 5
        else weekDay = weekday - 2 // Устанавливаем выбранный день на главной странице

        // Считаем дату понедельника
        while (weekday != dayOfWeek) { // Если выбран не понедельник
            if (weekday == 1) { // Если выбрано воскресенье
                weekday = dayOfWeek // Ставим день недели понедельник
                weekStartCalendar.add(Calendar.WEEK_OF_MONTH, -1) // Возвращаем календарь на неделю назад (т.к. вс-первый день новой недели | шатал я этим америкосов со своими стандартами, почему все не как у нормальных людей)
                weekStartCalendar.add(Calendar.DAY_OF_WEEK, 1) // Добавляем день (1->2) (ставим понедельник)
            } else { // Если не ПН и не ВС
                weekday -= 1 // Отнимаем день в счетчике
                weekStartCalendar.add(Calendar.DAY_OF_WEEK, -1) // Отнимаем день в календаре
            }
        }

        // Приводим дату к виду дд.мм.гггг
        val date2 = weekStartCalendar.time
        @SuppressLint("SimpleDateFormat") val dateStr = SimpleDateFormat("dd.MM.yyyy").format(date2)

        // Получаем номер недели из базы данных и обновляем на главной странице
        val weeksListRepository: WeeksListRepository = WeeksListImpl(act.applicationContext)
        val weeksListItems = WeeksListGetByLikeStartDateUseCase(weeksListRepository, dateStr).execute()
        if (weeksListItems.isNotEmpty()) { // Если неделя есть
            // Берем первую полученную позицию выборки
            weekId = weeksListItems[0].weekId // Устанавливаем новый номер недели
            UpdateDateInMainActivity(act).start() // Обновляем текст на главном экране
        } else { // Если недели нет в базе данных
            Toast.makeText(act, R.string.Not_find_week, Toast.LENGTH_LONG).show() // Выводим сообщение об ошибке выбора даты
            chosenDateCalendar = Calendar.getInstance() // Ставим текущую дату
        }
    }

    // установка обработчика выбора даты
    private var d = OnDateSetListener { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
        chosenDateCalendar[Calendar.YEAR] = year
        chosenDateCalendar[Calendar.MONTH] = monthOfYear
        chosenDateCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        setInitialDateTime()
    }
}