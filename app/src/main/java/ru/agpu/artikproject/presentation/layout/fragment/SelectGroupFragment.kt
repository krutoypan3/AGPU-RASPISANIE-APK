package ru.agpu.artikproject.presentation.layout.fragment

import android.app.Activity
import android.content.Intent
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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_GROUP
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_SELECTED_ITEM
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_SELECTED_ITEM_ID
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_SELECTED_ITEM_TYPE
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_START_RASP
import ru.agpu.artikproject.background_work.textDetranslit
import ru.agpu.artikproject.presentation.layout.activity.MainActivity
import ru.agpu.artikproject.presentation.layout.activity.StartActivity
import ru.oganesyanartem.core.data.repository.groups_list.GroupsListImpl
import ru.oganesyanartem.core.domain.models.GroupsListItem
import ru.oganesyanartem.core.domain.usecase.groups_list.GroupsListGetUseCase

/**
 * Экран выбора группы
 */
class SelectGroupFragment: Fragment(R.layout.fragment_start_activity_select_group) {
    var groupsListItems: List<GroupsListItem?> = emptyList()
    private var disposable: Disposable? = null
    override fun onDetach() {
        super.onDetach()
        disposable?.dispose()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        StartActivity.FRAGMENT = BACK_TO_GROUP
        val act = view.context as Activity
        val listView = view.findViewById<ListView>(R.id.list_groups)
        val groupNameET = view.findViewById<EditText>(R.id.group_name)

        // Прослушка нажатий на кнопку помощи с группой
        val helpGroupBtn = view.findViewById<View>(R.id.help_group)
        helpGroupBtn.setOnClickListener {
            helpGroupBtn.isClickable = false // Отключаем кнопку после нажатия
            helpGroupBtn.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.scale))
            parentFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container_view, GroupHelpFragment::class.java, null)
                .commit() // Переходим к фрагменты с помощью с группой
        }

        disposable = Single.just(GroupsListGetUseCase(GroupsListImpl(view.context)).execute())
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                groupsListItems = list
            }

        // Прослушиваем изменения текстового поля ввода группы
        groupNameET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val searchGroup = groupNameET.text.toString().trim().lowercase().textDetranslit()
                groupsName.clear() // Очищаем списки с ранее отсортированными результатами
                groupsId.clear() // Так же очищаем отсортированные id
                if (searchGroup.isNotBlank()) { // Если строка поиска не пустая
                    for (i in groupsListItems.indices) {
                        // Сравниваем "детранслированный" текст сохраненных групп с "детранслированным" текстом строки поиска
                        val currentGroup = groupsListItems[i]?.groupName
                            ?.lowercase()
                            ?.textDetranslit()
                        if (currentGroup?.contains(searchGroup) == true) {
                            groupsId.add(groupsListItems[i]?.groupId
                                ?: "-1") // Добавляем ID группы в отсортированный массив
                            groupsName.add(ListViewItems(groupsListItems[i]?.groupName
                                ?: ".·´¯`(>▂<)´¯`·. ")) // Добавляем название группы в отсортированный массив
                        }
                    }
                }

                // После сортировки применяем адаптер с отсортированными группами
                listView.adapter = ListViewAdapter(view.context, groupsName)
                if (listView.count == 0) // Если групп не было найдено
                // Делаем цвет строки поиска красным
                    groupNameET.setTextColor(view.context.getColor(R.color.error))
                else  // Возвращаем цвет строки поиска в исходное состояние
                    groupNameET.setTextColor(view.context.getColor(R.color.black))
            }
        })
        groupNameET.setText(StartActivity.SELECTED_GROUP) // Устанавливаем ранее выбранную группу в поле ввода (если таковая есть)
        // Прослушиваем нажатия на группы
        listView.onItemClickListener = OnItemClickListener { _: AdapterView<*>?, _: View?, i: Int, _: Long ->
                // При нажатии на группу - открывается расписание с выбранной группой
                val intent = Intent(act.applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra(PREF_START_RASP, true)
                intent.putExtra(PREF_SELECTED_ITEM_ID, groupsId[i])
                intent.putExtra(PREF_SELECTED_ITEM_TYPE, "Group")
                intent.putExtra(PREF_SELECTED_ITEM, groupsName[i].item)
                act.startActivity(intent)
            }
    }

    companion object {
        var groupsName = ArrayList<ListViewItems>() // Отсортированный список с названиями групп
        var groupsId = ArrayList<String>() // Отсортированный список с id групп
    }
}