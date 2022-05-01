package com.example.artikproject.background_work.main_show.ToolBar;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artikproject.R;
import com.example.artikproject.background_work.adapters.RecyclerView.RecyclerViewAdapter;
import com.example.artikproject.background_work.adapters.RecyclerView.RecyclerViewItems;

import java.util.ArrayList;
import java.util.List;

public class LoadBuildingsList extends Thread{
    private final Activity act;

    public LoadBuildingsList(Activity act){
        this.act = act;
    }

    @Override
    public void run(){
        List<RecyclerViewItems> list = new ArrayList<>();

        list.add(new RecyclerViewItems(act.getString(R.string.adress_main), "agpu", act.getString(R.string.adress_main_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_zaochka), "agpu", act.getString(R.string.adress_zaochka_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_spf), "agpu", act.getString(R.string.adress_spf_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_ebd), "agpu", act.getString(R.string.adress_ebd_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_foc), "agpu", act.getString(R.string.adress_foc_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_tehfak), "agpu", act.getString(R.string.adress_tehfak_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_obshaga), "agpu", act.getString(R.string.adress_obshaga_aud)));

        RecyclerView recyclerView = (RecyclerView) act.findViewById(R.id.recyclerView);
        // RecyclerView scroll vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        act.runOnUiThread(() ->{
            recyclerView.setAdapter(new RecyclerViewAdapter(act, list));
            recyclerView.setLayoutManager(linearLayoutManager);
        });
    }
}
