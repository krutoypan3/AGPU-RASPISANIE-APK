package ru.agpu.artikproject.background_work.service

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun doWork(): Result {
        CheckRaspChanges(applicationContext)
        return Result.success()
    }
}