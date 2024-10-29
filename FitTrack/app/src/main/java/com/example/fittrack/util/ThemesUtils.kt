package com.example.fittrack.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fittrack.R

object ThemeUtils {

    private val backgrounds = mapOf(
        "login" to arrayOf(R.drawable.fondologin, R.drawable.loginlight),
        "register" to arrayOf(R.drawable.fondoregister, R.drawable.registerlight),
        "workouts" to arrayOf(R.drawable.fondoworkouts, R.drawable.workoutslight),
        "profile" to arrayOf(R.drawable.fondoprofile, R.drawable.profilelight)
    )

    fun applyBackground(activity: AppCompatActivity, activityName: String) {
        val sharedPreferences = activity.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
        val backgroundIndex = sharedPreferences.getInt("currentBackgroundIndex", 0)
        val backgroundsArray = backgrounds[activityName]
        backgroundsArray?.let {
            activity.findViewById<ConstraintLayout>(R.id.rootlayout)?.setBackgroundResource(it[backgroundIndex])
        }
    }

    fun toggleTheme(activity: AppCompatActivity, activityName: String, isLightMode: Boolean) {
        val backgroundIndex = if (isLightMode) 1 else 0
        val sharedPreferences = activity.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("currentBackgroundIndex", backgroundIndex).apply()
        applyBackground(activity, activityName) // Aplica el nuevo fondo
    }
}
