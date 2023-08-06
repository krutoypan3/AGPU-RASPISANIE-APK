package ru.agpu.artikproject.background_work

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager.Request
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.browser.trusted.sharing.ShareTarget.RequestMethod
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.debug.DeviceInfo.getAppVersion
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Класс отвечающий за поиск обновлений приложения
 * @param act Контекст приложения
 * @param showNoUpdateMessage Показать уведомление об отсутствии обновлений (True / False)
 */
class CheckAppUpdate(private val act: Activity, private val showNoUpdateMessage: Boolean) : Thread() {
    @SuppressLint("InflateParams")
    override fun run() {
        try {
            val url = URL(URL_UPDATE_CHECK)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
            var inputLine: String?
            val response = StringBuilder()
            while (bufferedReader.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            bufferedReader.close()
            val ass = response.toString()
            // Считываем json
            val obj = JSONParser().parse(ass)
            val jo = obj as JSONObject
            val newVersion = jo["tag_name"] as String
            val currentVersion = getAppVersion(act.applicationContext)
            if (newVersion != currentVersion) { // Если версия приложения отличается от версии приложения на сервере
                act.runOnUiThread {
                    val updateBtn = act.findViewById<ImageView>(R.id.update_btn)
                    updateBtn.visibility = View.VISIBLE
                    updateBtn.animation = AnimationUtils.loadAnimation(
                        act.applicationContext,
                        R.anim.slow_scale_long
                    )
                    updateBtn.setOnClickListener { showUpdateMessage() }
                }
                isAppHaveUpdate = true
                whatsNew = jo["body"] as String?
                val assets = (jo["assets"] as JSONArray?)!!
                val assets0 = assets[0] as JSONObject
                urlDownload = assets0["browser_download_url"] as String?
            } else if (showNoUpdateMessage) { // Если версии одинаковые
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(act, R.string.no_new_version, Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showUpdateMessage() {
        val cdd = CustomAlertDialog(
            act, act.resources.getString(R.string.new_app_version), whatsNew, Uri.parse(urlDownload)
        )
        cdd.window!!.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
        cdd.show()
    }

    companion object {
        var isAppHaveUpdate = false
        var whatsNew: String? = null
        var urlDownload: String? = null

        const val URL_UPDATE_CHECK = "https://api.github.com/repos/krutoypan3/AGPU-RASPISANIE-APK/releases/latest"
    }
}
