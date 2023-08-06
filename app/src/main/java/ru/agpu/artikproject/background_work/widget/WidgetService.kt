package ru.agpu.artikproject.background_work.widget

import android.content.Intent
import android.widget.RemoteViewsService
import ru.agpu.artikproject.background_work.widget.providers.WidgetAdapter

class WidgetService: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return WidgetAdapter(applicationContext, intent)
    }
}