package id.ac.umn.studentapp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class StudentViewModel : ViewModel() {
    private val db = Firebase.firestore
    var students by mutableStateOf(listOf<Student>())
        private set
    init {
        fetchStudents()
    }
    fun addStudent(student: Student) {
        val studentMap = hashMapOf(
            "id" to student.id,
            "name" to student.name,
            "program" to student.program
        )
        db.collection("students")
            .add(studentMap)
            .addOnSuccessListener {
                Log.d("Firestore"
                    ,
                    "DocumentSnapshot added with ID: ${it.id}")
                fetchStudents() // Refresh list
            }
            .addOnFailureListener { e ->
                Log.w("Firestore"
                    ,
                    "Error adding document"
                    , e)
            }
    }
    private fun fetchStudents() {
        db.collection("students")
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<Student>()
                for (document in result) {
                    val id = document.getString("id") ?: ""
                    val name = document.getString("name") ?: ""
                    val program = document.getString("program") ?: ""
                    list.add(Student(id, name, program))
                }
                students = list
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents.", exception)
            }
    }
}