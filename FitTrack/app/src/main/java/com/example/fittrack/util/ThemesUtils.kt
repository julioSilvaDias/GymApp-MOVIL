package com.example.fittrack.utils

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
        if (backgroundsArray != null) {
            activity.findViewById<ConstraintLayout>(R.id.rootlayout)?.setBackgroundResource(backgroundsArray[backgroundIndex])
        }
    }
}

//object ThemeUtils {
//
//    fun applyBackground(activity: Activity, activityType: String) {
//        val sharedPreferences = activity.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
//        val isLightMode = sharedPreferences.getInt("currentBackgroundIndex", 0) == 1
//
//        val backgroundResource = when (activityType) {
//            "login" -> if (isLightMode) R.drawable.loginlight else R.drawable.fondologin
//            "register" -> if (isLightMode) R.drawable.registerlight else R.drawable.fondoregister
//            "workouts" -> if (isLightMode) R.drawable.workoutslight else R.drawable.fondoworkouts
//            "profile" -> if (isLightMode) R.drawable.profilelight else R.drawable.fondoprofile
//            else -> R.drawable.default_background // Fallback por si acaso
//        }
//
//        val rootLayout = activity.findViewById<ConstraintLayout>(R.id.rootlayout)
//        rootLayout.setBackgroundResource(backgroundResource)
//    }
