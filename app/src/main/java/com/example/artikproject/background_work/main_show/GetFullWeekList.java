package com.example.artikproject.background_work.main_show;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.artikproject.R;
import com.example.artikproject.background_work.CheckInternetConnection;
import com.example.artikproject.background_work.CustomAlertDialog;
import com.example.artikproject.background_work.rasp_show.GetRasp;
import com.example.artikproject.layout.MainActivity;
import com.example.artikproject.layout.Raspisanie_show;

public class GetFullWeekList extends Thread {
    Activity act;

    public GetFullWeekList(Activity act, View v){
        this.act = act;
        v.startAnimation(MainActivity.animScale);
    }

    @Override
    public void run() {
        ArrayAdapter<String> adapter = new ArrayAdapter(act.getApplicationContext(), R.layout.listviewadapterbl, GetFullWeekList_Button.weeks_s_po);
        try{
            act.runOnUiThread(() -> {
                CustomAlertDialog cdd = new CustomAlertDialog(act, "weeks_list");
                cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
                cdd.show();
                cdd.list_view.setAdapter(adapter);
                cdd.list_view.setOnItemClickListener((parent, v, pos, id) ->{
                    MainActivity.week_id = Integer.parseInt(GetFullWeekList_Button.weeks_id.get(pos));
                    cdd.dismiss();
                });
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

