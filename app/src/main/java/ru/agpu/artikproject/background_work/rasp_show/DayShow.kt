package ru.agpu.artikproject.background_work.rasp_show

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CheckInternetConnection
import ru.agpu.artikproject.background_work.datebase.RaspisanieRepository
import ru.agpu.artikproject.background_work.rasp_show.recycler_view.DayShowRVAdapter
import ru.agpu.artikproject.background_work.rasp_show.recycler_view.DayShowRVItems
import ru.agpu.artikproject.background_work.site_parse.GetRasp
import ru.agpu.artikproject.background_work.theme.ColorChanger
import ru.agpu.artikproject.background_work.theme.Theme
import ru.agpu.artikproject.presentation.layout.MainActivity


/**
 * Класс отвечающий за показ расписания в дневном режиме
 * @param view View фрагмента приложения
 */
class DayShow(view: View) {
    init {
        var mainText = ""
        val list = mutableListOf<DayShowRVItems>()

        val observable: Observable<String> = Observable.create { subscriber -> // Создаем observable, который будет выполняться в отдельном потоке

        val raspisanieList = RaspisanieRepository().getByGroupCodeAndWeekNumberAndWeekDay(
            MainActivity.selectedItemId?.toInt(), MainActivity.weekId, MainActivity.weekDay
        )
        if (raspisanieList.isNotEmpty()) {
            var prevTime = ""
            var prevNumber = 0
            mainText = "${raspisanieList.first().weekDayName} ${raspisanieList.first().weekDayDate}"
            raspisanieList.forEach { raspisanie ->
                if (raspisanie.paraName != null) {
                    val paraNumber: Int
                    val paraTime: String
                    if (prevTime == raspisanie.paraRazmer) {
                        paraNumber = prevNumber
                        paraTime = prevTime.replace("-", "\n")
                    } else {
                        prevNumber += 1
                        paraNumber = prevNumber
                        paraTime = raspisanie.paraRazmer?.replace("-", "\n") ?: ""
                        prevTime = raspisanie.paraRazmer ?: ""
                    }
                    var paraPrepod = raspisanie.paraPrepod ?: ""
                    var paraAud = raspisanie.paraAud ?: ""
                    if (paraAud == "" && paraPrepod.contains(",")) {
                        paraAud = paraPrepod.split(",").last()
                    }
                    if (paraAud != "") {
                        paraAud = "${view.context.getString(R.string.Audience)}: $paraAud"
                    }
                    if (paraPrepod.contains(",")) {
                        val prepodSplited = paraPrepod.split(",")
                        var newParaPrepod = prepodSplited[0]
                        for (i in 1 until prepodSplited.size) {
                            newParaPrepod = "${newParaPrepod}${prepodSplited[i]}"
                        }
                        paraPrepod = "${view.context.getString(R.string.Prepod)}: $newParaPrepod"
                    }
                    var paraGroup = raspisanie.paraGroup ?: ""
                    if (paraGroup != "") {
                        paraGroup = "${view.context.getString(R.string.Group)}: $paraGroup"
                    }

                    val paraColor = raspisanie.paraColor?.let {Color.parseColor(it)} ?: Color.LTGRAY

                    val raspColorRight: Int
                    val raspColorLeft: Int
                    if (Theme.getApplicationTheme(view.context) == AppCompatDelegate.MODE_NIGHT_YES) {
                        raspColorRight = ColorChanger.getDarkColor(paraColor, 120)
                        raspColorLeft = ColorChanger.getLightColor(raspColorRight, 30)
                    } else {
                        raspColorRight = ColorChanger.getLightColor(paraColor, 30)
                        raspColorLeft = paraColor
                    }

                    list.add(
                        DayShowRVItems(
                            paraNumber.toString(), paraTime, raspisanie.paraName ?: "",
                            "$paraAud ${raspisanie.paraDistant ?: ""}\n$paraGroup ${raspisanie.paraPodgroup ?: ""}",
                            paraPrepod, raspColorLeft, raspColorRight
                        )
                    )
                }
            }
        } else {
            if (CheckInternetConnection.getState(view.context)) {
                GetRasp(
                    MainActivity.selectedItemId ?: "",
                    MainActivity.selectedItemType ?: "",
                    MainActivity.selectedItem ?: "",
                    MainActivity.weekId,
                    view.context,
                    null
                )
            }
        }
            subscriber.onNext("")
        }

        var observ: Disposable? = null
        observ = observable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                try {
                    if (view.isShown) {
                        val mainTextTV = view.findViewById<TextView>(R.id.main_text)
                        mainTextTV.text = mainText

                        val recyclerView = view.findViewById<RecyclerView>(R.id.day_para_view_rec)
                        recyclerView.layoutManager = LinearLayoutManager(view.context)
                        recyclerView.adapter =
                            DayShowRVAdapter(
                                view.context as Activity,
                                list
                            )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    observ?.dispose()
                }
            }
    }
}