package ru.agpu.artikproject.background_work;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.agpu.artikproject.background_work.datebase.DataBase_Local;

public class GetDirectionsList {

    public ArrayList<List<String>> getDirectionsFromDatabase(){
        Cursor r = DataBase_Local.sqLiteDatabase.rawQuery("SELECT * FROM directions_list", null);
        ArrayList<List<String>> DirectionsList = new ArrayList<>();
        if (!(r.getCount() == 0)) {
            while (r.moveToNext()){
            List<String> list = new ArrayList<>();
            list.add(r.getString(0));
            list.add(r.getString(1));
            DirectionsList.add(list);
            }
        }
        return DirectionsList;
    }

    public void getDirectionsFromFirebase(){
        FirebaseDatabase databaseReference = FirebaseDatabase.getInstance();
        DatabaseReference myRef = databaseReference.getReference("Directions");

        myRef.orderByChild("group_name").addChildEventListener(new ChildEventListener() {
            // При использовании этих функций не пострадал ни один ребенок
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
                if  (dataSnapshot.getValue() != null) {
                    ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                    rowValues.put("direction_name", dataSnapshot.getValue().toString().split("group_name=")[1].split(",")[0]);
                    rowValues.put("group_name", dataSnapshot.getValue().toString().split("direction_name=")[1].replace("}", ""));
                    DataBase_Local.sqLiteDatabase.insert("directions_list", null, rowValues);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
