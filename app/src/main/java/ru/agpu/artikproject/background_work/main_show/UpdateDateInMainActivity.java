package ru.agpu.artikproject.background_work.main_show;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.layout.MainActivity;

public class UpdateDateInMainActivity {
    /**
     * Устанавливает текущую неделю на главном экране
     * @param act Активити
     */
    public UpdateDateInMainActivity(Activity act){
        Cursor f = MainActivity.sqLiteDatabase.rawQuery("SELECT week_s, week_po FROM weeks_list WHERE week_id = '" + MainActivity.week_id + "'", null);
        f.moveToFirst();
        String s_po = f.getString(0) + " " + f.getString(1);
        TextView current_week = act.findViewById(R.id.subtitle);
        act.runOnUiThread(() -> current_week.setText(s_po));

        Calendar tempCalendar = ChangeDay.dateAndTime;

        tempCalendar.add(Calendar.DAY_OF_WEEK, MainActivity.week_day);
        @SuppressLint("SimpleDateFormat") String day = (new SimpleDateFormat("dd.MM.yyyy, EEEE")).format(tempCalendar.getTime());
        String today_date = act.getString(R.string.Curent_day) + " " + day;
        TextView today = act.findViewById(R.id.main_activity_text);
        act.runOnUiThread(() -> today.setText(today_date));
    }
}
