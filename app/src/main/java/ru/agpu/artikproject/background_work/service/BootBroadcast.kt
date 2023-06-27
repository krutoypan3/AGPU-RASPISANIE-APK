package ru.agpu.artikproject.background_work.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class BootBroadcast: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i("onReceive", "Обн.: " + intent.action)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, PlayService::class.java))
            } else {
                context.startService(Intent(context, PlayService::class.java))
            }
        } catch (e: Exception) {
            Log.e("BootBroadcast", "onReceive error")
            e.printStackTrace()
        }
    }
}