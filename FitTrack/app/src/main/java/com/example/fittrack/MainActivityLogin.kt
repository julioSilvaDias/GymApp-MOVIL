package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivityLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)

        findViewById<Button>(R.id.buttonRegister).setOnClickListener {

            val intent = Intent(applicationContext, MainActivityRegister::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.buttonLogin).setOnClickListener {

            val intent = Intent(applicationContext, MainActivityWorkouts::class.java)
            startActivity(intent)
            finish()
        }

    }
}