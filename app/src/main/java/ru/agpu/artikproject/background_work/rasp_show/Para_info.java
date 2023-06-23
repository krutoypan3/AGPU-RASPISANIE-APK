package ru.agpu.artikproject.background_work.rasp_show;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CustomAlertDialog;
import ru.agpu.artikproject.background_work.GetCorpFromAudNumber;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems;
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite;
import ru.agpu.artikproject.background_work.rasp_show.recycler_view.RecyclerViewItems;
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements;
import ru.agpu.artikproject.presentation.layout.MainActivity;

public class Para_info {
    public static int finalCorp;

    /**
     * Класс отвечающий за вывод подробной информации о паре
     * @param position Позиция элемента в списке
     * @param act Активити
     * @param datas Элементы списка
     */
    public Para_info(int position, Activity act, List<RecyclerViewItems> datas){
        RecyclerViewItems para_adap =datas.get(position);

        String para_time = para_adap.getCard_para_time().replace("\n", "-");
        String prepod_aud = para_adap.getCard_para_prepod();
        int background_color = para_adap.getPara_description_layout_color();
        int background_color2 = para_adap.getPara_num_and_time_layout_color();

        GradientDrawable shape = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {background_color, background_color2});
        shape.setCornerRadius(50);


        prepod_aud = prepod_aud.replace( act.getString(R.string.Prepod) + ": ", "");
        Cursor r = DataBaseSqlite.Companion.getSqliteDatabase(act).rawQuery("SELECT * FROM raspisanie WHERE " +
                "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                "r_week_number = " + MainActivity.week_id + " AND " +
                "r_week_day = " + MainActivity.week_day + " AND " +
                "r_razmer = '" + para_time + "' AND " +
                "r_prepod LIKE '%" + prepod_aud.split("\\.")[0] + "%'", null);
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
                cdd.show();
                cdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                cdd.findViewById(R.id.scrollViewCustom).setBackground(shape);
                cdd.list_view.setAdapter(adapter);
                cdd.list_view.setOnItemClickListener((parent, v, pos, id) -> {
                    switch (pos) {
                        case (1):
                            String sss = ((ListViewItems)cdd.list_view.getItemAtPosition(pos)).item;
                            if (sss.contains("практика") || sss.contains("экз.") || sss.contains("зач.") ||
                                    sss.contains("экзамен") || sss.contains("зачет")  || sss.contains("зачёт")){
                                FichaAchievements.Companion.put(act.getApplicationContext(), "ficha_god");
                                CustomAlertDialog cdd2 = new CustomAlertDialog(act, "para_pasha");
                                cdd2.show();
                                cdd2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                cdd2.findViewById(R.id.scrollViewCustom).setBackground(shape);
                                ArrayList<ListViewItems> molitva = new ArrayList<>();
                                molitva.add(new ListViewItems("Да восвятится имя твое"));
                                molitva.add(new ListViewItems("Да покаешься ты в грехах своих"));
                                molitva.add(new ListViewItems("Да закроешь ты сессию эту"));
                                ListViewAdapter adapter2 = new ListViewAdapter(act.getApplicationContext(), molitva);
                                cdd2.list_view.setAdapter(adapter2);
                                Glide.with(act).load("https://i.ibb.co/4pqtKcY/ficha-god.png").into(cdd2.para_info_photo);
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
                            dialog_confirm.show();
                            dialog_confirm.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            dialog_confirm.findViewById(R.id.scrollViewCustom).setBackground(shape);
                            break;
                        case (2):
                            sss = ((ListViewItems)cdd.list_view.getItemAtPosition(pos)).item.split(",")[0].split(": ")[1];

                            // Этот блок кода созда исключительно в развлекательных целях и не несет в себе цель кого-то задеть или обидеть
                            if (sss.equals("Лапшин Н.А.")) {
                                if (new Random().nextInt(5) == 0) {
                                    FichaAchievements.Companion.put(act.getApplicationContext(), "ficha_para_lapshin");
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
