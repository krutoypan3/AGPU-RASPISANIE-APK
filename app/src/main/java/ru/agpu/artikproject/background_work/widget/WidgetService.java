package ru.agpu.artikproject.background_work.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import ru.agpu.artikproject.background_work.widget.providers.NewWidgetAdapter;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NewWidgetAdapter(getApplicationContext(), intent);
    }

}