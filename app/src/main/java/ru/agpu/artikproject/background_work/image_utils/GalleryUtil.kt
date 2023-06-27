package ru.agpu.artikproject.background_work.image_utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore

class GalleryUtil: Activity() {
    companion object {
        private const val RESULT_SELECT_IMAGE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try { // Запускаем выбор картинки из галереи
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, RESULT_SELECT_IMAGE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Обрабатываем выбор пользователя
        if (requestCode == RESULT_SELECT_IMAGE) {
            // Если картинка выбрана и путь к ней не пустой
            if (resultCode == RESULT_OK && data != null && data.data != null) {
                try {
                    val selectedImage = data.data // Получаем путь к картинке
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(
                        selectedImage!!,
                        filePathColumn, null, null, null
                    )
                    cursor!!.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val picturePath = cursor.getString(columnIndex)
                    cursor.close()

                    // Возвращаем путь к изображению
                    val returnFromGalleryIntent = Intent()
                    returnFromGalleryIntent.putExtra("picturePath", picturePath)
                    setResult(RESULT_OK, returnFromGalleryIntent)
                    finish()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    setResult(RESULT_CANCELED, Intent())
                    finish()
                }
            } else {
                setResult(RESULT_CANCELED, Intent())
                finish()
            }
        }
    }
}