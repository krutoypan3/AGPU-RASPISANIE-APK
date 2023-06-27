package ru.agpu.artikproject.background_work.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class PlayService: Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val uploadWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(applicationContext).enqueue(uploadWorkRequest)
        println("Сервис номер $startId был запущен")
        return super.onStartCommand(intent, flags, startId)
    }
}