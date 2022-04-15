package com.example.artikproject.background_work.main_show;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;

import com.example.artikproject.R;
import com.example.artikproject.background_work.CheckAppUpdate;
import com.example.artikproject.background_work.adapters.ListViewAdapter;
import com.example.artikproject.background_work.adapters.ListViewItems;
import com.example.artikproject.background_work.ShowNotification;
import com.example.artikproject.background_work.CustomAlertDialog;
import com.example.artikproject.background_work.debug.Device_info;
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


public class MainToolBar {
    /**
     * Класс отвечающий за создание и наполнение тулбара главного меню
     * @param act Activity главного меню
     */
    public MainToolBar(Activity act) throws PackageManager.NameNotFoundException {
        Context context = act.getApplicationContext();
        MainActivity.drawerResult = new Drawer()
            .withActivity(act)  // В каком активити создать тулбар
            .withToolbar(MainActivity.toolbar)  // Выбираем сам тулбар
            .withActionBarDrawerToggle(true)
            .withHeader(R.layout.drawer_header) // С заголовком88
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
                    new SecondaryDrawerItem().withName(context.getResources().getString(R.string.version) + ": " + Device_info.getAppVersion(context)).setEnabled(false)
            )
            .withOnDrawerItemClickListener((parent, view, position, id, drawerItem) -> {
                if (drawerItem instanceof Nameable) {
                    switch (drawerItem.getIdentifier()) {
                        case (1):
                            show_group_list();
                            break;
                        case (2):
                            MainActivity.listview.setVisibility(View.INVISIBLE);
                            MainActivity.result.setVisibility(View.INVISIBLE);
                            MainActivity.rasp_search_edit.setVisibility(View.INVISIBLE);
                            MainActivity.listview_aud.setVisibility(View.VISIBLE);
                            MainActivity.list_groups.setVisibility(View.INVISIBLE);
                            MainActivity.list_weeks.setVisibility(View.INVISIBLE);
                            MainActivity.today.setVisibility(View.INVISIBLE);
                            MainActivity.current_week.setVisibility(View.INVISIBLE);
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
                            MainActivity.listview_aud.setAdapter(adapter);
                            MainActivity.listview_aud.setVisibility(View.VISIBLE);
                            MainActivity.listview_aud.setBackgroundResource(R.drawable.list_view_favorite);
                            break;
                        case (3): // Кнопка 'Настройки'
                            MainActivity.drawerResult.setSelection(0);
                            Intent intent = new Intent(context, Settings_layout.class);
                            act.startActivity(intent);
                            MainActivity.drawerResult.setSelection(0);
                            new ShowNotification(context, context.getResources().getString(R.string.Questions), context.getResources().getString(R.string.Questions_sub)).start();
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
                                new CheckAppUpdate(act, true).start();
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
                    }
                }
            })
            .build();
    }

    public static void show_group_list(){
        MainActivity.drawerResult.setSelection(0);
        MainActivity.listview.setVisibility(View.VISIBLE);
        MainActivity.result.setVisibility(View.VISIBLE);
        MainActivity.rasp_search_edit.setVisibility(View.VISIBLE);
        MainActivity.listview_aud.setVisibility(View.INVISIBLE);
        MainActivity.today.setVisibility(View.VISIBLE);
        MainActivity.current_week.setVisibility(View.VISIBLE);
        MainActivity.list_groups.setVisibility(View.VISIBLE);
        MainActivity.list_weeks.setVisibility(View.VISIBLE);
    }
}
