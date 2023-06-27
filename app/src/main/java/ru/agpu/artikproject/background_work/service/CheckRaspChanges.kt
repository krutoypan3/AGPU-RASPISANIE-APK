package ru.agpu.artikproject.background_work.service

import android.content.Context
import ru.agpu.artikproject.background_work.CheckInternetConnection.getState
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite.Companion.getSqliteDatabase
import ru.agpu.artikproject.background_work.site_parse.GetRasp
import ru.oganesyanartem.core.data.repository.current_week_id.CurrentWeekIdImpl
import ru.oganesyanartem.core.domain.usecase.CurrentWeekIdGetUseCase

class CheckRaspChanges(context: Context) {
    init {
        if (getState(context)) {
            val r = getSqliteDatabase(context).rawQuery(
                "SELECT r_group_code, r_selectedItem_type, r_selectedItem FROM rasp_update",
                null
            ) // SELECT запрос
            val rGroup0 = ArrayList<String>()
            val rGroup1 = ArrayList<String>()
            val rGroup2 = ArrayList<String>()
            val weekIdUpd = CurrentWeekIdGetUseCase(CurrentWeekIdImpl(context)).execute()
            r.moveToFirst()
            do {
                if (r.count != 0) {
                    rGroup0.add(r.getString(0))
                    rGroup1.add(r.getString(1))
                    rGroup2.add(r.getString(2))
                }
            } while (r.moveToNext())
            for (i in rGroup0.indices) {
                GetRasp(
                    selectedItemId = rGroup0[i],
                    selectedItemType = rGroup1[i],
                    selectedItem = rGroup2[i],
                    weekIdUpd = weekIdUpd,
                    context = context,
                    type = "CheckRaspChanges"
                ).start()
            }
        }
    }
}