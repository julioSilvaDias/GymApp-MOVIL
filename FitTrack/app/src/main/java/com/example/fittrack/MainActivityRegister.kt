package com.example.fittrack

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivityRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        setupSpinnerBasic()

        val db = Firebase.firestore

        findViewById<Button>(R.id.buttonVolver).setOnClickListener {

            val intent = Intent(applicationContext, MainActivityLogin::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.buttonRegister).setOnClickListener {

            val nombre = findViewById<EditText>(R.id.textViewIngresarNombreRegistro).text.toString()
            val apellido = findViewById<EditText>(R.id.textViewIngresarApellidoRegistro).text.toString()
            val email = findViewById<EditText>(R.id.textViewIngresarEmailRegistro).text.toString()
            val fechaNacimiento = findViewById<EditText>(R.id.textViewIngresarFechaNacimientoRegistro).text.toString()
            val usuario = findViewById<EditText>(R.id.textViewIngresarUsuarioRegistro).text.toString()
            val password = findViewById<EditText>(R.id.textViewIngresarPasswordRegistro).text.toString()

            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() ||
                fechaNacimiento.isEmpty() || usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = hashMapOf(
                "name" to nombre,
                "surname" to apellido,
                "email" to email,
                "birthdate" to fechaNacimiento,
                "username" to usuario,
                "password" to password,
                "userType" to findViewById<Spinner>(R.id.spinner).selectedItem.toString()
            )
            db.collection("Users").document("004")
                .set(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "You have successfully registered", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    Log.w(TAG, "Error adding document", e)
                }
            val intent = Intent(applicationContext, MainActivityLogin::class.java)
            intent.putExtra("login", findViewById<EditText>(R.id.textViewIngresarUsuarioRegistro).text.toString())
            intent.putExtra("password", findViewById<EditText>(R.id.textViewIngresarPasswordRegistro).text.toString())
            startActivity(intent)
            finish()
        }
    }

    private fun setupSpinnerBasic() {
        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.userType,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}