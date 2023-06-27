package ru.agpu.artikproject.background_work;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite;
import ru.agpu.artikproject.background_work.main_show.ListViewGroupListener;
import ru.agpu.artikproject.background_work.main_show.WatchSaveGroupRasp;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.ShowBuildingsOnTheMap;
import ru.agpu.artikproject.background_work.rasp_show.ParaInfo;
import ru.agpu.artikproject.presentation.layout.MainActivity;

public class CustomAlertDialog extends Dialog implements android.view.View.OnClickListener {

    public final Activity act;
    public Button yes, no;
    public ImageView para_info_photo;
    public TextView main_text, body_text;
    public EditText edit_text;
    public String new_main_text, new_body_text;
    public Uri uri;
    public String dialog_type;
    public ListView list_view;
    private static final int DIALOG_PHOTO_HEIGHT = 700;

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
        body_text.setMovementMethod(new ScrollingMovementMethod());
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
            case "para_pasha":
                main_text.setText(R.string.pasha_god);
                body_text.setVisibility(View.INVISIBLE);
                yes.setVisibility(View.INVISIBLE);
                list_view.setVisibility(View.VISIBLE);
                para_info_photo.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = para_info_photo.getLayoutParams(); // получаем параметры
                params.height = DIALOG_PHOTO_HEIGHT;
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                para_info_photo.setLayoutParams(params);
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
                yes.setText(R.string.go);
                break;
            case "delete_one_saved_group":
                main_text.setText(R.string.delete_one_save);
                body_text.setText(R.string.delete_confirm);
                yes.setText(R.string.Delete);
                break;
            case "about":
                main_text.setText(R.string.app_info);
                body_text.setText(R.string.app_info_body);
                yes.setVisibility(View.INVISIBLE);
                no.setVisibility(View.INVISIBLE);
                break;
            case "ricardo_pasha":
                main_text.setText(R.string.rickardo_show);
                body_text.setVisibility(View.INVISIBLE);
                para_info_photo.setVisibility(View.VISIBLE);
                no.setVisibility(View.INVISIBLE);
                yes.setText(R.string.god_of_flex);
                params = para_info_photo.getLayoutParams(); // получаем параметры
                params.height = DIALOG_PHOTO_HEIGHT;
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                para_info_photo.setLayoutParams(params);
                break;
        } // Объясняю почему ставим высоту и ширину скрываемых элементов 1, а не 0.
        // Так как у некоторых View 0 воспринимается как wrap_content (например EditText),
        // Было принято решение ставить 1 вместо 0.
        if (para_info_photo.getVisibility() == View.INVISIBLE) // Если фотка в диалоге пустая
            para_info_photo.getLayoutParams().height = 1; // Скрываем фото
        if (body_text.getVisibility() == View.INVISIBLE) { // Если текст в теле пустой
            body_text.getLayoutParams().height = 1; // Скрываем текст
            body_text.setPadding(0, 0, 0, 0); // Убираем отступы
        }
        if (list_view.getVisibility() == View.INVISIBLE) // Если список пуст
            list_view.getLayoutParams().height = 1; // Убираем список
        if (edit_text.getVisibility() == View.INVISIBLE) // Если ввод текста пуст
            edit_text.getLayoutParams().height = 1; // Убираем ввод текста
        if (no.getVisibility() == View.INVISIBLE){ // Если кнопка отмены пуста
            no.getLayoutParams().height = 1; // Убираем кнопку отмены
            no.getLayoutParams().width = 1;
        }
        else{ // Если кнопка отмены не пуста
            no.setOnClickListener(this); // Прослушиваем нажатия на кнопку отмены
        }
        if (yes.getVisibility() == View.INVISIBLE){ // Если кнопка принятия пуста
            yes.getLayoutParams().height = 1; // Убираем кнопку принятия
            yes.getLayoutParams().width = 1;
        }
        else // Если кнопка принятия не пуста
            yes.setOnClickListener(this); // Прослушиваем кнопку принятия
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
                        DataBaseSqlite.Companion.getSqliteDatabase(v.getContext()).execSQL("DELETE FROM raspisanie");
                        DataBaseSqlite.Companion.getSqliteDatabase(v.getContext()).execSQL("DELETE FROM rasp_update");
                        MainActivity.group_listed = null;
                        new WatchSaveGroupRasp(act, null);
                        break;
                    case "map_confirm":
                        new ShowBuildingsOnTheMap(ParaInfo.Companion.getFinalCorp(), act);
                        break;
                    case "delete_one_saved_group":
                        DataBaseSqlite.Companion.getSqliteDatabase(v.getContext()).delete("raspisanie", "r_group_code = '" + MainActivity.group_listed_id[ListViewGroupListener.Companion.getPosition()] +
                                "' AND r_search_type = '" + MainActivity.group_listed_type[ListViewGroupListener.Companion.getPosition()] + "'", null);
                        new WatchSaveGroupRasp(act, null); // Первичный вывод групп которые были открыты ранее
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