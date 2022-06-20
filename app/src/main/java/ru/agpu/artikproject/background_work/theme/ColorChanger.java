package ru.agpu.artikproject.background_work.theme;

import android.graphics.Color;

/**
 * Класс, позволяющий проводить некоторые манипуляции с цветами
 */
public class ColorChanger {
    /**
     * Позволяет получить цвет темнее того, что подается на вход
     * @param color Исходный цвет
     * @param level Уровень затемнения [0..255]
     * @return Затемнённый цвет
     */
    public static int GetDarkColor(int color, int level){
        if (-255 > level || level > 255) // Проверяем входной уровень затемнения\засветления
            return color; // Если не входит в рамки, возвращаем исхождный цвет
        int red = Color.red(color) - level;
        if (red < 0) red = 0;
        if (red >255) red = 255;
        int green = Color.green(color) - level;
        if (green < 0) green = 0;
        if (green > 255) green = 255;
        int blue = Color.blue(color) - level;
        if (blue < 0) blue = 0;
        if (blue > 255) blue = 255;

        return Color.rgb(red, green, blue);
    }

    /**
     * Позволяет получить цвет светлее того, что подается на вход
     * @param color Исходный цвет
     * @param level Уровень засветления [0..255]
     * @return Засветлённый цвет
     */
    public static int GetLightColor(int color, int level){
        return GetDarkColor(color, -level);
    }
}
