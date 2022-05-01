package com.example.artikproject.background_work.main_show;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.artikproject.background_work.CheckInternetConnection;
import com.example.artikproject.background_work.site_parse.GetGroupList_Search;

public class EditTextRaspSearch_Listener {
    /**
     * Слушатель изменения текстового поля
     * @param editText Текстовое поле
     * @param act Активити
     */
    public EditTextRaspSearch_Listener(Activity act, EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {} // До изменения поля
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {} // После изменения поля
            public void onTextChanged(CharSequence s, int start, int before, int count) { // Во время изменения поля
                if (!(editText.getText().toString().trim().equals(""))) { // Если строка поиска не пустая
                    if (!CheckInternetConnection.getState(act.getApplicationContext())){ new WatchSaveGroupRasp(act); } // Если нет доступа к интернету то выводить список из бд
                    else {
                        String urlq = "https://www.it-institut.ru/SearchString/KeySearch?Id=118&SearchProductName=" + editText.getText().toString();
                        new GetGroupList_Search(urlq, act).start(); // Отправляем запрос на сервер и выводим получившийся список
                    }
                }
                else{new WatchSaveGroupRasp(act);}
            }
        });
    }
}
