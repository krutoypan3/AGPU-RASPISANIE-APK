package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewAdapter;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems;

public class LoadBuildingsList extends Thread{
    private final Activity act;
    public static List<RecyclerViewItems> buildings_list;

    public final static int BUILDING_MAIN = 0;
    public final static int BUILDING_ZAOCHKA = 1;
    public final static int BUILDING_SPF = 2;
    public final static int BUILDING_FOC = 3;
    public final static int BUILDING_TEHFAK = 4;
    public final static int BUILDING_DORMITORY = 5;
    public final static int BUILDING_ISTFAK = 6;

    /**
     * Подгружает список корпусов
     */
    public LoadBuildingsList(Activity act){
        this.act = act;
    }

    @Override
    public void run(){
        List<RecyclerViewItems> list = new ArrayList<>();

        list.add(new RecyclerViewItems(act.getString(R.string.adress_main), "https://i.ibb.co/M6n1bLj/photo-2022-05-09-17-54-18.jpg", act.getString(R.string.Audiences) +  " : " + act.getString(R.string.adress_main_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_zaochka), "https://i.ibb.co/42sQ0VB/photo-2022-05-09-17-55-37.jpg", act.getString(R.string.Audiences) +  " : " + act.getString(R.string.adress_zaochka_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_spf), "https://i.ibb.co/q56Z5Pv/photo-2022-05-09-17-55-30.jpg", act.getString(R.string.Audiences) +  " : " + act.getString(R.string.adress_spf_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_foc), "https://i.ibb.co/rmTf87S/photo-2022-05-09-17-54-44.jpg", act.getString(R.string.Audiences) +  " : " + act.getString(R.string.adress_foc_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_tehfak), "https://i.ibb.co/ZSVDrLS/photo-2022-05-09-17-55-06.jpg", act.getString(R.string.Audiences) +  " : " + act.getString(R.string.adress_tehfak_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_obshaga), "https://i.ibb.co/xM0mtwV/photo-2022-05-09-17-55-33.jpg", act.getString(R.string.Audiences) +  " : " + act.getString(R.string.adress_obshaga_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_istfak), "https://i.ibb.co/TKzdj75/M3i8-I-sw4-Y4-HAV0-KWJggg1-J1wns17i-S3ul-Kud9a-MVd3-Lgj-LL8y-WEFc3yn-Qx6-XVKd-BZO-BFtn-Ctq87-Vhxq8-Z.jpg", act.getString(R.string.Audiences) +  " : " +  act.getString(R.string.adress_istfak_aud)));

        RecyclerView recyclerView = act.findViewById(R.id.recyclerView);
        // RecyclerView scroll vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        buildings_list = list;
        act.runOnUiThread(() ->{
            recyclerView.setAdapter(new RecyclerViewAdapter(act, buildings_list, RecyclerViewAdapter.IS_BUILDINGS_ADAPTER));
            recyclerView.setLayoutManager(linearLayoutManager);
        });
    }
}
