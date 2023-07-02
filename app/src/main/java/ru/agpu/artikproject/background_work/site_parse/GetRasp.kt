package ru.agpu.artikproject.background_work.site_parse

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import io.realm.internal.IOException
import org.jsoup.Jsoup
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.ShowNotification
import ru.agpu.artikproject.background_work.datebase.Raspisanie
import ru.agpu.artikproject.background_work.datebase.RaspisanieRepository
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow
import java.util.*


class GetRasp(
    private var selectedItemId: String,
    private var selectedItemType: String,
    private var selectedItem: String,
    private var weekIdUpd: Int,
    private var context: Context,
    private var type: String? = null,
): Thread() {
    private val raspisanieRepository = RaspisanieRepository()

    override fun run() {
        if (!FragmentScheduleShow.refresh_on_off) {
            FragmentScheduleShow.refresh_on_off = true
            println("Был сделан запрос на обновление расписания для $selectedItem")
            try {
                    for (weekNumberOffset in -1..1) {
                        val url = "https://www.it-institut.ru/Raspisanie/SearchedRaspisanie?OwnerId=118&SearchId=$selectedItemId&SearchString=$selectedItem&Type=$selectedItemType&WeekId=${weekIdUpd + weekNumberOffset}"
                        try {
                            val doc = Jsoup.connect(url).get()
                            val days = mutableListOf<List<String>>()
                            for (i in 1..6) {
                                days.add(doc.select("tbody").toString().split("th scope")[i].split("td colspan="))
                            }
                            val dayDataRazmer = mutableListOf<String>()
                            val dayDataTime = mutableListOf<String>()
                            val dataaa = doc.select("thead").toString().split("colspan=\"")

                            for (i in 1..7) {
                                dayDataRazmer.add(dataaa[i].split("\"")[0])
                                dayDataTime.add(dataaa[i].split(">")[2].split("<")[0])
                            }

                            for (weekDay in 0..5) {
                                var prohod = false
                                var schet = 0
                                val predmetDataNed = days[weekDay][0].split("row\">")[1].split("<br>")[0]
                                val predmetDataChi = days[weekDay][0].split("<br>")[1].split("</th>")[0]

                                for (paraNumber in 0 until 9) {
                                    val para = days[weekDay][paraNumber]
                                    val predmetName = try { para.split("<span>")[1].split("</span>")[0] } catch (_: Exception) { null }
                                    val predmetPrepod = try { para.split("<span>")[2].split("</span>")[0] } catch (_: Exception) { null }
                                    val predmetGroup = try { para.split("<span>")[3].split("</span>")[0] } catch (_: Exception) { null }
                                    val predmetPodgroup = try { para.split("<span>")[4].split("</span>")[0] } catch (_: Exception) { null }
                                    val predmetAud: String? = null // TODO та переменная не юзается т.к. аудитория приходит вместе с преподом (вроде как)
                                    val predmetRazmer = try { para.split("\"")[1] } catch (_: Exception) { null }
                                    val predmetColor = try { para.split("style=\"background-color:")[1].split("\">")[0] } catch (_: Exception) { null }
                                    val predmetDistant = try { para.split("</div>")[2].split("</td>")[0] } catch (_: Exception) { null }

                                    var predmetTime: String? = null
                                    if ((paraNumber > 0) && (schet < 7)) {
                                        predmetTime = dayDataTime[schet]
                                        if ((dayDataRazmer[schet]) == predmetRazmer) {
                                            if (prohod) {
                                                prohod = false
                                            } else {
                                                schet++
                                            }
                                        } else {
                                            if (!prohod) {
                                                prohod = true
                                            } else {
                                                schet++
                                                prohod = false
                                            }
                                        }
                                    }
                                     val raspisanie = Raspisanie(
                                         groupCode = selectedItemId.toInt(),
                                         weekDay = weekDay,
                                         weekNumber = weekIdUpd + weekNumberOffset,
                                         paraNumber = paraNumber,
                                         paraName = predmetName,
                                         paraPrepod = predmetPrepod,
                                         paraGroup = predmetGroup,
                                         paraPodgroup= predmetPodgroup,
                                         paraAud = predmetAud,
                                         paraRazmer = predmetTime,
                                         weekDayName = predmetDataNed,
                                         weekDayDate = predmetDataChi,
                                         searchType = selectedItemType,
                                         lastUpdate = Date().time,
                                         paraColor = predmetColor,
                                         paraDistant = predmetDistant,
                                    )

                                    val paraList = raspisanieRepository.getParaByParams(Raspisanie(
                                        groupCode = selectedItemId.toInt(),
                                        weekNumber = weekIdUpd + weekNumberOffset,
                                        weekDay = weekDay,
                                        paraNumber = paraNumber,
                                        searchType = selectedItemType,
                                    ))


                                    if (paraList.isEmpty()) {
                                        raspisanieRepository.saveRaspisanie(raspisanie)
                                    } else {
                                        if (predmetName == paraList.first().paraName &&
                                            predmetPrepod == paraList.first().paraPrepod &&
                                            predmetDistant == paraList.first().paraDistant &&
                                            predmetGroup == paraList.first().paraGroup &&
                                            predmetPodgroup == paraList.first().paraPodgroup &&
                                            predmetAud == paraList.first().paraAud &&
                                            predmetTime == paraList.first().paraRazmer
                                        ) {
                                            raspisanieRepository.deletePara(paraList)
                                            raspisanieRepository.saveRaspisanie(raspisanie)
                                            // Это нужно для вызова вне основного потока
                                            Handler(Looper.getMainLooper()).post { // Выводим уведомление о наличии нового расписания
                                                ShowNotification(
                                                    context,
                                                    "$selectedItem ${context.resources.getString(R.string.new_rasp)}!",
                                                    "$predmetDataNed $predmetDataChi. ${context.resources.getString(R.string.new_rasp_sub)}",
                                                    selectedItemId.toInt()
                                                ).start()
                                            }
                                        }
                                        if (type == TYPE_WIDGET) {
                                            raspisanieRepository.deletePara(paraList)
                                            raspisanieRepository.saveRaspisanie(raspisanie)
                                        }
                                    }
                                }
                                FragmentScheduleShow.refresh_successful = true
                            }
                        } catch (e: IOException) {
                            // Прерывание функции, если нет интернета
                            Log.e("GetRaspK run", e.message ?: "")
                            FragmentScheduleShow.refresh_on_off = false
                            FragmentScheduleShow.refresh_successful = false
                            return
                        }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                println("Расписание для $selectedItem было обновлено")
                FragmentScheduleShow.refresh_on_off = false
            }
        }
    }

    companion object {
        const val TYPE_WIDGET = "widget"
    }
}