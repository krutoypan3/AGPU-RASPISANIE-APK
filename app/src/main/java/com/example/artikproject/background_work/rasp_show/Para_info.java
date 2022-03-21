package com.example.artikproject.background_work.rasp_show;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.ArrayAdapter;

import com.example.artikproject.R;
import com.example.artikproject.background_work.CustomAlertDialog;
import com.example.artikproject.background_work.GetCorpFromAudNumber;
import com.example.artikproject.layout.MainActivity;
import com.example.artikproject.layout.Raspisanie_show;

import java.util.ArrayList;
import java.util.List;

public class Para_info {
    private int pashalka = 0;
    public static int finalCorp;
    public Para_info(int position, Activity act){
        Object para_time = Raspisanie_show.day_para_view.getItemAtPosition(position).toString().split("\n")[0];
        Object prepod_aud = Raspisanie_show.day_para_view.getItemAtPosition(position).toString().split("\n")[2];
        Cursor r = MainActivity.sqLiteDatabase.rawQuery("SELECT * FROM rasp_test1 WHERE " +
                "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                "r_week_number = " + MainActivity.week_id + " AND " +
                "r_week_day = " + MainActivity.week_day + " AND " +
                "r_razmer = '" + para_time + "' AND " +
                "r_prepod = '" + prepod_aud + "'", null);
        if (r.getCount()!=0) {
            r.moveToFirst();
            List<String> group_list = new ArrayList<>();
            if (r.getString(4) != null){
                if (r.getString(9) != null)
                group_list.add(act.getApplicationContext().getResources().getString(R.string.Time) +
                        " : " + r.getString(9));
                if (r.getString(4) != null)
                    group_list.add(act.getApplicationContext().getResources().getString(R.string.CoupleName) +
                            " : " + r.getString(4));
                if (r.getString(5) != null)
                    group_list.add(act.getApplicationContext().getResources().getString(R.string.Prepod) +
                            " : " + r.getString(5).split(",")[0]);
                if (r.getString(5) != null)
                    group_list.add(act.getApplicationContext().getResources().getString(R.string.Audience) +
                            " : " + r.getString(5).split(",")[r.getString(5).split(",").length-1]);
                if (r.getString(6) != null)
                    group_list.add(act.getApplicationContext().getResources().getString(R.string.Group) +
                            " : " + r.getString(6).replace("(","").replace(")",""));
                if (r.getString(7) != null)
                    group_list.add(act.getApplicationContext().getResources().getString(R.string.PodGroup) +
                            " : " + r.getString(7).replace("(","").replace(")",""));
                if (r.getString(8) != null) group_list.add(r.getString(8));
                if (r.getString(15) != null) group_list.add(r.getString(15));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter(act.getApplicationContext(), R.layout.listviewadapterbl, group_list);
            try{
                CustomAlertDialog cdd = new CustomAlertDialog(act, "para_info");
                cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
                cdd.show();
                cdd.list_view.setAdapter(adapter);
                cdd.list_view.setOnItemClickListener((parent, v, pos, id) ->{
                    switch(pos){
                        case(3): // Клик по аудитории
                            String aud = (String) cdd.list_view.getItemAtPosition(pos);
                            aud = aud.split(": ")[1];
                            finalCorp = new GetCorpFromAudNumber().getCorp(act, aud);
                            CustomAlertDialog dialog_confirm = new CustomAlertDialog(act, "map_confirm");
                            dialog_confirm.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
                            dialog_confirm.show();
                            break;
                        case(2):
                            String sss = cdd.list_view.getItemAtPosition(pos).toString().split(",")[0].split(": ")[1];
                            if (sss.equals("Козлов В.А.")) {
                                if (pashalka == 3) {
                                    AudioManager audioManager = (AudioManager) act.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
                                    MediaPlayer mp = MediaPlayer.create(act, R.raw.povezlo_povezlo);
                                    mp.start();
                                    pashalka = 0;
                                }
                                else{
                                    pashalka += 1;
                                }
                            }
                            break;
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
