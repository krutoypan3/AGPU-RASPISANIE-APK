package ru.agpu.artikproject.background_work.main_show.buildings;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.RecyclerView.RecyclerViewAdapter;
import ru.agpu.artikproject.background_work.adapters.RecyclerView.RecyclerViewItems;

public class LoadBuildingsList extends Thread{
    private final Activity act;

    public LoadBuildingsList(Activity act){
        this.act = act;
    }

    @Override
    public void run(){
        List<RecyclerViewItems> list = new ArrayList<>();

        list.add(new RecyclerViewItems(act.getString(R.string.adress_main), R.drawable.agpu_ico, act.getString(R.string.adress_main_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_zaochka), R.drawable.agpu_ico, act.getString(R.string.adress_zaochka_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_spf), R.drawable.agpu_ico, act.getString(R.string.adress_spf_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_ebd), R.drawable.agpu_ico, act.getString(R.string.adress_ebd_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_foc), R.drawable.agpu_ico, act.getString(R.string.adress_foc_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_tehfak), R.drawable.agpu_ico, act.getString(R.string.adress_tehfak_aud)));
        list.add(new RecyclerViewItems(act.getString(R.string.adress_obshaga), R.drawable.agpu_ico, act.getString(R.string.adress_obshaga_aud)));

        RecyclerView recyclerView = act.findViewById(R.id.recyclerView);
        // RecyclerView scroll vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        act.runOnUiThread(() ->{
            recyclerView.setAdapter(new RecyclerViewAdapter(act, list));
            recyclerView.setLayoutManager(linearLayoutManager);
        });
    }
}
