package com.example.artikproject.background_work;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.artikproject.R;
import com.example.artikproject.background_work.datebase.MySharedPreferences;
import com.example.artikproject.background_work.theme.Theme;

import java.io.File;

public class SetNewBackground {

    /**
     * Установка фона активити
     * @param layout RelativeLayout на который устанавливается картинка
     */
    public static void setting(RelativeLayout layout){
        RelativeLayout layout_darker = layout.findViewById(R.id.background_darker);
        // Проверяем, включен ли пользовательский фон
        if (MySharedPreferences.get(layout.getContext(), "enable_background_user", false)){
            switch (Theme.get(layout.getContext())){ // Получаем тему приложения в настройках
                // Если тема светлая, то ставим светлый фон
                case AppCompatDelegate.MODE_NIGHT_NO:
                    setBackground(layout, "background_light");
                    setBackgroundDarker(layout_darker, true);
                    break;
                // Если тема темная, то ставим темный фон
                case AppCompatDelegate.MODE_NIGHT_YES:
                    setBackground(layout, "background_dark");
                    setBackgroundDarker(layout_darker, false);
                    break;
                // Если тема системная, то определяем тему системы
                case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                    int th = Theme.getCurrentSystemTheme(layout.getContext());
                    switch (th){
                        // Если в системе тема светлая, то ставим светлый фон
                        case AppCompatDelegate.MODE_NIGHT_NO:
                            setBackground(layout, "background_light");
                            setBackgroundDarker(layout_darker, true);
                            break;
                        // Если в системе тема темная, то ставим темный фон
                        case AppCompatDelegate.MODE_NIGHT_YES:
                            setBackground(layout, "background_dark");
                            setBackgroundDarker(layout_darker, false);
                            break;
                        default: // Если не удалось определить тему в системе, то ставим стандартный фон
                            layout.setBackgroundResource(R.drawable.background);
                            setBackgroundDarker(layout_darker, false);
                            break;
                    }
                    break;
            }
        }
        else{ // Если пользовательский фон отключен, то ставим стандартный фон
            layout.setBackgroundResource(R.drawable.background);
        }
    }

    private static void setBackground(RelativeLayout layout, String background_type){
        File file = new File(MySharedPreferences.get(layout.getContext(), background_type, ""));
        if (!file.getAbsolutePath().equals("")) { // Если путь к файлу не пустой
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath()); // Получаем картинку
            layout.setBackground(new BitmapDrawable(layout.getResources(), bitmap)); // Устанавливаем на фон
        }
        else{ // Если пользовательского фона нет, то ставим стандартный фон
            layout.setBackgroundResource(R.drawable.background);
        }
    }

    private static void setBackgroundDarker(RelativeLayout layout_darker, boolean lightDarker){
        if (layout_darker != null){
            if (lightDarker) {
                int level = MySharedPreferences.get(layout_darker.getContext(), "light_darker_level", 30);
                layout_darker.setBackgroundColor(Color.argb((int) (level * 2.5), 255, 255, 255));
            }
            else{
                int level = MySharedPreferences.get(layout_darker.getContext(), "dark_darker_level", 30);
                layout_darker.setBackgroundColor(Color.argb((int) (level * 2.5), 0, 0, 0));
            }
        }
    }
}
