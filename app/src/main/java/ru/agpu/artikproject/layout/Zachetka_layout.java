package ru.agpu.artikproject.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.agpu.artikproject.background_work.theme.CustomBackground;
import ru.agpu.artikproject.background_work.zach_book.ZachBook;

/**
 * Даное активити позволяем студентам просмотреть свою зачетную книжку
 */
public class Zachetka_layout extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zachetka_layout);

        // Установка нового фона и затемнителя | Должно быть после setContentView
        findViewById(R.id.zachetka_relative_layout).setBackground(CustomBackground.getBackground(getApplicationContext()));
        findViewById(R.id.background_darker).setBackgroundColor(CustomBackground.getBackgroundDarker(getApplicationContext()));

        TextView mainText = findViewById(R.id.zachetka_main_text); // Основной текст
        RecyclerView recyclerView = findViewById(R.id.recyclerView); // Список с оценками
        EditText ETLogin = findViewById(R.id.login); // Поле ввода логина
        EditText ETPassword = findViewById(R.id.password); // Поле ввода пароля
        Button loginButton = findViewById(R.id.login_btn); // Кнопка авторизации

        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Это обязательная фигня, я хз, но без нее список просто не показывается

        // Получаем данные о ранее введенных логине и пароле и вставляем их в текстовые поля
        ETLogin.setText(MySharedPreferences.get(getApplicationContext(), "EIOS_LOGIN", ""));
        ETPassword.setText(MySharedPreferences.get(getApplicationContext(), "EIOS_PASSWORD", ""));

        // Прослушиваем нажатия на кнопку авторизации
        loginButton.setOnClickListener(view -> {
            // Скрываем клавиатуру
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            loginButton.setClickable(false); // Делаем кнопку некликабельной
            loginButton.startAnimation(MainActivity.animRotate); // Запускаем анимацию
            mainText.setText(R.string.find_zach_book); // Меняем основной текст
            try { // Запускаем авторизацию на сайте и ищем зачетку
                new ZachBook(this, ETLogin.getText().toString(), ETPassword.getText().toString()).start();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }
}
