package ru.agpu.artikproject.background_work.rasp_show

import android.app.Activity
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CustomAlertDialog
import ru.agpu.artikproject.background_work.GetCorpFromAudNumber
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite.Companion.getSqliteDatabase
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
        val r = getSqliteDatabase(act).rawQuery("SELECT * FROM raspisanie WHERE " +
                "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                "r_week_number = " + MainActivity.week_id + " AND " +
                "r_week_day = " + MainActivity.week_day + " AND " +
                "r_razmer = '" + paraTime + "' AND " +
                "r_prepod LIKE '%" + prepodAud.split("\\.")[0] + "%'", null
        )
        if (r.count != 0) {
            r.moveToFirst()
            val groupList = ArrayList<ListViewItems>()
            if (r.getString(4) != null) {
                if (r.getString(9) != null) groupList.add(
                    ListViewItems("${act.applicationContext.resources.getString(R.string.Time)} : ${r.getString(9)}")
                )
                if (r.getString(4) != null) groupList.add(
                    ListViewItems(("${act.applicationContext.resources.getString(R.string.CoupleName)} : ${r.getString(4)}"))
                )
                if (r.getString(5) != null) groupList.add(
                    ListViewItems(("${act.applicationContext.resources.getString(R.string.Prepod)} : ${r.getString(5).split(",")[0]}"))
                )
                if (r.getString(5) != null) groupList.add(
                    ListViewItems(("${act.applicationContext.resources.getString(R.string.Audience)} : ${r.getString(5)
                        .split(",")[r.getString(5).split(",").size - 1]}"))
                )
                if (r.getString(6) != null) groupList.add(
                    ListViewItems(("${act.applicationContext.resources.getString(R.string.Group)} : ${r.getString(6)
                        .replace("(", "").replace(")", "")}"))
                )
                if (r.getString(7) != null) groupList.add(
                    ListViewItems(("${act.applicationContext.resources.getString(R.string.PodGroup)} : ${r.getString(7)
                        .replace("(", "").replace(")", "") }"))
                )
                if (r.getString(8) != null) groupList.add(ListViewItems(r.getString(8)))
                if (r.getString(15) != null) groupList.add(ListViewItems(r.getString(15)))
            }
            val adapter = ListViewAdapter(act.applicationContext, groupList)
            try {
                val cdd = CustomAlertDialog(act, "para_info")
                cdd.show()
                cdd.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                cdd.findViewById<View>(R.id.scrollViewCustom).background = shape
                cdd.list_view.adapter = adapter
                cdd.list_view.onItemClickListener = OnItemClickListener { _: AdapterView<*>?, _: View?, pos: Int, _: Long ->
                    val sss: String
                    when (pos) {
                        1 -> {
                            sss = (cdd.list_view.getItemAtPosition(pos) as ListViewItems).item
                            if ((sss.contains("практика") ||
                                 sss.contains("экз.") ||
                                 sss.contains("зач.") ||
                                 sss.contains("экзамен") ||
                                 sss.contains("зачет") ||
                                 sss.contains("зачёт"))
                            ) { FichaAchievements().playFichaGod(act, shape) }
                        }

                        3 -> {
                            var aud: String = (cdd.list_view.getItemAtPosition(pos) as ListViewItems).item
                            aud = aud.split(": ")[1]
                            finalCorp = GetCorpFromAudNumber().getCorp(act, aud)
                            val dialogConfirm = CustomAlertDialog(act, "map_confirm")
                            dialogConfirm.show()
                            dialogConfirm.window!!
                                .setBackgroundDrawableResource(android.R.color.transparent)
                            dialogConfirm.findViewById<View>(R.id.scrollViewCustom).background = shape
                        }

                        2 -> {
                            sss = (cdd.list_view.getItemAtPosition(pos) as ListViewItems).item
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