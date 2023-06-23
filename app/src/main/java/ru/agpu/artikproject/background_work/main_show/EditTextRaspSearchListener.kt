package ru.agpu.artikproject.background_work.main_show

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import ru.agpu.artikproject.background_work.CheckInternetConnection
import ru.agpu.artikproject.background_work.site_parse.GetGroupListSearch

/**
 * Слушатель изменения текстового поля
 * @param editText Текстовое поле
 * @param act Активити
 */
class EditTextRaspSearchListener(act: Activity, editText: EditText) {
    init {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {} // До изменения поля
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {} // После изменения поля
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { // Во время изменения поля
                if (editText.text.toString().trim() != "") { // Если строка поиска не пустая
                    if (!CheckInternetConnection.getState(act.applicationContext)) {
                        WatchSaveGroupRasp(act)
                    } // Если нет доступа к интернету то выводить список из бд
                    else {
                        val urlq = "https://www.it-institut.ru/SearchString/KeySearch?Id=118&SearchProductName=" + editText.text.toString()
                        GetGroupListSearch(urlq, act).start() // Отправляем запрос на сервер и выводим получившийся список
                    }
                    if (editText.text.toString().equals("рикардо", ignoreCase = true)) {
                        FichaRicardo(act)
                    }
                } else {
                    WatchSaveGroupRasp(act)
                }
            }
        })
    }
}
