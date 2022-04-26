package com.example.artikproject.background_work;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class GalleryUtil extends Activity{
    private final static int RESULT_SELECT_IMAGE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            // Запускаем выбор картинки из галереи
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_SELECT_IMAGE);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Обрабатываем выбор пользователя
        if (requestCode == RESULT_SELECT_IMAGE) {
            // Если картинка выбрана и путь к ней не пустой
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                try {
                    Uri selectedImage = data.getData(); // Получаем путь к картинке
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    // Возвращаем путь к изображению
                    Intent returnFromGalleryIntent = new Intent();
                    returnFromGalleryIntent.putExtra("picturePath", picturePath);
                    setResult(RESULT_OK, returnFromGalleryIntent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Intent returnFromGalleryIntent = new Intent();
                    setResult(RESULT_CANCELED, returnFromGalleryIntent);
                    finish();
                }
            } else {
                Intent returnFromGalleryIntent = new Intent();
                setResult(RESULT_CANCELED, returnFromGalleryIntent);
                finish();
            }
        }
    }
}