package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivityRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        findViewById<Button>(R.id.buttonVolver).setOnClickListener {

            val intent = Intent(applicationContext, MainActivityLogin::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.buttonRegister).setOnClickListener {

            val intent = Intent(applicationContext, MainActivityLogin::class.java)
            startActivity(intent)
            finish()
        }
    }
}