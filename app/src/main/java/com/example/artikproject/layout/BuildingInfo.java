package com.example.artikproject.layout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.artikproject.R;
import com.example.artikproject.background_work.CustomBackground;
import com.example.artikproject.background_work.main_show.ListViewAud_ClickListener;

/**
 * Данное активити отвечает за отображение подробной информации о корпусах и аудиториях
 */
public class BuildingInfo extends AppCompatActivity {

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        findViewById(R.id.btn_building_info).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building_info);
        // Это нужно для предотвращения мерцания при анимации
        getWindow().setEnterTransition(null);

        // Установка нового фона и затемнителя | Должно быть после setContentView
        RelativeLayout layout = findViewById(R.id.building_info_layout);
        layout.setBackground(CustomBackground.getBackground(getApplicationContext()));
        findViewById(R.id.cardBackgroundDarker).setBackgroundColor(CustomBackground.getBackgroundDarker(getApplicationContext()));

        // Настраиваем основной текст
        String mainText = getIntent().getStringExtra("mainText"); // Получаем переданное название корпуса
        TextView mainTextView = findViewById(R.id.cardViewAudMainText_second); // Находим TextEdit на слое
        mainTextView.setText(mainText); // Устанавливаем наш mainText

        // Настраиваем дополнительный текст
        String subText = getString(R.string.Audiences) + " : " + getIntent().getStringExtra("subText"); // Получаем переданный список аудиторий
        TextView subTextView = findViewById(R.id.cardViewAudSubText_second); // Находим TextEdit на слое
        subTextView.setText(subText); // Устанавливаем наш subText

        // Настраиваем картинку
        int imageResId = getIntent().getIntExtra("imageResId", R.drawable.agpu); // Получаем переданный id изображения
        ImageView imageView = findViewById(R.id.cardViewAudImage_second); // Находим нашу вьюшку (ImageView) на слое

        imageView.setImageResource(imageResId);


        // Обработка кнопки перехода на карту
        int itemPosition = getIntent().getIntExtra("itemPosition", 0); // Получем переданный id позиции элемента
        findViewById(R.id.btn_building_info).setOnClickListener(v -> // И при нажатии на кнопку
                new ListViewAud_ClickListener(itemPosition, this));  // Открываем карты
    }
}
