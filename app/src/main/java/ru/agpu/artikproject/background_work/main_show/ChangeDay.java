package ru.agpu.artikproject.background_work.main_show;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.layout.MainActivity;

public class ChangeDay {
    Activity act;
    public static Calendar dateAndTime = Calendar.getInstance(); // Один календарь на все приложение

    public ChangeDay(Activity act){
        this.act = act;
        setInitialDateTime();
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate() {
        new DatePickerDialog(act, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    /**
     * Установка даты (выбор недели)
     */
    private void setInitialDateTime() {
        int dayOfWeek = 2; // Конкретно нам нужен понедельник (начало недели)
        int weekday = dateAndTime.get(Calendar.DAY_OF_WEEK); // Текущий день недели (1..7)

        if (weekday == 1)
            MainActivity.week_day = 5;
        else
            MainActivity.week_day = weekday - 2; // Устанавливаем выбранный день на главной странице

        // Считаем дату понедельника
        while (weekday != dayOfWeek){ // Если выбран не понедельник
            if (weekday == 1) { // Если выбрано воскресенье
                weekday = dayOfWeek; // Ставим день недели понедельник
                dateAndTime.add(Calendar.WEEK_OF_MONTH, -1); // Возвращаем календарь на неделю назад (т.к. вс-первый день новой недели | шатал я этим америкосов со своими стандартами, почему все не как у нормальных людей)
                dateAndTime.add(Calendar.DAY_OF_WEEK, 1); // Добавляем день (1->2) (ставим понедельник)
            }
            else{ // Если не ПН и не ВС
                weekday -= 1; // Отнимаем день в счетчике
                dateAndTime.add(Calendar.DAY_OF_WEEK, -1); // Отнимаем день в календаре
            }
        }

        // Приводим дату к виду дд.мм.гггг
        Date date2 = dateAndTime.getTime();
        @SuppressLint("SimpleDateFormat") String dateStr = new SimpleDateFormat("dd.MM.yyyy").format(date2);

        // Получаем номер недели из базы данных и обновляем на главной странице
        Cursor r = MainActivity.sqLiteDatabase.rawQuery("SELECT * FROM weeks_list WHERE week_s LIKE '%" + dateStr + "%'",null);
        if (r.getCount() > 0){ // Если неделя есть в базе данных
            r.moveToFirst(); // Берем первую полученную позицию выборки
            MainActivity.week_id = Integer.parseInt(r.getString(0)); // Устанавливаем новый номер недели
            new UpdateDateInMainActivity(act); // Обновляем текст на главном экране
        }
        else{ // Если недели нет в базе данных
            Toast.makeText(act, R.string.Not_find_week, Toast.LENGTH_LONG).show(); // Выводим сообщение об ошибке выбора даты
            dateAndTime = Calendar.getInstance(); // Ставим текущую дату
            setInitialDateTime(); // Обновляем данные
        }
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
        dateAndTime.set(Calendar.YEAR, year);
        dateAndTime.set(Calendar.MONTH, monthOfYear);
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setInitialDateTime();
    };
}
