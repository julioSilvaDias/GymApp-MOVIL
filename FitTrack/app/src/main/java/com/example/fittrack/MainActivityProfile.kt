package com.example.fittrack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fittrack.util.ThemeUtils
import com.google.firebase.firestore.FirebaseFirestore

class MainActivityProfile : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var textUser:TextView
    private lateinit var textName:TextView
    private lateinit var textSurname:TextView
    private lateinit var textEmail:TextView
    private lateinit var textBirthdate:TextView
    private lateinit var switchMode: Switch
    private lateinit var rootLayout : ConstraintLayout

    private val backgrounds = arrayOf(
        R.drawable.fondologin,
        R.drawable.loginlight,
        R.drawable.fondoregister,
        R.drawable.registerlight,
        R.drawable.fondoworkouts,
        R.drawable.workoutslight,
        R.drawable.fondoprofile,
        R.drawable.profilelight
    )

    private var currentBackgroundIndex = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        db = FirebaseFirestore.getInstance()

        val sharedPreferences = getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
        currentBackgroundIndex = sharedPreferences.getInt("currentBackgroundIndex", 0)

        textUser = findViewById(R.id.textViewUsuario)
        textName = findViewById(R.id.textViewNombre)
        textSurname = findViewById(R.id.textViewApellido)
        textEmail = findViewById(R.id.textViewEmail)
        textBirthdate = findViewById(R.id.textViewFechaNacimiento)
        switchMode = findViewById(R.id.switch1)
        rootLayout = findViewById(R.id.rootlayout)

        ThemeUtils.applyBackground(this, "profile")

        switchMode.isChecked = currentBackgroundIndex ==1

        switchMode.setOnCheckedChangeListener{_, isChecked ->

            currentBackgroundIndex = if (isChecked) 1 else 0
            updateBackground()

            sharedPreferences.edit().putInt("currentBackgroundIndex", currentBackgroundIndex).apply()
        }
        val username= intent.getStringExtra("username")

        if (username != null){
            getAllData(username)
        } else {
            Toast.makeText(this, "username not found", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.buttonVolverPerfil).setOnClickListener {

            val intent = Intent(applicationContext, MainActivityWorkouts::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getAllData(username : String) {
        db.collection("Users").whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->

                if (!documents.isEmpty) {
                    val document = documents.first()
                    val userName = document.getString("name")?: ""
                    val userSurname = document.getString("surname")?: ""
                    val userEmail = document.getString("email")?: ""
                    val userBirthdate = document.getString("birthdate")?: ""

                    textUser.text = username
                    textName.text = userName
                    textSurname.text = userSurname
                    textEmail.text = userEmail
                    textBirthdate.text = userBirthdate
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error General", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateBackground(){
        rootLayout.setBackgroundResource(backgrounds[currentBackgroundIndex])
    }
}