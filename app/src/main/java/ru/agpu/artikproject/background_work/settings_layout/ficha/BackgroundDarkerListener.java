package ru.agpu.artikproject.background_work.settings_layout.ficha;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

import ru.agpu.artikproject.R;

public class BackgroundDarkerListener {
    /**
     * Слкшатель нажатий на фон в настройках (пасхалочки)
     * @param act Активити
     */
    @SuppressLint("ClickableViewAccessibility")
    public BackgroundDarkerListener(Activity act){
        RelativeLayout update_layout = act.findViewById(R.id.update_layout); // Находим сам фон
        ImageView animImage = act.findViewById(R.id.animImage); // Находим изначально невидимое пустое изображение
        update_layout.setOnTouchListener((v, event) -> { // При нажатии на фон
            animImage.setX(event.getX()-100); // Определяем позицию нажатия по X
            animImage.setY(event.getY()-100); // Определяем позицию нажатия по Y
            switch (event.getAction()){ // Получаем информацию о том, что делает пользователь
                case MotionEvent.ACTION_DOWN: // Если опускает палец на экран
                    FichaAchievements.Companion.put(act.getApplicationContext(), "ficha_setting_logo"); // Засчитываем фичу с плавающим логотипом
                    new FichaShow(act); // Обновляем список найденных фич, т.к. находимся на слое настроек
                    animImage.setVisibility(View.VISIBLE); // Делаем невидимую картинку видимой
                    if (new Random().nextInt(20) == 0){ // С шансом 1 к 20 устанавливаем другое фото вместо логотипа
                        animImage.setImageResource(R.drawable.ficha_leonardo); // Ставим фото Леонардо
                        FichaAchievements.Companion.put(act.getApplicationContext(), "ficha_setting_leonardo"); // Засчитываем фичу
                    }
                    break;
                case MotionEvent.ACTION_UP: // Если поднимает палец
                    animImage.setVisibility(View.INVISIBLE); // Скрываем плавающий логотип
                    animImage.setImageResource(R.drawable.agpu_ico); // И возвращаем его (если была активирована фича с Леонардо)
                    break;
            }
            return true;
        });
    }
}
