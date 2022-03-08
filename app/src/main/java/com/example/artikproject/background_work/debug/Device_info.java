package com.example.artikproject.background_work.debug;
import android.content.Context;

public class Device_info {

    /**
     * Получить ширину устройства (px)
     *
     */
    public static int getDeviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * Получить высоту устройства (px)
     */
    public static int getDeviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * Получить название производителя
     * **/
    public static String getDeviceManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * Получить название продукта
     * **/
    public static String getDeviceProduct() {
        return android.os.Build.PRODUCT;
    }


    /**
     * Получить модель телефона
     */
    public static String getDeviceModel() {
        return android.os.Build.MODEL;
    }


    /**
     * Название оборудования
     * **/
    public static String getDeviceDevice() {
        return android.os.Build.DEVICE;
    }



    /**
     * Название оборудования
     *
     * **/
    public static String getDeviceHardware() {
        return android.os.Build.HARDWARE;
    }


    /**
     *
     * Показать идентификатор
     * **/
    public static String getDeviceDisplay() {
        return android.os.Build.DISPLAY;
    }

    /**
     * ID
     *
     * **/
    public static String getDeviceId() {
        return android.os.Build.ID;
    }


    /**
     * Получить Android SDK мобильного телефона
     *
     * @return
     */
    public static int getDeviceSDK() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * Получить версию Android для телефона
     *
     * @return
     */
    public static String getDeviceAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }
}
