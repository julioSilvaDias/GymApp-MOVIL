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

class MainActivityWorkoutsInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeUtils.setLocale(this, ThemeUtils.getLocale(this) ?: "en")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_workouts_info)

        ThemeUtils.applyBackground(this, "workouts")
        ThemeUtils.applyTextTheme(this)

        findViewById<Button>(R.id.button5).setOnClickListener {
            finish()
        }

        val selectedHistoric = intent.getSerializableExtra("historicItem") as? Historic
        val videoUrl = intent.getStringExtra("url")


        if (selectedHistoric != null) {

            findViewById<TextView>(R.id.workoutName).text = selectedHistoric.nameWorkout
            findViewById<TextView>(R.id.workoutNivel).text = selectedHistoric.level
            findViewById<TextView>(R.id.totalTIme).text = selectedHistoric.totalTime
            findViewById<TextView>(R.id.expectedTime).text = selectedHistoric.expectedTime
            findViewById<TextView>(R.id.date).text = selectedHistoric.date.toString()
            findViewById<TextView>(R.id.exerciseCompleted).text =
                selectedHistoric.completedExercises

        }
        val videoId = extractVideoId(videoUrl)
        if (videoId != null) {
            val thumbnailUrl = "https://img.youtube.com/vi/$videoId/0.jpg"
            val imageView = findViewById<ImageView>(R.id.imageYt)
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