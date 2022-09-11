package ru.oganesyanartem.core.data.repository.current_week_id

import org.jsoup.Jsoup

class CurrentWeekIdGetFromApi {

    fun get(): Int?{
        var newWeekId: Int? = null
        try {
            val urlq = "http://www.it-institut.ru/SearchString/Index/118"
            val doc = Jsoup.connect(urlq).get()
            val todayInfo: String =
                doc.select("div").toString().split("today-info").toTypedArray()[1]
            newWeekId = todayInfo.split("value=\"").toTypedArray()[1].split("\"")
                .toTypedArray()[0].toInt() // Получаем текущую неделю с интернета
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return newWeekId
    }
}