package ru.agpu.artikproject.background_work.admin_panel

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements
import ru.agpu.artikproject.background_work.theme.CustomBackground

/**
 * Фрагмент с авторизацией пользователя в Google Firebase
 */
class FragmentLoginFirebase: Fragment(R.layout.fragment_admin_panel_login_firebase) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Проверка аунтефикации пользователя
        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        if (currentUser == null) { // Если пользователь не авторизирован
            // Установка нового фона [и ТУТ НЕТ затемнителя] | Должно быть после setContentView
            view.findViewById<View>(R.id.fragment_admin_panel).background = CustomBackground.getBackground(view.context)
            val act = view.context as Activity

            // Инициализируем поля ввода логина и пароля
            val loginFirebaseET = view.findViewById<EditText>(R.id.Login_firebase)
            val passwordFirebaseET = view.findViewById<EditText>(R.id.Password_firebase)

            // Прослушиваем нажатия на кнопку авторизации
            val continueBtn = view.findViewById<View>(R.id.btn_continue)
            continueBtn.setOnClickListener {
                // Получаем логин и пароль из полей ввода
                val loginFirebaseText = loginFirebaseET.text.toString()
                val passwordFirebaseText = passwordFirebaseET.text.toString()
                // Если логин или пароль пустые
                if ((loginFirebaseText == "") || (passwordFirebaseText == "")) // Выводим сообщение об ошибке
                    Toast.makeText(view.context, R.string.Error, Toast.LENGTH_SHORT).show()
                else if (loginFirebaseText.lowercase() == "admin" && passwordFirebaseText.lowercase() == "admin") {
                    FichaAchievements().playFichaAdmin(act)
                } else { // Авторизируемся в системе по логину и паролю
                    mAuth.signInWithEmailAndPassword(loginFirebaseText, passwordFirebaseText)
                        .addOnCompleteListener(act) { task ->
                            if (task.isSuccessful) { // Если авторизация успешна - переходим к следующему фрагменту
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container_view, FragmentShowListDirections::class.java, null)
                                    .commit()
                            } else { // Если авторизация не удалась - выводим сообщение об ошибке
                                Toast.makeText(act, R.string.Firebase_login_error, Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        } else {
            // Если пользователь авторизирован, то сразу переходим к следующему фрагменту
            parentFragmentManager.beginTransaction().replace(
                R.id.fragment_container_view,
                FragmentShowListDirections::class.java, null
            ).commit()
        }
    }
}