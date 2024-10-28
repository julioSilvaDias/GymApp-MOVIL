package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivityProfile : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var textUser:TextView
    private lateinit var textName:TextView
    private lateinit var textSurname:TextView
    private lateinit var textEmail:TextView
    private lateinit var textBirthdate:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        db = FirebaseFirestore.getInstance()

        textUser = findViewById(R.id.textViewUsuario)
        textName = findViewById(R.id.textViewNombre)
        textSurname = findViewById(R.id.textViewApellido)
        textEmail = findViewById(R.id.textViewEmail)
        textBirthdate = findViewById(R.id.textViewFechaNacimiento)

        val userId = intent.getStringExtra("id")

        if (userId != null){
            getAllData(userId)
        } else {
            Toast.makeText(this, "ID not found", Toast.LENGTH_SHORT).show()
        }
//        val userId = intent.getStringExtra("userId")
//        if (userId != null) {
//            getAllData(userId)  // Llamar a la función con el ID
//        } else {
//            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
//        }
//
//        findViewById<Button>(R.id.buttonVolverPerfil).setOnClickListener {
//            val intent = Intent(applicationContext, MainActivityWorkouts::class.java)
//            startActivity(intent)
//            finish()
//        }
        findViewById<Button>(R.id.buttonVolverPerfil).setOnClickListener {

            val intent = Intent(applicationContext, MainActivityWorkouts::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun getAllData(userId : String) {
        db.collection("Users").document(userId).get()
            .addOnSuccessListener { document ->

                if (!document.exists()) {
                    val username = document.getString("username")?: ""
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
}