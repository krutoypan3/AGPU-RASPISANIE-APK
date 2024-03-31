package ru.agpu.artikproject.presentation.layout.fragment

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CustomDialog
import ru.agpu.artikproject.background_work.CustomDialogType
import ru.agpu.artikproject.background_work.GetDirectionsList
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_GROUP
import ru.agpu.artikproject.background_work.textDetranslit
import ru.agpu.artikproject.presentation.layout.activity.StartActivity


/**
 * Фрагмент выбора направления подготовки
 */
class SelectTrainingFragment: Fragment(R.layout.fragment_start_activity_select_training) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StartActivity.FRAGMENT = BACK_TO_GROUP

        val listView = view.findViewById<ListView>(R.id.list_training)
        val trainingNameET = view.findViewById<EditText>(R.id.training_name)
        val sortedDirections = ArrayList<ListViewItems>()
        val finalDirectionsList = GetDirectionsList().directionsFromDatabase

        // Отслеживаем изменения текстового поля
        trainingNameET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val searchDirection = trainingNameET.text.toString().trim().lowercase()
                sortedDirections.clear() // Очищаем ранее сформированный список
                if (searchDirection.isNotBlank()) { // Если строка поиска не пустая
                    for (currentDirections in finalDirectionsList) { // Проходимся по всему списку с направлениями
                        if (currentDirections.directionName?.lowercase()?.contains(searchDirection) == true) { // Если в направлении есть строка поиска
                            // То добавляем её в новый отсортированный массив
                            sortedDirections.add(ListViewItems(
                            "${view.context.getString(R.string.Group)}: ${currentDirections.groupName}\n${currentDirections.directionName}"
                            ))
                        }
                    }
                }
                // После сортировки выводим новый отсортированный список
                listView.adapter = ListViewAdapter(view.context, sortedDirections)
                if (listView.count == 0) // Если список пуст - делаем цвет текстового поля красным
                    trainingNameET.setTextColor(view.context.getColor(R.color.error))
                else  // Иначе - возвращаем цвет в исходное состояние
                    trainingNameET.setTextColor(view.context.getColor(R.color.black))
            }
        })

        // Прослушиваем нажатия на направления в списке
        listView.onItemClickListener =
            OnItemClickListener { _: AdapterView<*>?, _: View, i: Int, _: Long ->
                // Выводим новый всплывающий диалог
                val dialogConfirm = CustomDialog(context as Activity, CustomDialogType.UPDATE)
                dialogConfirm.window!!.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
                dialogConfirm.show()

                // Ставим в главный текст название группы
                dialogConfirm.mainTextTV?.text = sortedDirections[i].item.split("\n")[0]
                dialogConfirm.bodyTextTV?.text = view.context.getString(R.string.if_understand_group_name)
                dialogConfirm.yesBtn?.text = view.context.getString(R.string.go)
                dialogConfirm.noBtn?.text = view.context.getString(R.string.Back)

                // Устанавливаем слушатель нажатий на кнопку перехода к группе
                dialogConfirm.yesBtn?.setOnClickListener {
                    dialogConfirm.dismiss() // Закрываем диалог

                    // Удаляем первую букву Z и S (Удаляем сокращенки и заочки
                    // [Вместо SZВМ-ИВТ-3-1 будет показан список: ВМ-ИВТ-3-1, ZВМ-ИВТ-3-1, SZВМ-ИВТ-3-1],
                    // это нужно потому что группа может быть как SZВМ так и ZSВМ (короче - человеческий фактор (криворукие не могут определиться в каком порядке ставить буквы))
                    // Устанавливаем выбранную группу в основное активити фрагмента
                    StartActivity.SELECTED_GROUP = sortedDirections[i].item
                            .split("\n")[0]
                            .split((view.context.getString(R.string.Group) + ": "))[1]
                            .uppercase()
                            .replaceFirst("Z", "")
                            .replaceFirst("S", "")
                            .textDetranslit()

                    // Открываем фрагмент с выбором группы
                    parentFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_container_view, SelectGroupFragment::class.java, null)
                        .commit()
                }
            }

        // Прослушка нажатий на кнопку помощи с группой
        val helpGroupBtn = view.findViewById<View>(R.id.help_group)
        helpGroupBtn.setOnClickListener {
            helpGroupBtn.isClickable = false // Отключаем кнопку после нажатия
            helpGroupBtn.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.scale))
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, GroupHelpFragment::class.java, null)
                .commit() // Переходим к фрагменты с помощью с группой
        }
    }
}