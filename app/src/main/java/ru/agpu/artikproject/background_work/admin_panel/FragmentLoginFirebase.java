package ru.agpu.artikproject.background_work.admin_panel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.theme.CustomBackground;

/**
 * Фрагмент с авторизацией пользователя в Google Firebase
 */
public class FragmentLoginFirebase extends Fragment {

    public FragmentLoginFirebase() {
        super(R.layout.fragment_admin_panel_login_firebase);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Проверка аунтефикации пользователя
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) { // Если пользователь не авторизирован
            // Установка нового фона [и ТУТ НЕТ затемнителя] | Должно быть после setContentView
            view.findViewById(R.id.fragment_admin_panel).setBackground(CustomBackground.getBackground(view.getContext()));

            Activity act = (Activity) view.getContext();

            // Инициализируем поля ввода логина и пароля
            EditText Login_firebase = view.findViewById(R.id.Login_firebase);
            EditText Password_firebase = view.findViewById(R.id.Password_firebase);

            // Прослушиваем нажатия на кнопку авторизации
            view.findViewById(R.id.btn_continue).setOnClickListener(view1 -> {
                // Получаем логин и пароль из полей ввода
                String Login_firebase_text = Login_firebase.getText().toString();
                String Password_firebase_text = Password_firebase.getText().toString();
                // Если логин или пароль пустые
                if (Login_firebase_text.equals("") || Password_firebase_text.equals(""))
                    // Выводим сообщение об ошибке
                    Toast.makeText(view1.getContext(), R.string.Error, Toast.LENGTH_SHORT).show();
                else { // Авторизируемся в системе по логину и паролю
                    mAuth.signInWithEmailAndPassword(Login_firebase_text, Password_firebase_text)
                        .addOnCompleteListener(act, task -> {
                            if (task.isSuccessful()) { // Если авторизация успешна - переходим к следующему фрагменту
                                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentShowListDirections.class, null).commit();
                            } else { // Если авторизация не удалась - выводим сообщение об ошибке
                                Toast.makeText(act, R.string.Error, Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            });
        } else {
            // Если пользователь авторизирован, то сразу переходим к следующему фрагменту
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentShowListDirections.class, null).commit();
        }
    }
}