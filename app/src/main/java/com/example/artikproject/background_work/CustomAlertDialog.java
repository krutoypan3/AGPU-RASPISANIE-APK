package com.example.artikproject.background_work;

import static com.example.artikproject.layout.MainActivity.sqLiteDatabase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artikproject.R;
import com.example.artikproject.background_work.main_show.ListViewGroupListener;
import com.example.artikproject.background_work.server.SendInfoToServer;
import com.example.artikproject.background_work.main_show.ListViewAud_ClickListener;
import com.example.artikproject.background_work.main_show.WatchSaveGroupRasp;
import com.example.artikproject.background_work.rasp_show.Para_info;
import com.example.artikproject.layout.MainActivity;

public class CustomAlertDialog extends Dialog implements android.view.View.OnClickListener {

    public final Activity act;
    public Button yes, no;
    public ImageView para_info_photo;
    public TextView main_text, body_text;
    public EditText edit_text;
    public String new_main_text, new_body_text;
    public Uri uri;
    public final String dialog_type;
    public ListView list_view;

    /**
     * Диалог о наличии обновлений
     * @param act Активити
     * @param new_main_text Текст о наличии обновлений
     * @param new_body_text Тело текста
     * @param uri Ссылка на скачивание
     */
    public CustomAlertDialog(Activity act, String new_main_text, String new_body_text, Uri uri) {
        super(act);
        this.act = act;
        this.new_main_text = new_main_text;
        this.uri = uri;
        this.new_body_text = new_body_text;
        this.dialog_type = "update";
    }

    /**
     * Диалог с указанием типа
     * @param act Активити
     */
    public CustomAlertDialog(Activity act, String dialog_type) {
        super(act);
        this.act = act;
        this.dialog_type = dialog_type;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        main_text = findViewById(R.id.custom_dialog_main_text);
        body_text = findViewById(R.id.custom_dialog_body_text);
        edit_text = findViewById(R.id.custom_dialog_edit_text);
        list_view = findViewById(R.id.custom_dialog_list_view);
        para_info_photo = findViewById(R.id.para_info_photo);
        yes = findViewById(R.id.btn_alert_dialog_yes);
        no = findViewById(R.id.btn_alert_dialog_no);
        switch (dialog_type) {
            case "update":
                main_text.setText(new_main_text);
                body_text.setText(new_body_text);
                yes.setText(R.string.Update);
                break;
            case "delete":
                main_text.setText(R.string.delete_all_save);
                body_text.setText(R.string.delete_confirm);
                yes.setText(R.string.Delete_everything);
                break;
            case "feedback":
                main_text.setText(R.string.FeedBack_title);
                body_text.setText(R.string.FeedBack_subtitle);
                yes.setText(R.string.Post_review);
                edit_text.setVisibility(View.VISIBLE);
                break;
            case "para_pasha":
                main_text.setText(R.string.pasha_god);
                body_text.setVisibility(View.INVISIBLE);
                yes.setVisibility(View.INVISIBLE);
                list_view.setVisibility(View.VISIBLE);
                para_info_photo.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = para_info_photo.getLayoutParams(); // получаем параметры
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                para_info_photo.setLayoutParams(params);
                break;
            case "para_info":
                main_text.setText(R.string.para_info);
                body_text.setVisibility(View.INVISIBLE);
                yes.setVisibility(View.INVISIBLE);
                list_view.setVisibility(View.VISIBLE);
                break;
            case "groups_list":
                main_text.setText(R.string.groups_list);
                body_text.setVisibility(View.INVISIBLE);
                yes.setVisibility(View.INVISIBLE);
                list_view.setVisibility(View.VISIBLE);
                break;
            case "faculties_list":
                main_text.setText(R.string.faculties_list);
                body_text.setVisibility(View.INVISIBLE);
                yes.setVisibility(View.INVISIBLE);
                list_view.setVisibility(View.VISIBLE);
                break;
            case "weeks_list":
                main_text.setText(R.string.weeks_list);
                body_text.setVisibility(View.INVISIBLE);
                yes.setVisibility(View.INVISIBLE);
                list_view.setVisibility(View.VISIBLE);
                break;
            case "map_confirm":
                main_text.setText(R.string.map_confirm);
                body_text.setText(R.string.redirect_to_map);
                yes.setText(R.string.Go_to_map);
                break;
            case "delete_one_saved_group":
                main_text.setText(R.string.delete_one_save);
                body_text.setText(R.string.delete_confirm);
                yes.setText(R.string.Delete);
            case "about":
                main_text.setText(R.string.app_info);
                body_text.setText(R.string.app_info_body);
                yes.setVisibility(View.INVISIBLE);
                no.setVisibility(View.INVISIBLE);
        }
        no.setText(R.string.Cancel);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_alert_dialog_yes:
                switch (dialog_type){
                    case "update":
                        act.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        break;
                    case "delete":
                        sqLiteDatabase.execSQL("DELETE FROM raspisanie");
                        sqLiteDatabase.execSQL("DELETE FROM rasp_update");
                        MainActivity.group_listed = null;
                        new WatchSaveGroupRasp(act.getApplicationContext());
                        break;
                    case "feedback":
                        String value = String.valueOf(edit_text.getText());
                        Toast.makeText(act.getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        new SendInfoToServer(act.getApplicationContext(), value).start();
                        break;
                    case "map_confirm":
                        new ListViewAud_ClickListener(Para_info.finalCorp, act);
                        break;
                    case "delete_one_saved_group":
                        sqLiteDatabase.delete("raspisanie", "r_group_code = '" + MainActivity.group_listed_id[ListViewGroupListener.position] +
                                "' AND r_search_type = '" + MainActivity.group_listed_type[ListViewGroupListener.position] + "'", null);
                        new WatchSaveGroupRasp(act.getApplicationContext()); // Первичный вывод групп которые были открыты ранее
                        break;
                }
                break;
            case R.id.btn_alert_dialog_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();

    }
}