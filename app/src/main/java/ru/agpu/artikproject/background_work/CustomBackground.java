package ru.agpu.artikproject.background_work;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import java.io.File;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.agpu.artikproject.background_work.theme.Theme;

public class CustomBackground {

    /**
     * Функция получения фона в зависимости от текущей темы
     * @param context Контекст приложения
     * @return Drawable - изображение
     */
    public static Drawable getBackground(Context context){
        int theme = Theme.getApplicationTheme(context);  // Получаем тему приложения
        if (MySharedPreferences.get(context, "enable_background_user", false)) { // Если включен пользовательский фон
            if (theme == AppCompatDelegate.MODE_NIGHT_NO)
                return getBackgroundDrawable(context, "background_light"); // Если светлая, то возвращаем светлый фон
            return getBackgroundDrawable(context, "background_dark"); // Если темная, то возвращаем темный фон
        } // Если пользовательский фон отключен, то возвращаем стандартный фон
        if (theme == AppCompatDelegate.MODE_NIGHT_NO)
            return AppCompatResources.getDrawable(context, R.drawable.background_light);
        return AppCompatResources.getDrawable(context, R.drawable.background_dark);
    }

    /**
     * Функция получения затемнителя фона в зависимости от текущей темы
     * @param context Контекст приложения
     * @return Color - цвет картинки
     */
    public static int getBackgroundDarker(Context context){
        if (Theme.getApplicationTheme(context) == AppCompatDelegate.MODE_NIGHT_YES) { // Получаем тему приложения в настройках
            return getBackgroundDarkerColor(context, false); // Если темная -  затемнитель темный
        } // Если тема светлая то ставим стандартный фон
        return getBackgroundDarkerColor(context, true);
    }

    // Микрофункция возвращающая картинку в зависимости от темы
    private static Drawable getBackgroundDrawable(Context context, String background_type){
        File file = new File(MySharedPreferences.get(context, background_type, "")); // Получаем картинку фона
        if (!file.equals(new File(""))) { // Если путь к файлу не пустой, то возвращаем картинку
            return new BitmapDrawable(context.getResources(), BitmapFactory.decodeFile(file.getAbsolutePath()));
        } // Если файл пустой - возвращаем стандартные обои
        if (Theme.getApplicationTheme(context) == AppCompatDelegate.MODE_NIGHT_NO)
            return AppCompatResources.getDrawable(context, R.drawable.background_light);
        return AppCompatResources.getDrawable(context, R.drawable.background_dark);
    }

    // Микрофункция возвращающая цвет в зависимости от темы
    private static int getBackgroundDarkerColor(Context context, boolean lightDarker){
        if (lightDarker) { // Если тема светлая, то возвращаем светлый затемнитель
            int level = MySharedPreferences.get(context, "light_darker_level", 60);
            return Color.argb((int) (level * 2.5), 255, 255, 255);
        } // Если тема темная, то возвращаем темный затемнитель
        int level = MySharedPreferences.get(context, "dark_darker_level", 30);
        return Color.argb((int) (level * 2.5), 0, 0, 0);
    }
}
