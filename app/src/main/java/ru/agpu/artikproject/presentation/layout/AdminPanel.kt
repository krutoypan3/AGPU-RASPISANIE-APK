package ru.agpu.artikproject.presentation.layout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.theme.Theme.setting

class AdminPanel: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setting(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_panel)
    }
}