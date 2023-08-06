package ru.agpu.artikproject.background_work

import android.content.ContentValues
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite.Companion.getSqliteDatabase
import java.util.concurrent.TimeUnit

class GetDirectionsList {
    val directionsFromDatabase: ArrayList<List<String>>
        get() {
            val r = getSqliteDatabase(null).rawQuery("SELECT * FROM directions_list", null)
            val directionsList = ArrayList<List<String>>()
            if (r.count != 0) {
                while (r.moveToNext()) {
                    val list: MutableList<String> = ArrayList()
                    list.add(r.getString(0))
                    list.add(r.getString(1))
                    directionsList.add(list)
                }
            } else {
                directionsFromFirebase
                try {
                    TimeUnit.MILLISECONDS.sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            return directionsList
        }
    private val directionsFromFirebase: Unit
        get() {
            val databaseReference = FirebaseDatabase.getInstance()
            val myRef = databaseReference.getReference("Directions")
            myRef.orderByChild("group_name").addChildEventListener(object : ChildEventListener {
                // При использовании этих функций не пострадал ни один ребенок
                override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                    if (dataSnapshot.value != null) {
                        val rowValues = ContentValues() // Значения для вставки в базу данных
                        rowValues.put("direction_name", dataSnapshot.value.toString().split("group_name=")[1].split(",")[0])
                        rowValues.put("group_name", dataSnapshot.value.toString().split("direction_name=")[1].replace("}", ""))
                        getSqliteDatabase(null).insert("directions_list", null, rowValues)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
        }
}
