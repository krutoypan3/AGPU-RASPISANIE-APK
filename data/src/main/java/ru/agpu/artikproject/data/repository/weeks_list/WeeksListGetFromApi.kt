package ru.agpu.artikproject.data.repository.weeks_list

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import ru.agpu.artikproject.domain.models.WeeksListItem
import java.io.IOException

const val WEEK_LIST_URL = "http://www.it-institut.ru/SearchString/OpenAllWeek?ClientId=118"
const val SEMESTR_ID_URL = "http://www.it-institut.ru/SearchString/ShowTestWeeks?ClientId=118"

class WeeksListGetFromApi {
    fun get(): ArrayList<WeeksListItem> {
        val weeksListItems = ArrayList<WeeksListItem>()
        try {
            var doc: Document = try {
                Jsoup.connect(SEMESTR_ID_URL)
                    .get() // Отправляет запрос для получения Id текущего семестра
            } catch (e: IOException) { // Прерывание функции, если нет интернета
                e.printStackTrace()
                return weeksListItems
            }
            val semestrId: String =
                doc.select("div").toString()
                    .split("<input id=\"ID\" name=\"ID\"")[doc.select("div")
                    .toString()
                    .split("<input id=\"ID\" name=\"ID\"").size - 1]
                    .split("value=\"")[1].split("\"")[0]
            val urlw =
                "$WEEK_LIST_URL&SemestrId=$semestrId"
            doc = try {
                Jsoup.connect(urlw).get()
            } catch (e: IOException) { // Прерывание функции, если нет интернета
                e.printStackTrace()
                return weeksListItems
            }
            val cards: Array<String> = doc.select("div").toString().split("<a").toTypedArray()
            val cardsLength = cards.size
            for (i in 2 until cardsLength) {
                try {
                    val startDate =
                        cards[i].split("<span>").toTypedArray()[2].split("</span>")
                            .toTypedArray()[0] // С какого дня недели
                    val endDate =
                        cards[i].split("<span>").toTypedArray()[3].split("</span>")
                            .toTypedArray()[0] // По какой день недели
                    val id =
                        cards[i].split("WeekId=").toTypedArray()[1].split("\"")
                            .toTypedArray()[0].toInt() // Id недели
                    val weeksListItem = WeeksListItem(
                        weekId = id,
                        startDate = startDate,
                        endDate = endDate
                    )
                    weeksListItems.add(weeksListItem)
                } catch (ignored: Exception) {
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return weeksListItems
    }
}