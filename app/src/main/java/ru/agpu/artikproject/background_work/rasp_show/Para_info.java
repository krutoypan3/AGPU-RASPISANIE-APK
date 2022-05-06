package ru.agpu.artikproject.background_work.rasp_show;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Random;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CustomAlertDialog;
import ru.agpu.artikproject.background_work.GetCorpFromAudNumber;
import ru.agpu.artikproject.background_work.adapters.ListView.ListViewAdapter;
import ru.agpu.artikproject.background_work.adapters.ListView.ListViewItems;
import ru.agpu.artikproject.background_work.settings_layout.ficha.Ficha_achievements;
import ru.agpu.artikproject.layout.MainActivity;
import ru.agpu.artikproject.layout.Raspisanie_show;

public class Para_info {
    public static int finalCorp;
    public Para_info(int position, Activity act){
        ListViewItems para_adap = (ListViewItems) Raspisanie_show.day_para_view.getItemAtPosition(position);
        String para_time = para_adap.item.split("\n")[0];
        String prepod_aud = para_adap.item.split("\n")[2];
        Cursor r = MainActivity.sqLiteDatabase.rawQuery("SELECT * FROM raspisanie WHERE " +
                "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                "r_week_number = " + MainActivity.week_id + " AND " +
                "r_week_day = " + MainActivity.week_day + " AND " +
                "r_razmer = '" + para_time + "' AND " +
                "r_prepod = '" + prepod_aud + "'", null);
        if (r.getCount() != 0) {
            r.moveToFirst();
            ArrayList<ListViewItems> group_list = new ArrayList<>();
            if (r.getString(4) != null) {
                if (r.getString(9) != null)
                    group_list.add(new ListViewItems(act.getApplicationContext().getResources().getString(R.string.Time) +
                            " : " + r.getString(9)));
                if (r.getString(4) != null)
                    group_list.add(new ListViewItems(act.getApplicationContext().getResources().getString(R.string.CoupleName) +
                            " : " + r.getString(4)));
                if (r.getString(5) != null)
                    group_list.add(new ListViewItems(act.getApplicationContext().getResources().getString(R.string.Prepod) +
                            " : " + r.getString(5).split(",")[0]));
                if (r.getString(5) != null)
                    group_list.add(new ListViewItems(act.getApplicationContext().getResources().getString(R.string.Audience) +
                            " : " + r.getString(5).split(",")[r.getString(5).split(",").length - 1]));
                if (r.getString(6) != null)
                    group_list.add(new ListViewItems(act.getApplicationContext().getResources().getString(R.string.Group) +
                            " : " + r.getString(6).replace("(", "").replace(")", "")));
                if (r.getString(7) != null)
                    group_list.add(new ListViewItems(act.getApplicationContext().getResources().getString(R.string.PodGroup) +
                            " : " + r.getString(7).replace("(", "").replace(")", "")));
                if (r.getString(8) != null) group_list.add(new ListViewItems(r.getString(8)));
                if (r.getString(15) != null) group_list.add(new ListViewItems(r.getString(15)));
            }
            ListViewAdapter adapter = new ListViewAdapter(act.getApplicationContext(), group_list);
            try {
                CustomAlertDialog cdd = new CustomAlertDialog(act, "para_info");
                cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
                cdd.show();
                cdd.list_view.setAdapter(adapter);
                cdd.list_view.setOnItemClickListener((parent, v, pos, id) -> {
                    switch (pos) {
                        case (1):
                            String sss = ((ListViewItems)cdd.list_view.getItemAtPosition(pos)).item;
                            if (sss.contains("практика") || sss.contains("экз.") || sss.contains("зач.") ||
                                    sss.contains("экзамен") || sss.contains("зачет")  || sss.contains("зачёт")){
                                Ficha_achievements.put(act.getApplicationContext(), "ficha_god");
                                CustomAlertDialog cdd2 = new CustomAlertDialog(act, "para_pasha");
                                cdd2.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
                                cdd2.show();
                                ArrayList<ListViewItems> molitva = new ArrayList<>();
                                molitva.add(new ListViewItems("Да восвятится имя твое"));
                                molitva.add(new ListViewItems("Да покаешься ты в грехах своих"));
                                molitva.add(new ListViewItems("Да закроешь ты сессию эту"));
                                ListViewAdapter adapter2 = new ListViewAdapter(act.getApplicationContext(), molitva);
                                cdd2.list_view.setAdapter(adapter2);
                                AudioManager audioManager = (AudioManager) act.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
                                MediaPlayer mp = MediaPlayer.create(act, R.raw.ficha_god);
                                mp.start();
                            }
                            break;
                        case (3): // Клик по аудитории
                            String aud = ((ListViewItems) cdd.list_view.getItemAtPosition(pos)).item;
                            aud = aud.split(": ")[1];
                            finalCorp = new GetCorpFromAudNumber().getCorp(act, aud);
                            CustomAlertDialog dialog_confirm = new CustomAlertDialog(act, "map_confirm");
                            dialog_confirm.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
                            dialog_confirm.show();
                            break;
                        case (2):
                            sss = ((ListViewItems)cdd.list_view.getItemAtPosition(pos)).item.split(",")[0].split(": ")[1];

                            // Этот блок кода созда исключительно в развлекательных целях и не несет в себе цель кого-то задеть или обидеть
                            if (sss.equals("Козлов В.А.")) {
                                if (new Random().nextInt(5) == 0) {
                                    Ficha_achievements.put(act.getApplicationContext(), "ficha_para_kozlov");
                                    AudioManager audioManager = (AudioManager) act.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
                                    MediaPlayer mp = MediaPlayer.create(act, R.raw.povezlo_povezlo);
                                    mp.start();
                                }
                            }
                            else if (sss.equals("Лапшин Н.А.")) {
                                if (new Random().nextInt(5) == 0) {
                                    Ficha_achievements.put(act.getApplicationContext(), "ficha_para_lapshin");
                                    AudioManager audioManager = (AudioManager) act.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
                                    MediaPlayer mp = MediaPlayer.create(act, R.raw.povezlo_povezlo);
                                    mp.start();
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://i.ibb.co/jLRpZ1B/2022-04-29-203438.png"));
                                    act.startActivity(intent);
                                }
                            }
                            break;
                            // Конец блока развлекательного кода
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
