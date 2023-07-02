package ru.agpu.artikproject.background_work.service

import android.content.Context
import ru.agpu.artikproject.background_work.CheckInternetConnection.getState
import ru.agpu.artikproject.background_work.datebase.RaspUpdateRepository
import ru.agpu.artikproject.background_work.site_parse.GetRasp
import ru.oganesyanartem.core.data.repository.current_week_id.CurrentWeekIdImpl
import ru.oganesyanartem.core.domain.usecase.CurrentWeekIdGetUseCase

class CheckRaspChanges(context: Context) {
    init {
        if (getState(context)) {
            val raspUpdates = RaspUpdateRepository().getAll()
            val weekIdUpd = CurrentWeekIdGetUseCase(CurrentWeekIdImpl(context)).execute()
            raspUpdates.forEach {
                GetRasp(
                    selectedItemId = it.groupCode.toString(),
                    selectedItemType = it.searchType ?: "",
                    selectedItem = it.paraName ?: "",
                    weekIdUpd = weekIdUpd,
                    context = context,
                    type = "CheckRaspChanges"
                ).start()
            }
        }
    }
}