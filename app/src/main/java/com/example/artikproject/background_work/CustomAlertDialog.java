package com.example.artikproject.background_work;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artikproject.R;
import com.example.artikproject.background_work.debug.SendInfoToServer;
import com.example.artikproject.background_work.main_show.ListViewAud_ClickListener;
import com.example.artikproject.background_work.main_show.WatchSaveGroupRasp;
import com.example.artikproject.background_work.rasp_show.Para_info;
import com.example.artikproject.layout.MainActivity;

public class CustomAlertDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity act;
    public Button yes, no;
    public TextView main_text, body_text;
    public EditText edit_text;
    public String new_main_text, new_body_text;
    public Uri uri;
    public String dialog_type;
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
        // TODO Auto-generated constructor stub
        this.act = act;
        this.new_main_text = new_main_text;
        this.uri = uri;
        this.new_body_text = new_body_text;
        this.dialog_type = "update";
    }

    /**
     * Диалог об удалении расписания
     * @param act Активити
     */
    public CustomAlertDialog(Activity act, String dialog_type) {
        super(act);
        // TODO Auto-generated constructor stub
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
        yes = (Button) findViewById(R.id.btn_alert_dialog_yes);
        no = (Button) findViewById(R.id.btn_alert_dialog_no);
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
            case "para_info":
                main_text.setText(R.string.para_info);
                body_text.setVisibility(View.INVISIBLE);
                yes.setVisibility(View.INVISIBLE);
                list_view.setVisibility(View.VISIBLE);
                break;
            case "map_confirm":
                main_text.setText(R.string.map_confirm);
                body_text.setText(R.string.redirect_to_map);
                yes.setText(R.string.Go_to_map);

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
                        MainActivity.sqLiteDatabase.execSQL("DELETE FROM rasp_test1");
                        MainActivity.sqLiteDatabase.execSQL("DELETE FROM rasp_update");
                        MainActivity.group_listed = null;
                        new WatchSaveGroupRasp(act.getApplicationContext());
                        break;
                    case "feedback":
                        String value = String.valueOf(edit_text.getText());
                        Toast.makeText(act.getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        new SendInfoToServer(act.getApplicationContext(), value);
                        break;
                    case "map_confirm":
                        new ListViewAud_ClickListener(Para_info.finalCorp, act);
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