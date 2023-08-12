package ru.agpu.artikproject.background_work.rasp_show

import android.app.Activity
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CustomDialog
import ru.agpu.artikproject.background_work.CustomDialogType
import ru.agpu.artikproject.background_work.GetCorpFromAudNumber
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems
import ru.agpu.artikproject.background_work.datebase.Raspisanie
import ru.agpu.artikproject.background_work.datebase.RaspisanieRepository
import ru.agpu.artikproject.background_work.rasp_show.recycler_view.DayShowRVItems
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements
import ru.agpu.artikproject.presentation.layout.MainActivity

class ParaInfo(position: Int, act: Activity, datas: List<DayShowRVItems>) {

    companion object {
        var finalCorp = 0
    }

    init {
        val paraAdap = datas[position]

        val paraTime = paraAdap.cardParaTime.replace("\n", "-")
        var prepodAud = paraAdap.cardParaPrepod
        val backgroundColor = paraAdap.paraDescriptionLayoutColor
        val backgroundColor2 = paraAdap.paraNumAndTimeLayoutColor

        val shape = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(backgroundColor, backgroundColor2))
        shape.cornerRadius = 50f


        prepodAud = prepodAud.replace(act.getString(R.string.Prepod) + ": ", "")
        val raspisanieRepository = RaspisanieRepository()
        val r = raspisanieRepository.getParaByParams(
            Raspisanie(
                groupCode = MainActivity.selectedItem_id.toIntOrNull(),
                weekNumber = MainActivity.week_id,
                weekDay = MainActivity.week_day,
                paraRazmer = paraTime,
            )
        ).firstOrNull { prepodAud.split(".")[0] in (it.paraPrepod ?: "") }
        if (r == null) {
            Log.e("ParaInfo", "Not find para info")
        } else {
            val groupList = ArrayList<ListViewItems>()
            if (r.paraName != null) {
                r.paraRazmer?.let {
                    groupList.add(
                        ListViewItems("${act.getString(R.string.Time)} : $it")
                    )
                }
                groupList.add(
                    ListViewItems(("${act.getString(R.string.CoupleName)} : ${r.paraName}"))
                )
                r.paraPrepod?.let {
                    groupList.add(
                        ListViewItems(("${act.getString(R.string.Prepod)} : ${it.split(",")[0]}"))
                    )
                }
                r.paraPrepod?.let {
                    groupList.add(
                        ListViewItems(
                            ("${act.getString(R.string.Audience)} : " +
                                    it.split(",").last())
                        )
                    )
                }
                r.paraGroup?.let {
                    groupList.add(
                        ListViewItems(
                            ("${act.getString(R.string.Group)} : " +
                                    it.replace("(", "").replace(")", ""))
                        )
                    )
                }
                r.paraPodgroup?.let {
                    groupList.add(
                        ListViewItems(
                            ("${act.getString(R.string.PodGroup)} : " +
                                    it.replace("(", "").replace(")", ""))
                        )
                    )
                }
                r.paraAud?.let { groupList.add(ListViewItems(it)) }
                r.paraDistant?.let { groupList.add(ListViewItems(r.paraDistant!!)) }
            }
            val adapter = ListViewAdapter(act, groupList)
            try {
                val cdd = CustomDialog(act, CustomDialogType.PARA_INFO)
                cdd.show()
                cdd.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                cdd.findViewById<View>(R.id.scrollViewCustom).background = shape
                cdd.listViewLV?.adapter = adapter
                cdd.listViewLV?.onItemClickListener =
                    OnItemClickListener { _: AdapterView<*>?, _: View?, pos: Int, _: Long ->
                        val sss: String
                        when (pos) {
                            1 -> {
                                sss = (cdd.listViewLV?.getItemAtPosition(pos) as ListViewItems).item
                                if ((sss.contains("практика") ||
                                            sss.contains("экз.") ||
                                            sss.contains("зач.") ||
                                            sss.contains("экзамен") ||
                                            sss.contains("зачет") ||
                                            sss.contains("зачёт"))
                                ) {
                                    FichaAchievements().playFichaGod(act, shape)
                                }
                            }

                            3 -> {
                                var aud: String =
                                    (cdd.listViewLV?.getItemAtPosition(pos) as ListViewItems).item
                                aud = aud.split(": ")[1]
                                finalCorp = GetCorpFromAudNumber().getCorp(act, aud)
                                val dialogConfirm = CustomDialog(act, CustomDialogType.MAP_CONFIRM)
                                dialogConfirm.show()
                                dialogConfirm.window!!
                                    .setBackgroundDrawableResource(android.R.color.transparent)
                                dialogConfirm.findViewById<View>(R.id.scrollViewCustom).background =
                                    shape
                            }

                            2 -> {
                                sss = (cdd.listViewLV?.getItemAtPosition(pos) as ListViewItems).item
                                    .split(",")[0]
                                    .split(": ")[1]

                                // Этот блок кода созда исключительно в развлекательных целях и не несет в себе цель кого-то задеть или обидеть
                                if ((sss == "Лапшин Н.А.")) {
                                    FichaAchievements().playFichaLapshin(act)
                                }
                            }
                        }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}