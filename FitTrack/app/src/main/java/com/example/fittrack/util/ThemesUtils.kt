package com.example.fittrack.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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

    fun applyBackground(activity: AppCompatActivity, activityName: String){
        val sharedPreferences = activity.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
        val backgroundIndex = sharedPreferences.getInt("currentBackgroundIndex", 0)
        val backgroundsArray = backgrounds[activityName]
        backgroundsArray?.let {
            activity.findViewById<ConstraintLayout>(R.id.rootlayout)?.setBackgroundResource(it[backgroundIndex])
        }
    }

    fun applyButtonTheme(activity: AppCompatActivity, button: Button){
        val sharedPreferences = activity.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
        val isLightMode = sharedPreferences.getInt("currentBackgroundIndex", 0) == 1

        if (isLightMode) {
            button.setBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"))
            button.setTextColor(android.graphics.Color.BLACK)
        } else {
            button.setBackgroundColor(android.graphics.Color.parseColor("#716969"))
            button.setTextColor(android.graphics.Color.WHITE)
        }
    }

    fun applyTextTheme(activity: AppCompatActivity){
        val sharedPreferences = activity.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
        val isLightMode = sharedPreferences.getInt("currentBackgroundIndex", 0) == 1

        val rootView = activity.findViewById<View>(android.R.id.content)
        setTextColor(rootView, isLightMode)
    }

    fun toggleTheme(activity: AppCompatActivity, activityName: String, isLightMode: Boolean){
        val backgroundIndex = if (isLightMode) 1 else 0
        val sharedPreferences = activity.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("currentBackgroundIndex", backgroundIndex).apply()
        applyBackground(activity, activityName)
    }

    private fun setTextColor(view: View, isLightMode: Boolean) {
        if (view is TextView) {
            view.setTextColor(if (isLightMode) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount){
                setTextColor(view.getChildAt(i), isLightMode)
            }
        }
    }
}
