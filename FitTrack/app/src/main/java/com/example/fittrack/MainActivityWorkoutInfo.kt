package com.example.fittrack

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fittrack.util.ThemeUtils

class MainActivityWorkoutInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtils.setLocale(this, ThemeUtils.getLocale(this) ?: "en")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_workout_info)

        ThemeUtils.applyBackground(this, "trainer")
        ThemeUtils.applyTextTheme(this)

        findViewById<Button>(R.id.buttonBack).setOnClickListener {
            finish()
        }

        val selectedWorkout = intent.getSerializableExtra("workoutItem") as? Workout
        val videoUrl = intent.getStringExtra("url")

        if (selectedWorkout != null) {
            findViewById<TextView>(R.id.nameWorkout).text = selectedWorkout.name
            findViewById<TextView>(R.id.levelWorkout).text = selectedWorkout.level.toString()
            findViewById<TextView>(R.id.exercisesWorkout).text = selectedWorkout.exercises.toString()
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

    private fun extractVideoId(videoUrl: String?): String? {
        return videoUrl?.let {
            val regex =
                "v=([^&]+)".toRegex()
            val matchResult = regex.find(it)
            matchResult?.groups?.get(1)?.value
        }
    }
}