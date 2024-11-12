package com.example.fittrack

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fittrack.util.ThemeUtils
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivityWorkoutInfo : AppCompatActivity() {
    private lateinit var adapter: ExercisesAdapter
    private lateinit var exercisesList: ArrayList<Exercise>

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtils.setLocale(this, ThemeUtils.getLocale(this) ?: "en")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_workout_info)

        ThemeUtils.applyBackground(this, "trainer")
        ThemeUtils.applyTextTheme(this)
        val selectedWorkout = intent.getSerializableExtra("workoutItem") as? Workout
        val videoUrl = intent.getStringExtra("url")

        val listView = findViewById<ListView>(R.id.listExercises)
        exercisesList = ArrayList()
        adapter = ExercisesAdapter(this, exercisesList)
        listView.adapter = adapter
        if (selectedWorkout != null) {
            getExercises(selectedWorkout.id)
        }


        val name = findViewById<TextView>(R.id.nameWorkout)
        val level = findViewById<TextView>(R.id.levelWorkout)
        val exercises = findViewById<TextView>(R.id.exercisesWorkout)

        findViewById<Button>(R.id.buttonBack).setOnClickListener {
            val intent = Intent(this, MainActivityTrainer::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.buttonUpdate).setOnClickListener {
            if (selectedWorkout != null) {
                if (name.text != selectedWorkout.name || level.text != selectedWorkout.level.toString() ||
                    exercises.text != selectedWorkout.exercises.toString()) {
                    update(
                        selectedWorkout.id,
                        name.text.toString(),
                        level.text.toString(),
                        exercises.text.toString()
                    )
                    val intent = Intent(this, MainActivityTrainer::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "No changes realized", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<Button>(R.id.buttonDelete).setOnClickListener {
            if (selectedWorkout != null) {
                delete(selectedWorkout.id)
                val intent = Intent(this, MainActivityTrainer::class.java)
                startActivity(intent)
                finish()
            }
        }

        if (selectedWorkout != null) {
            name.text = selectedWorkout.name
            level.text = selectedWorkout.level.toString()
            exercises.text = selectedWorkout.exercises.toString()
        }
        val videoId = extractVideoId(videoUrl)
        if (videoId != null) {
            val thumbnailUrl = "https://img.youtube.com/vi/$videoId/0.jpg"
            val imageView = findViewById<ImageView>(R.id.url)
            Glide.with(this).load(thumbnailUrl).into(imageView)

            imageView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                startActivity(intent)
            }
        }
    }

    private fun getExercises(id: String) {
        val db = Firebase.firestore

        val workoutRef = db.collection("Workouts").document(id)
        workoutRef.collection("Exercises")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val exercise = Exercise()
                    exercise.exerciseName = document.getString("name").toString()
                    exercise.exerciseDescription = document.getString("description").toString()
                    exercisesList.add(exercise)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Database error. \n No history found", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun update(
        id: String,
        name: String,
        level: String,
        exercises: String
    ) {
        val db = Firebase.firestore
        db.collection("Workouts").document(id)
            .update("name", name, "level", level.toInt(), "exercises", exercises.toInt())
            .addOnSuccessListener { Toast.makeText(this, "Updated workout", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show() }
    }

    private fun delete(id: String) {
        val db = Firebase.firestore
        db.collection("Workouts").document(id)
            .delete()
            .addOnSuccessListener { Toast.makeText(this, "Workout deleted", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show() }
    }

    private fun extractVideoId(videoUrl: String?): String? {
        return videoUrl?.let {
            val regex =
                "v=([^&]+)".toRegex()
            val matchResult = regex.find(it)
            matchResult?.groups?.get(1)?.value
        }
    }
}