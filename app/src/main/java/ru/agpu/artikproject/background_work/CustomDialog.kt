package ru.agpu.artikproject.background_work

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.RaspUpdateRepository
import ru.agpu.artikproject.background_work.datebase.Raspisanie
import ru.agpu.artikproject.background_work.datebase.RaspisanieRepository
import ru.agpu.artikproject.background_work.main_show.ListViewGroupListener.Companion.position
import ru.agpu.artikproject.background_work.main_show.WatchSaveGroupRasp
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.ShowBuildingsOnTheMap
import ru.agpu.artikproject.background_work.rasp_show.ParaInfo.Companion.finalCorp
import ru.agpu.artikproject.presentation.layout.MainActivity

/**
 * Кастомный диалог
 * @param act Активити
 * @param newMainText Заголовок
 * @param newBodyText Тело
 * @param uri Ссылка на скачивание
 * @param dialogType Тип диалога
 */
class CustomDialog(
    private val act: Activity,
    private val dialogType: CustomDialogType,
    private val newMainText: String? = null,
    private val newBodyText: String? = null,
    private val uri: Uri? = null,
): Dialog(act), View.OnClickListener {
    var paraInfoPhotoIV: ImageView? = null
    var listViewLV: ListView? = null
    var mainTextTV: TextView? = null
    var bodyTextTV: TextView? = null
    var yesBtn: Button? = null
    var noBtn: Button? = null


    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog)
        mainTextTV = findViewById(R.id.custom_dialog_main_text)
        bodyTextTV = findViewById(R.id.custom_dialog_body_text)
        bodyTextTV?.movementMethod = ScrollingMovementMethod()
        val editTextTV = findViewById<EditText>(R.id.custom_dialog_edit_text)
        listViewLV = findViewById(R.id.custom_dialog_list_view)
        paraInfoPhotoIV = findViewById(R.id.para_info_photo)
        yesBtn = findViewById(R.id.btn_alert_dialog_yes)
        noBtn = findViewById(R.id.btn_alert_dialog_no)
        when (dialogType) {
            CustomDialogType.UPDATE -> {
                mainTextTV?.text = newMainText
                bodyTextTV?.text = newBodyText
                yesBtn?.setText(R.string.Update)
            }

            CustomDialogType.DELETE -> {
                mainTextTV?.setText(R.string.delete_all_save)
                bodyTextTV?.setText(R.string.delete_confirm)
                yesBtn?.setText(R.string.Delete_everything)
            }

            CustomDialogType.PARA_PASHA -> {
                mainTextTV?.setText(R.string.pasha_god)
                bodyTextTV?.visibility = View.INVISIBLE
                yesBtn?.visibility = View.INVISIBLE
                listViewLV?.visibility = View.VISIBLE
                paraInfoPhotoIV?.visibility = View.VISIBLE
                val params = paraInfoPhotoIV?.layoutParams // получаем параметры
                params?.height = DIALOG_PHOTO_HEIGHT
                params?.width = ViewGroup.LayoutParams.WRAP_CONTENT
                paraInfoPhotoIV?.layoutParams = params
            }

            CustomDialogType.PARA_INFO -> {
                mainTextTV?.setText(R.string.para_info)
                bodyTextTV?.visibility = View.INVISIBLE
                yesBtn?.visibility = View.INVISIBLE
                listViewLV?.visibility = View.VISIBLE
            }

            CustomDialogType.MAP_CONFIRM -> {
                mainTextTV?.setText(R.string.map_confirm)
                bodyTextTV?.setText(R.string.redirect_to_map)
                yesBtn?.setText(R.string.go)
            }

            CustomDialogType.DELETE_SAVED_GROUP -> {
                mainTextTV?.setText(R.string.delete_one_save)
                bodyTextTV?.setText(R.string.delete_confirm)
                yesBtn?.setText(R.string.Delete)
            }

            CustomDialogType.ABOUT -> {
                mainTextTV?.setText(R.string.app_info)
                bodyTextTV?.setText(R.string.app_info_body)
                yesBtn?.visibility = View.INVISIBLE
                noBtn?.visibility = View.INVISIBLE
            }

            CustomDialogType.RICARDO_PASHA -> {
                mainTextTV?.setText(R.string.rickardo_show)
                bodyTextTV?.visibility = View.INVISIBLE
                paraInfoPhotoIV?.visibility = View.VISIBLE
                noBtn?.visibility = View.INVISIBLE
                yesBtn?.setText(R.string.god_of_flex)
                val params = paraInfoPhotoIV?.layoutParams // получаем параметры
                params?.height = DIALOG_PHOTO_HEIGHT
                params?.width = ViewGroup.LayoutParams.WRAP_CONTENT
                paraInfoPhotoIV?.layoutParams = params
            }
        }
        // Так как у некоторых View 0 воспринимается как wrap_content (например EditText),
        // Было принято решение ставить 1 вместо 0.
        if (paraInfoPhotoIV?.visibility == View.INVISIBLE) // Если фотка в диалоге пустая
            paraInfoPhotoIV?.layoutParams?.height = 1 // Скрываем фото
        if (bodyTextTV?.visibility == View.INVISIBLE) { // Если текст в теле пустой
            bodyTextTV?.layoutParams?.height = 1 // Скрываем текст
            bodyTextTV?.setPadding(0, 0, 0, 0) // Убираем отступы
        }
        if (listViewLV?.visibility == View.INVISIBLE) // Если список пуст
            listViewLV?.layoutParams?.height = 1 // Убираем список
        if (editTextTV.visibility == View.INVISIBLE) // Если ввод текста пуст
            editTextTV.layoutParams.height = 1 // Убираем ввод текста
        if (noBtn?.visibility == View.INVISIBLE) { // Если кнопка отмены пуста
            noBtn?.layoutParams?.height = 1 // Убираем кнопку отмены
            noBtn?.layoutParams?.width = 1
        } else { // Если кнопка отмены не пуста
            noBtn?.setOnClickListener(this) // Прослушиваем нажатия на кнопку отмены
        }
        if (yesBtn?.visibility == View.INVISIBLE) { // Если кнопка принятия пуста
            yesBtn?.layoutParams?.height = 1 // Убираем кнопку принятия
            yesBtn?.layoutParams?.width = 1
        } else  // Если кнопка принятия не пуста
            yesBtn?.setOnClickListener(this) // Прослушиваем кнопку принятия
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_alert_dialog_yes -> when (dialogType) {
                CustomDialogType.UPDATE -> act.startActivity(Intent(Intent.ACTION_VIEW, uri))
                CustomDialogType.DELETE -> {
                    val raspisanieRepository = RaspisanieRepository()
                    raspisanieRepository.deleteAll()
                    val raspUpdateRepository = RaspUpdateRepository()
                    raspUpdateRepository.deleteAll()
                    MainActivity.groupListed = null
                    WatchSaveGroupRasp(act, null)
                }
                CustomDialogType.MAP_CONFIRM -> ShowBuildingsOnTheMap(finalCorp, act)
                CustomDialogType.DELETE_SAVED_GROUP -> {
                    val raspisanieRepository = RaspisanieRepository()
                    raspisanieRepository.deletePara(
                        Raspisanie(
                            groupCode = MainActivity.groupListedId[position].toIntOrNull(),
                            searchType = MainActivity.groupListedType[position],
                        )
                    )
                    WatchSaveGroupRasp(act, null) // Первичный вывод групп которые были открыты ранее
                }
                else -> {}
            }

            R.id.btn_alert_dialog_no -> dismiss()
            else -> {}
        }
        dismiss()
    }

    companion object {
        private const val DIALOG_PHOTO_HEIGHT = 700
    }
}