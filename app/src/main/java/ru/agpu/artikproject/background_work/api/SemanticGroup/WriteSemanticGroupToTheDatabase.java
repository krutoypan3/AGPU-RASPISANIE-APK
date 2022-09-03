package ru.agpu.artikproject.background_work.api.SemanticGroup;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.agpu.artikproject.background_work.datebase.DataBase_Local;

public class WriteSemanticGroupToTheDatabase {
    /**
     * Перезаписывает список групп и факультетов в базу данных
     *
     * @param context            Контекст приложения
     * @param semanticGroupItems Список групп и факультетов
     */
    public WriteSemanticGroupToTheDatabase(Context context, ArrayList<SemanticGroupItem> semanticGroupItems) {
        try (SQLiteDatabase sqLiteDatabase = new DataBase_Local(context).getWritableDatabase()) {
            sqLiteDatabase.delete(DataBase_Local.TABLE_NAME_SEMANTIC_GROUP, "", null);
            for (SemanticGroupItem item : semanticGroupItems) {
                ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                rowValues.put("id", item.Id);
                rowValues.put("name", item.Name);
                rowValues.put("group_id", item.groupId);
                rowValues.put("group_is_archive", item.groupIsArchive);
                rowValues.put("group_number_of_students", item.groupNumberOfStudents);
                rowValues.put("group_name", item.groupName);
                rowValues.put("group_is_raspis", item.groupIsRaspis);
                sqLiteDatabase.insert(DataBase_Local.TABLE_NAME_SEMANTIC_GROUP, null, rowValues);
            }
        }
    }
}
