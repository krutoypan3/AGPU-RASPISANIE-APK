package ru.agpu.artikproject.background_work.debug

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * Класс отвечающий за получение информации об устройстве
 */
object DeviceInfo {
    /**
     * @param context Контекст приложения
     * @return Видимая версия приложения
     */
    @Throws(PackageManager.NameNotFoundException::class)
    fun getAppVersion(context: Context): String? = context.packageManager.getPackageInfo(context.packageName, 0).versionName

    /**
     * @param context Контекст приложения
     * @return Код версии приложения
     */
    fun getAppVersionCode(context: Context) = context.packageManager.getPackageInfo(context.packageName, 0).versionCode

    /**
     * @param context Контекст приложения
     * @return Ширину устройства (px)
     */
    fun getDeviceWidth(context: Context) = context.resources.displayMetrics.widthPixels

    /**
     * @param context Контекст приложения
     * @return Высоту устройства (px)
     */
    fun getDeviceHeight(context: Context) = context.resources.displayMetrics.heightPixels

    /**
     * @return Название производителя
     */
    fun getDeviceManufacturer(): String? = Build.MANUFACTURER

    /**
     * @return Название продукта
     */
    fun getDeviceProduct(): String? = Build.PRODUCT

    /**
     * @return Модель телефона
     */
    fun getDeviceModel(): String? = Build.MODEL

    /**
     * @return Название оборудования (Device)
     */
    fun getDeviceDevice(): String? = Build.DEVICE

    /**
     * @return Название оборудования (Hardware)
     */
    fun getDeviceHardware(): String? = Build.HARDWARE

    /**
     * @return Идентификатор
     */
    fun getDeviceDisplay(): String? = Build.DISPLAY

    /**
     * @return ID
     */
    fun getDeviceId(): String? = Build.ID

    /**
     * @return Android SDK
     */
    fun getDeviceSDK() = Build.VERSION.SDK_INT

    /**
     * @return Версия Android для телефона
     */
    fun getDeviceAndroidVersion(): String? = Build.VERSION.RELEASE
}