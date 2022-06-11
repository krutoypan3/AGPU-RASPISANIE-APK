package ru.agpu.artikproject.background_work.main_show;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.layout.MainActivity;

public class UpdateDateInMainActivity extends Thread{

    private final Activity act;
    /**
     * Устанавливает дату на главном экране
     * @param act Активити
     */
    public UpdateDateInMainActivity(Activity act) {
        this.act = act;
    }

    @Override
    public void run(){
        try {
            Cursor f = DataBase_Local.sqLiteDatabase.rawQuery("SELECT week_s, week_po FROM weeks_list WHERE week_id = '" + MainActivity.week_id + "'", null);
            f.moveToFirst();
            String s_po = f.getString(0) + " " + f.getString(1);
            act.runOnUiThread(() -> {
                try{
                    TextView current_week = act.findViewById(R.id.subtitle);
                    current_week.setText(s_po);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            });

            @SuppressLint("SimpleDateFormat") String day = (new SimpleDateFormat("dd.MM.yyyy, EEEE")).format(ChangeDay.chosenDateCalendar.getTime());
            String today_date = act.getString(R.string.Curent_day) + " " + day;
            act.runOnUiThread(() -> {
                try{
                    TextView today = act.findViewById(R.id.main_activity_text);
                    today.setText(today_date);
                }catch (Exception e) {
                e.printStackTrace();
            }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
