package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fittrack.util.ThemeUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class MainActivityTrainer : AppCompatActivity() {
    private lateinit var adapter: WorkoutsAdapter
    private lateinit var workoutsList: ArrayList<Workout>
    private lateinit var filteredList: ArrayList<Workout>
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtils.setLocale(this, ThemeUtils.getLocale(this) ?: "en")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrenador)
        ThemeUtils.applyBackground(this, "trainer")
        ThemeUtils.applyTextTheme(this)

        val textInput = findViewById<TextInputEditText>(R.id.textInputLayout)
        val listView = findViewById<ListView>(R.id.listView2)
        workoutsList = ArrayList()
        filteredList = ArrayList()
        adapter = WorkoutsAdapter(this, filteredList)
        listView.adapter = adapter
        getWorkouts()

        textInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(i: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(i: Editable?) {
                filterList(i.toString())
            }
        })

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedWorkout = workoutsList[position]

            getVideo(selectedWorkout) { urlVideo ->
                val intent = Intent(this, MainActivityWorkoutInfo::class.java)
                intent.putExtra("workoutItem", selectedWorkout)
                intent.putExtra("url", urlVideo)
                startActivity(intent)
                finish()
            }
        }

        findViewById<Button>(R.id.buttonAddFromTrainer).setOnClickListener {
            val intent = Intent(this, MainActivityAddWorkout::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.buttonBackFromTrainer).setOnClickListener {
            finish()
        }
    }

    private fun getWorkouts() {
        val db = Firebase.firestore

        db.collection("Workouts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val workout = document.toObject(Workout::class.java)
                    workout.id = document.id
                    workoutsList.add(workout)
                }
                filteredList.addAll(workoutsList)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Database error. \n No history found", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun getVideo(selectedWorkout: Workout, callback: (String) -> Unit) {
        val db = Firebase.firestore
        val name = selectedWorkout.name

        db.collection("Workouts")
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val videoUrl = document.getString("URL")
                        callback(videoUrl ?: "")
                    }
                } else {
                    callback("")
                }
            }
            .addOnFailureListener {
                callback("")
                Toast.makeText(this, "Database error. \n No history found", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun filterList(level: String) {
        filteredList.clear()

        if (level.isEmpty()) {
            filteredList.addAll(workoutsList)
        } else if (level == "Learner") {
            filteredList.addAll(workoutsList.filter { it.level.toInt() == 0 })
        } else if ( level == "Beginner") {
            filteredList.addAll(workoutsList.filter { it.level.toInt() == 1 })
        } else if ( level == "Intermediate") {
            filteredList.addAll(workoutsList.filter { it.level.toInt() == 2 })
        } else if ( level == "Advanced") {
            filteredList.addAll(workoutsList.filter { it.level.toInt() == 3 })
        }

        adapter.notifyDataSetChanged()
    }
}