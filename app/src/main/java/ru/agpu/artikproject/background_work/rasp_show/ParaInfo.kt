package ru.agpu.artikproject.background_work.rasp_show

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import com.bumptech.glide.Glide
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CustomAlertDialog
import ru.agpu.artikproject.background_work.GetCorpFromAudNumber
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite.Companion.getSqliteDatabase
import ru.agpu.artikproject.background_work.rasp_show.recycler_view.DayShowRVItems
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements.Companion.put
import ru.agpu.artikproject.presentation.layout.MainActivity
import java.util.Random

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
                            ) {
                                put(act.applicationContext, "ficha_god")
                                val cdd2 = CustomAlertDialog(act, "para_pasha")
                                cdd2.show()
                                cdd2.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                                cdd2.findViewById<View>(R.id.scrollViewCustom).background = shape
                                val molitva = ArrayList<ListViewItems>()
                                molitva.add(ListViewItems("Да восвятится имя твое"))
                                molitva.add(ListViewItems("Да покаешься ты в грехах своих"))
                                molitva.add(ListViewItems("Да закроешь ты сессию эту"))
                                val adapter2 = ListViewAdapter(act.applicationContext, molitva)
                                cdd2.list_view.adapter = adapter2
                                Glide.with(act).load("https://i.ibb.co/4pqtKcY/ficha-god.png")
                                    .into(cdd2.para_info_photo)
                                val audioManager = act.applicationContext
                                    .getSystemService(Context.AUDIO_SERVICE) as AudioManager
                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
                                val mp = MediaPlayer.create(act, R.raw.ficha_god)
                                mp.start()
                            }
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
                                if (Random().nextInt(5) == 0) { put(act.applicationContext, "ficha_para_lapshin")
                                    val audioManager: AudioManager = act.applicationContext
                                        .getSystemService(Context.AUDIO_SERVICE) as AudioManager
                                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
                                    val mp = MediaPlayer.create(act, R.raw.povezlo_povezlo)
                                    mp.start()
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://i.ibb.co/jLRpZ1B/2022-04-29-203438.png"))
                                    act.startActivity(intent)
                                }
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