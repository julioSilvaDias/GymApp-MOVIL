package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivityAddWorkout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_workout)

        val db = Firebase.firestore

        findViewById<Button>(R.id.buttonCancel).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityTrainer::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.buttonAdd).setOnClickListener {
            val nameWorkoutAdd = findViewById<EditText>(R.id.nameWorkoutAdd).text.toString()
            val levelWorkoutAdd = findViewById<EditText>(R.id.levelWorkoutAdd).text.toString()
            val exercisesWorkoutAdd =
                findViewById<EditText>(R.id.exercisesWorkoutAdd).text.toString()
            val urlWorkoutAdd = findViewById<EditText>(R.id.urlWorkoutAdd).text.toString()

            if (nameWorkoutAdd.isEmpty() || levelWorkoutAdd.isEmpty() || exercisesWorkoutAdd.isEmpty() ||
                urlWorkoutAdd.isEmpty()
            ) {
                Toast.makeText(this, "Please, complete all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val workout = hashMapOf(
                "name" to nameWorkoutAdd,
                "level" to levelWorkoutAdd.toInt(),
                "exercises" to exercisesWorkoutAdd.toInt(),
                "URL" to urlWorkoutAdd,
            )

            var lastId: Int
            var newId = ""

            db.collection("Workouts").get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val id = document.id
                        lastId = id.toInt()
                        newId = "00" + (lastId + 1).toString()
                    }
                    db.collection("Workouts").document(newId)
                        .set(workout)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Workout added", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show()
                        }
                    val intent = Intent(applicationContext, MainActivityTrainer::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show()
                }
        }
    }
}