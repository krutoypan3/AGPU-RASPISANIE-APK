package ru.agpu.artikproject.background_work.image_utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences
import ru.agpu.artikproject.background_work.debug.DeviceInfo.getDeviceHeight
import ru.agpu.artikproject.background_work.debug.DeviceInfo.getDeviceWidth
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ImageSelector: Activity() {
    companion object {
        private const val GALLERY_ACTIVITY_CODE = 200
        private const val REQUEST_EXTERNAL_STORAGE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = intent.getStringExtra("background")
        println(type)

        // Если разрешение не предоставлено
        val perm = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (perm != PackageManager.PERMISSION_GRANTED) {
            // Вызываем диалог с запросом разрешения
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    // Проверяем получены ли разрешения
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        var granted = true
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty()) {
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        granted = false
                        break
                    }
                }
            } else {
                granted = false
            }
        }
        if (granted) { // Если разрешения получены
            // Запуск активити с выбором изображения из галереи
            val galleryIntent = Intent(applicationContext, GalleryUtil::class.java)
            startActivityForResult(galleryIntent, GALLERY_ACTIVITY_CODE)
        } else { // Если разрешения не получены, то сообщаем об этом пользователю
            Toast.makeText(applicationContext, R.string.Permission_denied, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_ACTIVITY_CODE) { // Если завершен выбор изображения
            if (resultCode == RESULT_OK) { // Если изображение выбрано
                try {
                    data.getParcelableExtra<Parcelable>("data")

                    // Получаем путь к выбранной картинке
                    val path = data.extras!!["picturePath"] as String

                    // Извлекаем изображение по полученному пути
                    var image = BitmapFactory.decodeStream(
                        contentResolver.openInputStream(Uri.fromFile(File(path)))
                    )

                    // Подгоняем изображение под размер экрана
                    image = Bitmap.createScaledBitmap(
                        image,
                        getDeviceWidth(applicationContext),
                        getDeviceHeight(applicationContext), false
                    )

                    // Сжимаем изображение до 30% от исходного (вес картинки / 3)
                    val out = ByteArrayOutputStream()
                    image.compress(Bitmap.CompressFormat.JPEG, 30, out)
                    val decoded = BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))

                    // Получаем тип картинки (светлая \ темная)
                    val type = intent.getStringExtra("background")

                    // Сохраняем изображение в памяти приложения
                    storeImage(decoded, "$type.jpg")

                    // Подключаемся к новосозданному файлу
                    val file = File(filesDir, "$type.jpg")

                    // И сохраняем путь к картинке в приложении
                    MySharedPreferences.putPref(applicationContext, type, file.path)
                    Toast.makeText(applicationContext, R.string.theme_apply, Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, R.string.Error_image_selection, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            } else {
                // Если картинка не выбрана, то выводим соответствующее сообщение
                Toast.makeText(applicationContext, R.string.Operation_canceled, Toast.LENGTH_SHORT).show()
            }
        }
        finish() // Закрываем активити
    }

    /**
     * Сохранение картинки в файлах приложения
     * @param image Bitmap-изображение, которое необходимо сохранить
     */
    private fun storeImage(image: Bitmap, filepath: String) {
        val file = File(filesDir, filepath)
        try {
            if (!file.exists()) // Если файл по такому пути не существует
                file.createNewFile() // Создаем новый файл
            val fos = FileOutputStream(file)
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.e("FileNotFoundException", e.message!!)
        } catch (e: IOException) {
            Log.e("IOException", e.message!!)
        }
    }
}