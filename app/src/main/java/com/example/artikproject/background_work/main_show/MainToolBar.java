package com.example.artikproject.background_work.main_show;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.artikproject.R;
import com.example.artikproject.background_work.CheckAppUpdate;
import com.example.artikproject.background_work.ShowNotification;
import com.example.artikproject.background_work.debug.Device_info;
import com.example.artikproject.background_work.debug.SendInfoToServer;
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
import java.util.List;


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
                    new SecondaryDrawerItem().withName("Версия: " + Device_info.getAppVersion(context)).setEnabled(false)
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
                            MainActivity.subtitle.setVisibility(View.INVISIBLE);
                            List<String> group_list_aud = new ArrayList<>();
                            group_list_aud.add("Главный корпус  по ул. Р. Люксембург, 159\n" +
                                    "Аудитории: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 14а," +
                                    " 15, 15а, 16, 17, 18,  21, 22, 23\n");
                            group_list_aud.add("Корпус по ул. Кирова 50 (Заочка)\n" +
                                    "Аудитории: 24, 25, 26, 27, 28\n");
                            group_list_aud.add("Корпус по ул. Ленина, 79  (СПФ)\n" +
                                    "Аудитории: 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50\n");
                            group_list_aud.add("Корпус по ул. Ефремова, 35 (ЕБД)\n" +
                                    "Аудитории: 80, 81, 82, 82а, 83, 84\n");
                            group_list_aud.add("Корпус по ул. П. Осипенко, 83 (ФОК)\n" +
                                    "Аудитории: 85, 85а, 86\n");
                            group_list_aud.add("Корпус по ул. П. Комсомольская, 93 (ФТЭиД\\ТЕХФАК)\n" +
                                    "Аудитории: 51, 52, 53, 57, 58 а, 58 б, 59, 60, 61, 62, 63, 64," +
                                    " 65, 66, 67, 68\n");
                            group_list_aud.add("Корпус по ул. К. Маркса, 49 (Общежитие 1)\n" +
                                    "Аудитории: 30, 31, 32, 33, 34, 35, 36, 37, 38, ЛК-1 – ЛК-6, 101," +
                                    " 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113," +
                                    " 114, 115, 116, 117, 118, 119, 120, 121");
                            String[] group_listed_aud;
                            group_listed_aud = group_list_aud.toArray(new String[0]);
                            ArrayAdapter<String> adapter = new ArrayAdapter(context, R.layout.listviewadapterbl, group_listed_aud);
                            MainActivity.listview_aud.setAdapter(adapter);
                            MainActivity.listview_aud.setVisibility(View.VISIBLE);
                            MainActivity.listview_aud.setBackgroundResource(R.drawable.list_view_favorite);
                            break;
                        case (3): // Кнопка 'Настройки'
                            MainActivity.drawerResult.setSelection(0);
                            Intent intent = new Intent(context, Settings_layout.class);
                            act.startActivity(intent);
                            MainActivity.drawerResult.setSelection(0);
                            new ShowNotification(context, "Вопросы?", "Напиши мне в ВК:\nАртем Оганесян\nvk.com/aom13").start();
                            break;
                        case (4):
                            MainActivity.drawerResult.setSelection(0);
                            act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/krutoypan3/AGPU-RASPISANIE-APK/releases")));
                            MainActivity.drawerResult.setSelection(0);
                            break;
                        case (5): // Если нажали на кнопку удаления
                            MainActivity.drawerResult.setSelection(0);
                            AlertDialog.Builder builder = new AlertDialog.Builder(act);
                            builder.setTitle("Удалить все сохраненные расписания?!")
                                    .setMessage("Подтвердите удаление!")
                                    .setCancelable(false)
                                    .setPositiveButton("Отмена", (dialog, which) -> dialog.cancel())
                                    .setNegativeButton("Удалить все!", (dialog, which) -> {
                                        MainActivity.sqLiteDatabase.execSQL("DELETE FROM rasp_test1");
                                        MainActivity.sqLiteDatabase.execSQL("DELETE FROM rasp_update");
                                        MainActivity.group_listed = null;
                                        new WatchSaveGroupRasp(context);
                                        dialog.cancel();
                                    });
                            AlertDialog Error = builder.create();
                            Error.show();
                            MainActivity.listview.setAdapter(null);
                            MainActivity.drawerResult.setSelection(0);
                            break;
                        case (6):
                            MainActivity.drawerResult.setSelection(0);
                            try { // Проверка обновлений
                                new CheckAppUpdate(context);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case (7):
                            MainActivity.drawerResult.setSelection(0);
                            builder = new AlertDialog.Builder(act);
                            final EditText input = new EditText(context);
                            builder.setView(input);
                            builder.setTitle("О чем вы хотите сообщить?")
                                    .setMessage("Опишите ситуацию поподробнее")
                                    .setCancelable(true)

                                    .setNegativeButton("Отмена", (dialog, which) -> dialog.cancel())
                                    .setPositiveButton("Отправить отзыв", (dialog, whichButton) -> {
                                        String value = String.valueOf(input.getText());
                                        Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
                                        new SendInfoToServer(context, value);
                                    });
                            Error = builder.create();
                            Error.show();
                            MainActivity.listview.setAdapter(null);
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
        MainActivity.subtitle.setVisibility(View.VISIBLE);
    }
}
