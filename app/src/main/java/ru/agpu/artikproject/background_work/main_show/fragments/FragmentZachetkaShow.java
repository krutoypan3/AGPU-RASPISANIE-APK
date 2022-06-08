package ru.agpu.artikproject.background_work.main_show.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.agpu.artikproject.background_work.zach_book.ZachBook;
import ru.agpu.artikproject.layout.MainActivity;

public class FragmentZachetkaShow extends Fragment {
    // Тут вроде все готово
    public FragmentZachetkaShow() {
        super(R.layout.fragment_main_activity_zachetka_show);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = (Activity) view.getContext();

        TextView mainText = view.findViewById(R.id.zachetka_main_text); // Основной текст
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView); // Список с оценками
        EditText ETLogin = view.findViewById(R.id.login); // Поле ввода логина
        EditText ETPassword = view.findViewById(R.id.password); // Поле ввода пароля
        Button loginButton = view.findViewById(R.id.login_btn); // Кнопка авторизации

        recyclerView.setLayoutManager(new LinearLayoutManager(activity)); // Это обязательная фигня, я хз, но без нее список просто не показывается

        // Получаем данные о ранее введенных логине и пароле и вставляем их в текстовые поля
        ETLogin.setText(MySharedPreferences.get(view.getContext(), "EIOS_LOGIN", ""));
        ETPassword.setText(MySharedPreferences.get(view.getContext(), "EIOS_PASSWORD", ""));

        // Прослушиваем нажатия на кнопку авторизации
        loginButton.setOnClickListener(view2 -> {
            // Скрываем клавиатуру
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view2.getWindowToken(), 0);

            loginButton.setClickable(false); // Делаем кнопку некликабельной
            loginButton.startAnimation(MainActivity.animRotate); // Запускаем анимацию
            mainText.setText(R.string.find_zach_book); // Меняем основной текст
            try { // Запускаем авторизацию на сайте и ищем зачетку
                new ZachBook(activity, ETLogin.getText().toString(), ETPassword.getText().toString()).start();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }
}
