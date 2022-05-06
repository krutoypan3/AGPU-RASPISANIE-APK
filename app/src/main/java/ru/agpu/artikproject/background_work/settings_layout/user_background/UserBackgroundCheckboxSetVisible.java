package ru.agpu.artikproject.background_work.settings_layout.user_background;

import android.app.Activity;
import android.view.View;
import android.widget.GridLayout;

import ru.agpu.artikproject.R;

public class UserBackgroundCheckboxSetVisible {
    /**
     * Данный класс изменяет видимость слоя с настройкой фона и затемнителя
     * @param act Активити
     * @param is_visible Сделать видимым(true) \ невидимым(false)
     */
    public UserBackgroundCheckboxSetVisible(Activity act, boolean is_visible){
        GridLayout layout = act.findViewById(R.id.layout_change_background); // Слой с элементами настройки фона
        if (is_visible) { // Если включаем видимость
            layout.setVisibility(View.VISIBLE); // Делаем фон видимым
            layout.getLayoutParams().width = GridLayout.LayoutParams.WRAP_CONTENT; // Растягиваем по размеру элементов
            layout.getLayoutParams().height = GridLayout.LayoutParams.WRAP_CONTENT; // Растягиваем по размеру элементов
        }
        else{ // Если выключаем видимость
            layout.setVisibility(View.INVISIBLE); // Делаем фон невидимым
            layout.getLayoutParams().width = 0;  // Сужаем слой до 0
            layout.getLayoutParams().height = 0; // Сужаем слой до 0
        }
        layout.requestLayout(); // Обновляем элемент на слое
    }
}
