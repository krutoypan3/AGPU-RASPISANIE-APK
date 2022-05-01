package com.example.artikproject.background_work.main_show;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.artikproject.R;
import com.example.artikproject.background_work.CheckAppUpdate;
import com.example.artikproject.background_work.CustomAlertDialog;
import com.example.artikproject.background_work.adapters.ListViewAdapter;
import com.example.artikproject.background_work.adapters.ListViewItems;
import com.example.artikproject.background_work.debug.Device_info;
import com.example.artikproject.background_work.settings_layout.Ficha_achievements;
import com.example.artikproject.layout.MainActivity;
import com.example.artikproject.layout.Settings_layout;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.util.ArrayList;
import java.util.Random;


public class MainToolBar {
    /**
     * Класс отвечающий за создание и наполнение тулбара главного меню
     * @param act Activity главного меню
     */
    public MainToolBar(Activity act) throws PackageManager.NameNotFoundException {
        ListView listview_aud = act.findViewById(R.id.listview_aud);
        Context context = act.getApplicationContext();
        MainActivity.drawerResult = new Drawer()
            .withActivity(act)  // В каком активити создать тулбар
            .withToolbar(MainActivity.toolbar)  // Выбираем сам тулбар
            .withActionBarDrawerToggle(true)
            .withHeader(R.layout.drawer_header) // С фото университета
            .addDrawerItems(  // Содержимое тулбара
                    new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                    new PrimaryDrawerItem().withName(R.string.drawer_item_location).withIcon(FontAwesome.Icon.faw_location_arrow).withIdentifier(2),
                    new SectionDrawerItem().withName(R.string.drawer_item_settings),
                    new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(3),
                    new SecondaryDrawerItem().withName(R.string.drawer_item_delete).withIcon(FontAwesome.Icon.faw_remove).withIdentifier(5),
                    new SecondaryDrawerItem().withName(R.string.drawer_item_github).withIcon(FontAwesome.Icon.faw_question).withIdentifier(6),
                    new DividerDrawerItem(),
                    new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github).withIdentifier(4),
                    new DividerDrawerItem(),
                    new SecondaryDrawerItem().withName(R.string.feedback).withIcon(FontAwesome.Icon.faw_question_circle).withIdentifier(7),
                    new SecondaryDrawerItem().withName(R.string.app_info).withIcon(FontAwesome.Icon.faw_exclamation_circle).withIdentifier(8),
                    new SecondaryDrawerItem().withName(context.getResources().getString(R.string.version) + ": " + Device_info.getAppVersion(context)).setEnabled(false)
            )
            .withOnDrawerItemClickListener((parent, view, position, id, drawerItem) -> {
                if (drawerItem instanceof Nameable) {
                    switch (drawerItem.getIdentifier()) {
                        case (1):
                            new ShowRaspSearch(act);
                            break;
                        case (2):
                            new ShowBuildingList(act);
                            ArrayList<ListViewItems> group_list_aud = new ArrayList<>();
                            group_list_aud.add(new ListViewItems(context.getResources().getString(R.string.adress_main) + "\n" +
                                    context.getResources().getString(R.string.Audiences) +
                                    ": " + context.getResources().getString(R.string.adress_main_aud) + "\n"));
                            group_list_aud.add(new ListViewItems(context.getResources().getString(R.string.adress_zaochka) + "\n" +
                                    context.getResources().getString(R.string.Audiences) +
                                    ": " + context.getResources().getString(R.string.adress_zaochka_aud) + "\n"));
                            group_list_aud.add(new ListViewItems(context.getResources().getString(R.string.adress_spf) + "\n" +
                                    context.getResources().getString(R.string.Audiences) +
                                    ": " + context.getResources().getString(R.string.adress_spf_aud) +"\n"));
                            group_list_aud.add(new ListViewItems(context.getResources().getString(R.string.adress_ebd) + "\n" +
                                    context.getResources().getString(R.string.Audiences) +
                                    ": " + context.getResources().getString(R.string.adress_ebd_aud) +"\n"));
                            group_list_aud.add(new ListViewItems(context.getResources().getString(R.string.adress_foc) + "\n" +
                                    context.getResources().getString(R.string.Audiences) +
                                    ": " + context.getResources().getString(R.string.adress_foc_aud) +"\n"));
                            group_list_aud.add(new ListViewItems(context.getResources().getString(R.string.adress_tehfak) + "\n" +
                                    context.getResources().getString(R.string.Audiences) +
                                    ": " + context.getResources().getString(R.string.adress_tehfak_aud) +"\n"));
                            group_list_aud.add(new ListViewItems(context.getResources().getString(R.string.adress_obshaga) + "\n" +
                                    context.getResources().getString(R.string.Audiences) +
                                    ": " + context.getResources().getString(R.string.adress_obshaga_aud)));
                            ListViewAdapter adapter = new ListViewAdapter(context, group_list_aud);
                            listview_aud.setAdapter(adapter);
                            listview_aud.setVisibility(View.VISIBLE);
                            listview_aud.setBackgroundResource(R.drawable.list_view_favorite);
                            break;
                        case (3): // Кнопка 'Настройки'
                            MainActivity.drawerResult.setSelection(0);
                            Intent intent = new Intent(context, Settings_layout.class);
                            act.startActivity(intent);
                            MainActivity.drawerResult.setSelection(0);
                            break;
                        case (4):
                            MainActivity.drawerResult.setSelection(0);
                            act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/krutoypan3/AGPU-RASPISANIE-APK/releases")));
                            MainActivity.drawerResult.setSelection(0);
                            break;
                        case (5): // Если нажали на кнопку удаления
                            CustomAlertDialog cdd = new CustomAlertDialog(act, "delete");
                            cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
                            cdd.show();
                            MainActivity.drawerResult.setSelection(0);
                            break;
                        case (6):
                            MainActivity.drawerResult.setSelection(0);
                            try { // Проверка обновлений
                                new CheckAppUpdate(act, true, true).start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case (7):
                            cdd = new CustomAlertDialog(act, "feedback");
                            cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
                            cdd.show();
                            MainActivity.drawerResult.setSelection(0);
                            break;
                        case (8):
                            cdd = new CustomAlertDialog(act, "about");
                            cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
                            cdd.show();
                            MainActivity.drawerResult.setSelection(0);
                            break;
                    }
                }
            })
            .build();
        ImageView agpu_secret_btn_toolbar = act.findViewById(R.id.agpu_secret_btn_toolbar);
        // Отслеживание нажатий на иконку университета в тулбаре (фича)
        agpu_secret_btn_toolbar.setOnClickListener(v -> {
            agpu_secret_btn_toolbar.startAnimation(MainActivity.animScale);
            Ficha_achievements.put(context, "ficha_toolbar1");
            int random_int = new Random().nextInt(10);
            if (random_int == 0){
                Ficha_achievements.put(context, "ficha_toolbar2");
                AudioManager audioManager = (AudioManager) act.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
                MediaPlayer mp = MediaPlayer.create(act, R.raw.povezlo_povezlo);
                mp.start();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.meme-arsenal.com/memes/995d4522ce4f35fd83cf5cd57dcb32ce.jpg"));
                act.startActivity(intent);
            }
        });
    }
}
