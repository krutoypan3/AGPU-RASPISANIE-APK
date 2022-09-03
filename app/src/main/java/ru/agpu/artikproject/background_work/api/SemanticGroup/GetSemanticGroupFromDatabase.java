package ru.agpu.artikproject.background_work.api.SemanticGroup;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.agpu.artikproject.background_work.datebase.DataBase_Local;

public class GetSemanticGroupFromDatabase {
    public ArrayList<SemanticGroupItem> get(Context context) {
        try (SQLiteDatabase sqLiteDatabase = new DataBase_Local(context).getWritableDatabase()) {
            Cursor r = sqLiteDatabase.rawQuery(
                    "SELECT * FROM " + DataBase_Local.TABLE_NAME_SEMANTIC_GROUP, null
            );
            ArrayList<SemanticGroupItem> semanticGroupItems = new ArrayList<>();
            while (r.moveToNext()) {
                SemanticGroupItem item = new SemanticGroupItem(
                        r.getString(0),
                        r.getString(1),
                        r.getString(2),
                        r.getString(3),
                        r.getString(4),
                        r.getString(5),
                        r.getString(6)
                );
                semanticGroupItems.add(item);
            }
            return semanticGroupItems;
        }
    }
}
