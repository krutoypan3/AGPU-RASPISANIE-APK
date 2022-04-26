package com.example.artikproject.background_work;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.RelativeLayout;

import com.example.artikproject.R;
import com.example.artikproject.background_work.datebase.MySharedPreferences;

import java.io.File;

public class SetNewBackground {

    /**
     * Установка фона активити
     * @param layout RelativeLayout на который устанавливается картинка
     */
    public static void setting(RelativeLayout layout){
        // Получаем путь к файлу
        File file = new File(MySharedPreferences.get(layout.getContext(), "background_user", ""));
        if (!file.getAbsolutePath().equals("")) { // Если путь к файлу не пустой
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath()); // Получаем картинку
            layout.setBackground(new BitmapDrawable(layout.getResources(), bitmap)); // Устанавливаем на фон
        }
        else{ // Если путь пустой, то ставим стандартный фон
            layout.setBackgroundResource(R.drawable.background);
        }
    }
}
