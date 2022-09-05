package ru.agpu.artikproject.data.repository.groups_list

import org.jsoup.Jsoup
import ru.agpu.artikproject.domain.models.GroupsListItem
import java.io.IOException
import java.lang.Exception

class GroupsListGetFromApi {
    fun get(): List<GroupsListItem> {
        val groupsListItems = ArrayList<GroupsListItem>()
        try {
            val urlq = "http://www.it-institut.ru/SearchString/Index/118"
            val doc = try {
                Jsoup.connect(urlq).get()
            } catch (e: IOException) { // Прерывание функции, если нет интернета
                e.printStackTrace()
                return groupsListItems
            }
            val cards = doc.select("body").toString().split("\"card\"").toTypedArray()
            val count = cards.size
            for (i in 1 until count) {
                val facultiesNameButtons = cards[i]
                    .split("</button>")
                    .toTypedArray()[0]
                    .split(">")
                    .toTypedArray()
                val facName =
                    facultiesNameButtons[facultiesNameButtons.size - 1] // Извлекаем названия факультетов
                val facultiesNameClassP2 = cards[i]
                    .split("<div class=\"p-2\">")
                    .toTypedArray()
                val facLength = facultiesNameClassP2.size
                for (j in 1 until facLength) {
                    val facultiesNameClassP2A = facultiesNameClassP2[j]
                        .split("</a>")
                        .toTypedArray()[0]
                        .split(">")
                        .toTypedArray()
                    val facNameLength = facultiesNameClassP2A.size
                    val facultiesGroupNameCurGroup = facultiesNameClassP2A[facNameLength - 1]
                    val facultiesGroupIdCurGroup = facultiesNameClassP2[j]
                        .split("SearchId=")
                        .toTypedArray()[1]
                        .split("&")
                        .toTypedArray()[0]
                    groupsListItems.add(
                        GroupsListItem(
                            facultiesName = facName,
                            groupName = facultiesGroupNameCurGroup,
                            groupId = facultiesGroupIdCurGroup,
                        )
                    )
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return groupsListItems
    }
}