package com.example.artikproject.background_work.main_show;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.artikproject.R;
import com.example.artikproject.background_work.CustomAlertDialog;
import com.example.artikproject.background_work.site_parse.GetFullWeekList_Online;
import com.example.artikproject.layout.MainActivity;

public class ShowFullWeekList extends Thread {
    Activity act;

    /**
     * Показывает полный список недель и обрабатывает нажатия на недели, устанавливая главную неделю
     * @param act Активити
     * @param v View
     */
    public ShowFullWeekList(Activity act, View v){
        this.act = act;
        v.startAnimation(MainActivity.animScale);
    }

    @Override
    public void run() {
        try{
            act.runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter(act.getApplicationContext(), R.layout.listviewadapterbl, GetFullWeekList_Online.weeks_s_po);
                CustomAlertDialog cdd = new CustomAlertDialog(act, "weeks_list");
                cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
                cdd.show();
                cdd.list_view.setAdapter(adapter);
                cdd.list_view.setOnItemClickListener((parent, v, pos, id) ->{
                    MainActivity.week_id = Integer.parseInt(GetFullWeekList_Online.weeks_id.get(pos)); // Получаем id выбранной недели из списка
                    MainActivity.week_day = 0; // Ставим понедельник
                    new GetWeekFromId(act);
                    cdd.dismiss();
                });
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

