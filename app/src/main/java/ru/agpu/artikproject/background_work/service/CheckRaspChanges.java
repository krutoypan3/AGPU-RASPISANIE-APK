package ru.agpu.artikproject.background_work.service;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import ru.agpu.artikproject.background_work.CheckInternetConnection;
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite;
import ru.agpu.artikproject.background_work.site_parse.GetRasp;
import ru.oganesyanartem.core.data.repository.current_week_id.CurrentWeekIdImpl;
import ru.oganesyanartem.core.domain.usecase.CurrentWeekIdGetUseCase;

public class CheckRaspChanges {
    public CheckRaspChanges(Context context) {
        if (CheckInternetConnection.INSTANCE.getState(context)) {
            Cursor r = DataBaseSqlite.Companion.getSqliteDatabase(context).rawQuery("SELECT r_group_code, r_selectedItem_type, r_selectedItem FROM rasp_update", null); // SELECT запрос
            ArrayList<String> r_group0 = new ArrayList<>();
            ArrayList<String> r_group1 = new ArrayList<>();
            ArrayList<String> r_group2 = new ArrayList<>();

            int week_id_upd = new CurrentWeekIdGetUseCase(new CurrentWeekIdImpl(context)).execute();
            r.moveToFirst();
            do {
                if (r.getCount() != 0) {
                    r_group0.add(r.getString(0));
                    r_group1.add(r.getString(1));
                    r_group2.add(r.getString(2));
                }
            } while (r.moveToNext());

            for (int i = 0; i < r_group0.size(); i++) {
                new GetRasp(r_group0.get(i), r_group1.get(i), r_group2.get(i), week_id_upd, context, "CheckRaspChanges").start();
            }
    }

    }
}
