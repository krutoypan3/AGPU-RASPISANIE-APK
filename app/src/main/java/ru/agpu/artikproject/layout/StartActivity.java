package ru.agpu.artikproject.layout;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import ru.agpu.artikproject.background_work.CustomBackground;
import ru.agpu.artikproject.background_work.Starter_MainActivity;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.background_work.theme.Theme;
import ru.agpu.artikproject.R;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.sqLiteDatabase = new DataBase_Local(getApplicationContext()).getWritableDatabase();
        Theme.setting(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        // Установка нового фона [и ТУТ НЕТ затемнителя] | Должно быть после setContentView
        findViewById(R.id.start_activity).setBackground(CustomBackground.getBackground(getApplicationContext()));

        ImageView loading_ico = findViewById(R.id.loading_ico);
        new Starter_MainActivity(this, loading_ico).start();
    }
}
