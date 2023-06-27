package ru.agpu.artikproject.background_work

import android.content.Context
import android.net.ConnectivityManager

object CheckInternetConnection {
    /**
     * Проверяет возможность подключения к интернету
     * @param context Контекст приложения
     * @return Состояние подключения к интернету [True / False]
     */
    fun getState(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}