package com.example.artikproject;

import static com.example.artikproject.MainActivity.sqLiteDatabase;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;

public class settings_layout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Тема приложения из базы данных
        if (savedInstanceState == null) {
            // Set the local night mode to some value
            new ThemeChanger().set();
            // Now recreate for it to take effect
            recreate();
        }
        setContentView(R.layout.settings_layout);

        String[] countries = {"Светлая", "Темная", "Системная"};

        Spinner spinner = findViewById(R.id.theme_spinner);

        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);

        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);
        Cursor r;
        r = sqLiteDatabase.rawQuery("SELECT DISTINCT value FROM settings_app WHERE settings_name = 'theme'", null);
        r.moveToNext();
        int theme_id = r.getInt(0);
        switch (theme_id){
            case(-1):
                spinner.setSelection(2);
                break;
            case(0):
                spinner.setSelection(0);
                break;
            case(1):
                spinner.setSelection(1);
                break;
        }
        // Прослушиваем выбор элемента в выпадающем списке
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);

                // Здесь мы изменяем на то, что нам нужно))
                MainActivity.sqLiteDatabase.execSQL("DELETE FROM settings_app WHERE settings_name = 'theme'");
                switch (item) {
                    case ("Светлая"):
                        MainActivity.sqLiteDatabase.execSQL("INSERT INTO settings_app VALUES('theme', 0)");
                        break;
                    case ("Темная"):
                        MainActivity.sqLiteDatabase.execSQL("INSERT INTO settings_app VALUES('theme', 1)");
                        break;
                    case ("Системная"):
                        MainActivity.sqLiteDatabase.execSQL("INSERT INTO settings_app VALUES('theme', -1)");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

    }
}
