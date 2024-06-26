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
import ru.agpu.artikproject.presentation.layout.fragment.ScheduleShowFragment
import java.util.*


class GetRasp(
    private var selectedItemId: String,
    private var selectedItemType: String,
    private var selectedItem: String,
    private var weekIdUpd: Int,
    private var context: Context,
): Thread() {
    private val raspisanieRepository = RaspisanieRepository()

    override fun run() {
        if (!ScheduleShowFragment.refresh_on_off) {
            ScheduleShowFragment.refresh_on_off = true
            Log.i(LOG_TAG, "Был сделан запрос на обновление расписания для $selectedItem")
            val raspisanieList: MutableList<Raspisanie> = mutableListOf()
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
                            var predmetDataNed = days[weekDay][0].split("row\">")[1].split("<br>")[0]
                            var predmetDataChi = days[weekDay][0].split("<br>")[1].split("</th>")[0]

                            if (!dateIsCorrect(predmetDataChi)) {
                                predmetDataNed = ""
                                predmetDataChi = context.getString(R.string.weeks_not_found)
                            }

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

                                raspisanieRepository.deletePara(paraList)
                                raspisanieList.add(raspisanie)

                                if (raspIsChange(paraList.firstOrNull(), raspisanie)) {
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
                            }
                            ScheduleShowFragment.refresh_successful = true
                        }
                    } catch (e: IOException) {
                        // Прерывание функции, если нет интернета
                        Log.e(LOG_TAG, e.message ?: "")
                        ScheduleShowFragment.refresh_on_off = false
                        ScheduleShowFragment.refresh_successful = false
                        return
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                raspisanieRepository.saveRaspisanie(raspisanieList)
                Log.i(LOG_TAG, "Расписание для $selectedItem было обновлено")
                ScheduleShowFragment.refresh_on_off = false
            }
        }
    }

    /**
     * Проверка даты на корректность.
     * Проверяет даты типа 01.01.0001 или 06.01.0001
     */
    private fun dateIsCorrect(date: String?) = date?.split(".")?.last() != "0001"


    /**
     * Проверка изменения в расписании
     */
    private fun raspIsChange(oldRasp: Raspisanie?, newRasp: Raspisanie?): Boolean {
        return (oldRasp?.paraName != newRasp?.paraName ||
            oldRasp?.paraPrepod != newRasp?.paraPrepod ||
            oldRasp?.paraDistant != newRasp?.paraDistant ||
            oldRasp?.paraGroup != newRasp?.paraGroup ||
            oldRasp?.paraPodgroup != newRasp?.paraPodgroup ||
            oldRasp?.paraAud != newRasp?.paraAud ||
            oldRasp?.paraRazmer != newRasp?.paraRazmer
        )

    }

    companion object {
        const val LOG_TAG = "GetRasp"
    }
}