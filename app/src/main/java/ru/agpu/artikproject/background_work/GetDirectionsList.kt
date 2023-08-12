package ru.agpu.artikproject.background_work

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import ru.agpu.artikproject.background_work.datebase.Directions
import ru.agpu.artikproject.background_work.datebase.DirectionsRepository
import java.util.concurrent.TimeUnit

class GetDirectionsList {
    val directionsFromDatabase: List<Directions>
        get() {
            val directionsRepository = DirectionsRepository()
            val directionsList = directionsRepository.getAll()
            if (directionsList.isEmpty()) {
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
                        val directionsRepository = DirectionsRepository()
                        directionsRepository.saveDirections(
                            Directions(
                                directionName = dataSnapshot.value.toString().split("group_name=")[1].split(",")[0],
                                groupName = dataSnapshot.value.toString().split("direction_name=")[1].replace("}", ""),
                            )
                        )
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
        }
}
