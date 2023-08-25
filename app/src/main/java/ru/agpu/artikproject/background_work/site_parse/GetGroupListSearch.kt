package ru.agpu.artikproject.background_work.site_parse

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONException
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems
import ru.agpu.artikproject.background_work.datebase.AppData.Groups.groupListed
import ru.agpu.artikproject.background_work.datebase.AppData.Groups.groupListedId
import ru.agpu.artikproject.background_work.datebase.AppData.Groups.groupListedType
import ru.agpu.artikproject.presentation.layout.MainActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Класс отвечающий за поиск группы, аудитории, преподователя
 * @param urlQ Ссылка на сайт
 * @param act Активити
 */
class GetGroupListSearch(private val urlQ: String, private val act: Activity): Thread() {
    override fun run() {
        val result = act.findViewById<TextView>(R.id.result)
        var connection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        try {
            val url = URL(urlQ)
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            try {
                connection.connect()
            } catch (e: Exception) {
                // Это нужно для вызова вне основного потока
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        act.applicationContext,
                        R.string.Internet_error,
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
            val stream = connection.inputStream
            reader = BufferedReader(InputStreamReader(stream))
            val buffer = reader.readLine()
            val obj = JSONArray(buffer)
            val groupList = ArrayList<ListViewItems>()
            val groupListType: MutableList<String> = ArrayList()
            val groupListId: MutableList<String> = ArrayList()
            for (ii in 0 until obj.length()) {
                val guysJSON = obj.getJSONObject(ii)
                val keys: Iterator<*> = guysJSON.keys()
                var i = 0
                while (keys.hasNext()) {
                    val value = guysJSON[keys.next() as String]
                    if (i % 4 == 0) {
                        val groupName = value as String
                        groupList.add(ListViewItems(groupName))
                    } else if (i % 4 == 1) {
                        val groupType = value as String
                        groupListType.add(groupType)
                    } else if (i % 4 == 2) {
                        val groupId = value.toString()
                        groupListId.add(groupId)
                    }
                    i++
                }
            }
            groupListed = groupList
            groupListedType = groupListType.toTypedArray()
            groupListedId = groupListId.toTypedArray()
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            connection?.disconnect()
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        // Это нужно для вызова вне основного потока
        Handler(Looper.getMainLooper()).post {
            val listview = act.findViewById<ListView>(R.id.listview)
            if (listview != null) {
                val adapter = ListViewAdapter(act.applicationContext, groupListed ?: emptyList())
                listview.adapter = adapter
                result.text = ""
                listview.visibility = View.VISIBLE
            }
        }
    }
}