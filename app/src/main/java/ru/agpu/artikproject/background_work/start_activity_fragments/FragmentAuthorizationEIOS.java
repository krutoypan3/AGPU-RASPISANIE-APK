package ru.agpu.artikproject.background_work.start_activity_fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CustomAlertDialog;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.agpu.artikproject.background_work.eios.Authorization;
import ru.agpu.artikproject.background_work.eios.GetUserInfo;
import ru.agpu.artikproject.layout.StartActivity;

public class FragmentAuthorizationEIOS extends Fragment {

    // Тут вроде все готово
    public FragmentAuthorizationEIOS() {
        super(R.layout.fragment_start_activity_authorization_eios);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity act = (Activity) view.getContext();

        EditText Login_EIOS = view.findViewById(R.id.Login_EIOS);
        EditText Password_EIOS = view.findViewById(R.id.Password_EIOS);

        StartActivity.FRAGMENT = StartActivity.BACK_TO_EIOS;

        // Продолжить (кнопка авторизации)
        view.findViewById(R.id.yes_i_have_data_btn).setOnClickListener(view1 -> {
            view.findViewById(R.id.yes_i_have_data_btn).setClickable(false);
            view.findViewById(R.id.yes_i_have_data_btn).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            String Login = Login_EIOS.getText().toString();
            String Password = Password_EIOS.getText().toString();

            Authorization authorization = new Authorization(Login, Password);
            authorization.start();
            Runnable waiting_task = () -> {
                try {
                    authorization.join();
                    if (!authorization.getACCESS_TOKEN().equals("")){
                        try {
                            GetUserInfo userInfo = new GetUserInfo(authorization.getACCESS_TOKEN());
                            userInfo.start();
                            Runnable waiting_task2 = () -> {
                                try {
                                    userInfo.join();
                                    MySharedPreferences.put(view.getContext(), "EIOS_LOGIN", Login);
                                    MySharedPreferences.put(view.getContext(), "EIOS_PASSWORD", Password);
                                    MySharedPreferences.put(view.getContext(), "user_info_fio", userInfo.getUserFIO());
                                    MySharedPreferences.put(view.getContext(), "user_info_group_name", userInfo.getGroupName());
                                    MySharedPreferences.put(view.getContext(), "user_info_group_id", userInfo.getGroupId());
                                    MySharedPreferences.put(view.getContext(), "user_info_first_name", userInfo.getUserFirstName());
                                    MySharedPreferences.put(view.getContext(), "user_info_last_name", userInfo.getUserLastName());
                                    MySharedPreferences.put(view.getContext(), "user_info_middle_name", userInfo.getUserMiddleName());
                                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentAuthorizationEIOSSuccessfully.class, null).commit();
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            };
                            Thread authorization_wait_thread2 = new Thread(waiting_task2);
                            authorization_wait_thread2.start();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else {
                        act.runOnUiThread(() -> {
                            TextView second_text = view.findViewById(R.id.second_text);
                            if (authorization.getMSG_ERROR() != null)
                                second_text.setText(authorization.getMSG_ERROR());
                            else
                                second_text.setText(getString(R.string.Error));
                            view.findViewById(R.id.yes_i_have_data_btn).setClickable(true);
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            Thread authorization_wait_thread = new Thread(waiting_task);
            authorization_wait_thread.start();
        });

        // Я не помню пароль
        view.findViewById(R.id.I_dont_remember_the_password_ET).setOnClickListener(view1 -> {
            view.findViewById(R.id.I_dont_remember_the_password_ET).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            view.findViewById(R.id.I_dont_remember_the_password_ET).setClickable(false);
            CustomAlertDialog cdd = new CustomAlertDialog(act, getString(R.string.Restore_password), getString(R.string.You_will_be_directed_to_the_EIOS), Uri.parse("http://plany.agpu.net/Account/Register.aspx?fPass=1"));
            cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
            cdd.show();
            cdd.yes.setText(R.string.go);
            view.findViewById(R.id.I_dont_remember_the_password_ET).setClickable(true);
        });
    }
}
