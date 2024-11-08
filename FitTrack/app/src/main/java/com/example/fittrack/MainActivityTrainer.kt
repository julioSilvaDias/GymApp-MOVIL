package com.example.fittrack

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.fittrack.util.ThemeUtils
import com.google.firebase.firestore.FirebaseFirestore

class MainActivityTrainer : AppCompatActivity() {

    private lateinit var db:FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeUtils.setLocale(this, ThemeUtils.getLocale(this) ?: "en")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrenador)

        db = FirebaseFirestore.getInstance()

        ThemeUtils.applyBackground(this, "workouts")
        ThemeUtils.applyTextTheme(this)

        findViewById<Button>(R.id.buttonUpdateFromTrainer).setOnClickListener {
            updateWorkouts()
        }

        findViewById<Button>(R.id.buttonAddFromTrainer).setOnClickListener {
            addWorkouts()
        }

        findViewById<Button>(R.id.buttonEliminateFromTrainer).setOnClickListener {
            eliminateWorkouts()
        }

        findViewById<Button>(R.id.buttonBackFromTrainer).setOnClickListener {
            finish()
        }
    }

    private fun updateWorkouts(){

    }

    private fun addWorkouts(){

    }

    private fun eliminateWorkouts(){

    }

}