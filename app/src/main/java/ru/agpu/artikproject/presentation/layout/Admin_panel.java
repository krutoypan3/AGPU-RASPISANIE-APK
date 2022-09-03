package ru.agpu.artikproject.presentation.layout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.theme.Theme;

public class Admin_panel extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Theme.setting(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_panel);
    }
}
