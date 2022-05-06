package ru.agpu.artikproject.background_work.debug;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Класс отвечающий за получение информации об устройстве
 */
public class Device_info {
    /**
     * @param context Контекст приложения
     * @return Видимая версия приложения
     */
    public static String getAppVersion(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return packageInfo.versionName;
    }

    /**
     * @param context Контекст приложения
     * @return Код версии приложения
     */
    public static int getAppVersionCode(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return packageInfo.versionCode;
    }

    /**
     * @param context Контекст приложения
     * @return Ширину устройства (px)
     */
    public static int getDeviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * @param context Контекст приложения
     * @return Высоту устройства (px)
     */
    public static int getDeviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * @return Название производителя
     */
    public static String getDeviceManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * @return Название продукта
     */
    public static String getDeviceProduct() {
        return android.os.Build.PRODUCT;
    }

    /**
     * @return Модель телефона
     */
    public static String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    /**
     * @return Название оборудования (Device)
     */
    public static String getDeviceDevice() {
        return android.os.Build.DEVICE;
    }

    /**
     * @return Название оборудования (Hardware)
     */
    public static String getDeviceHardware() {
        return android.os.Build.HARDWARE;
    }

    /**
     * @return Идентификатор
     */
    public static String getDeviceDisplay() {
        return android.os.Build.DISPLAY;
    }

    /**
     * @return ID
     */
    public static String getDeviceId() {
        return android.os.Build.ID;
    }

    /**
     * @return Android SDK
     */
    public static int getDeviceSDK() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * @return Версия Android для телефона
     */
    public static String getDeviceAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }
}
