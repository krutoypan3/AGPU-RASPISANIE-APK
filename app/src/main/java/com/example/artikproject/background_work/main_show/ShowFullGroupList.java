package com.example.artikproject.background_work.main_show;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.artikproject.R;
import com.example.artikproject.background_work.CheckInternetConnection;
import com.example.artikproject.background_work.CustomAlertDialog;
import com.example.artikproject.background_work.adapters.ListViewAdapter;
import com.example.artikproject.background_work.site_parse.GetRasp;
import com.example.artikproject.background_work.site_parse.GetFullGroupList_Online;
import com.example.artikproject.layout.MainActivity;
import com.example.artikproject.layout.Raspisanie_show;


public class ShowFullGroupList extends Thread {
    final Activity act;

    /**
     * Показывает список всех факультетов и их групп
     * @param act Активити
     * @param v View
     */
    public ShowFullGroupList(Activity act, View v){
        this.act = act;
        v.startAnimation(MainActivity.animScale);
    }

    @Override
    public void run() {
        try{
            act.runOnUiThread(() -> {
                ListViewAdapter adapter = new ListViewAdapter(act.getApplicationContext(), GetFullGroupList_Online.faculties_name);
                CustomAlertDialog cdd = new CustomAlertDialog(act, "faculties_list");
                cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
                cdd.show();
                cdd.list_view.setAdapter(adapter);
                cdd.list_view.setOnItemClickListener((parent, v, pos, id) ->{
                    CustomAlertDialog cdd2 = new CustomAlertDialog(act, "groups_list");
                    cdd2.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
                    cdd2.show();
                    ListViewAdapter adapter2 = new ListViewAdapter(act.getApplicationContext(), GetFullGroupList_Online.faculties_group_name.get(pos));
                    cdd2.list_view.setAdapter(adapter2);
                    cdd2.list_view.setOnItemClickListener((parent2, v2, pos2, id2) ->{
                        MainActivity.selectedItem = GetFullGroupList_Online.faculties_group_name.get(pos).get(pos2).item;
                        MainActivity.selectedItem_type = "Group";
                        MainActivity.selectedItem_id = GetFullGroupList_Online.faculties_group_id.get(pos).get(pos2).item;
                        Intent intent = new Intent(act.getApplicationContext(), Raspisanie_show.class);
                        if (CheckInternetConnection.getState(act.getApplicationContext())){
                            new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, act.getApplicationContext()).start();
                        }
                        act.startActivity(intent);
                        cdd.dismiss();
                        cdd2.dismiss();
                    });
                });

            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

