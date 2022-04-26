package com.example.artikproject.background_work;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.artikproject.R;
import com.example.artikproject.background_work.datebase.MySharedPreferences;
import com.example.artikproject.layout.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSelecter extends Activity {

    private final int GALLERY_ACTIVITY_CODE=200;
    private final int REQUEST_EXTERNAL_STORAGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type = getIntent().getStringExtra("background");
        System.out.println(type);
        if (!(ActivityCompat.checkSelfPermission(this, // Если разрешение не предоставлено
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            // Вызываем диалог с запросом разрешения
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_EXTERNAL_STORAGE);
        }
    }

    // Проверяем получены ли разрешения
    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        boolean granted = true;
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        granted = false;
                        break;
                    }
                }
            } else {
                granted = false;
            }
        }
        if (granted){ // Если разрешения получены
            // Запуск активити с выбором изображения из галереи
            Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
            startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
        }
        else{ // Если разрешения не получены, то сообщаем об этом пользователю
            Toast.makeText(getApplicationContext(), R.string.Permission_denied, Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) { // Если завершен выбор изображения
            if(resultCode == Activity.RESULT_OK){ // Если изображение выбрано
                try {
                    data.getParcelableExtra("data");

                    // Получаем путь к выбранной картинке
                    String path = (String) data.getExtras().get("picturePath");

                    // Извлекаем изображение по полученному пути
                    Bitmap image = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(path))));

                    // Получаем тип картинки (светлая \ темная)
                    String type = getIntent().getStringExtra("background");

                    // Сохраняем изображение в памяти приложения
                    storeImage(image, type + ".jpg");

                    // Подключаемся к новосозданному файлу
                    File file = new File(getFilesDir(), type + ".jpg");

                    // И сохраняем путь к картинке в приложении
                    MySharedPreferences.put(getApplicationContext(), type, file.getPath());

                    Toast.makeText(getApplicationContext(), R.string.theme_apply, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.Error_image_selection, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            else{
                // Если картинка не выбрана, то выводим соответствующее сообщение
                Toast.makeText(getApplicationContext(), R.string.Operation_canceled, Toast.LENGTH_SHORT).show();
            }
        }
        finish(); // Закрываем активити
    }

    /**
     * Сохранение картинки в файлах приложения
     * @param image Bitmap-изображение, которое необходимо сохранить
     */
    private void storeImage(Bitmap image, String filepath) {
        File file = new File(getFilesDir(), filepath);
        try {
            if(!file.exists()) // Если файл по такому пути не существует
                file.createNewFile(); // Создаем новый файл
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", e.getMessage());
        } catch (IOException e) {
            Log.e("IOException", e.getMessage());
        }
    }
}