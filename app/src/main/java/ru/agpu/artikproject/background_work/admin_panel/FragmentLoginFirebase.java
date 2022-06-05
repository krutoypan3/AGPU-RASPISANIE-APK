package ru.agpu.artikproject.background_work.admin_panel;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.agpu.artikproject.background_work.theme.CustomBackground;

public class FragmentLoginFirebase extends Fragment {

    public FragmentLoginFirebase() {
        super(R.layout.fragment_admin_panel_login_firebase);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Установка нового фона [и ТУТ НЕТ затемнителя] | Должно быть после setContentView
        view.findViewById(R.id.fragment_admin_panel).setBackground(CustomBackground.getBackground(view.getContext()));

        Activity act = (Activity) view.getContext();


        EditText Login_firebase = view.findViewById(R.id.Login_firebase);
        EditText Password_firebase = view.findViewById(R.id.Password_firebase);

        view.findViewById(R.id.btn_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize Firebase Auth
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                String Login_firebase_text =  Login_firebase.getText().toString();
                String Password_firebase_text = Password_firebase.getText().toString();

                if (Login_firebase_text.equals("") || Password_firebase_text.equals(""))
                    Toast.makeText(view.getContext(), R.string.Error, Toast.LENGTH_SHORT).show();
                else{
                    mAuth.signInWithEmailAndPassword(Login_firebase_text, Password_firebase_text)
                        .addOnCompleteListener(act, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(act, R.string.Welcome, Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();

                                FirebaseDatabase databaseReference = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = databaseReference.getReference("message");
                                if (user != null) {
                                    myRef.setValue("I'm user " + user.getEmail());
                                }
                                // Read from the database
                                myRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // This method is called once with the initial value and again
                                        // whenever data at this location is updated.
                                        String value = dataSnapshot.getValue(String.class);
                                        Log.d(TAG, "Value is: " + value);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Failed to read value
                                        Log.w(TAG, "Failed to read value.", error.toException());
                                    }
                                });
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(act, R.string.Error, Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        });

        // TODO Здесь нужно сделать проверку на авторизацию (был ли человек ранее авторизован)
        // Если это первый запуск приложения
        if (MySharedPreferences.get(getContext(),"IsFirstAppStart", true)){
            // Запускаем фрагмент с приветствием
//            new GetFullGroupList_Online().start(); // Получение полного списка групп и закидывание их в адаптер
//            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentWelcome.class, null).commit();
        }
        else { // Иначе показываем анимацию запускаи переходим в приложение
//            ImageView loading_ico = view.findViewById(R.id.loading_ico);
        }
    }

}