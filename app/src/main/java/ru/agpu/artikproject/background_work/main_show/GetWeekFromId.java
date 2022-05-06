package ru.agpu.artikproject.background_work.main_show;

import android.app.Activity;
import android.database.Cursor;
import android.widget.TextView;

import ru.agpu.artikproject.layout.MainActivity;
import ru.agpu.artikproject.R;

public class GetWeekFromId {
    /**
     * Устанавливает текущую неделю на главном экране
     * @param act Активити
     */
    public GetWeekFromId(Activity act){
        Cursor f = MainActivity.sqLiteDatabase.rawQuery("SELECT week_s, week_po FROM weeks_list WHERE week_id = '" + MainActivity.week_id + "'", null);
        f.moveToFirst();
        String s_po = f.getString(0) + " " + f.getString(1);
        TextView current_week = act.findViewById(R.id.subtitle);
        act.runOnUiThread(() -> current_week.setText(s_po));
    }
}
