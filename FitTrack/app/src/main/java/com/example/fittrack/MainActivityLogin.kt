package com.example.fittrack

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivityLogin : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var userEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var rememberMeCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)

        db = FirebaseFirestore.getInstance()


        userEditText = findViewById(R.id.textViewIngresarUsuario)
        passwordEditText = findViewById(R.id.textViewIngresarPassword)
        rememberMeCheckBox = findViewById(R.id.checkBox_RememberMe)

        loadSavedData()

        findViewById<Button>(R.id.buttonRegister).setOnClickListener {

            val intent = Intent(applicationContext, MainActivityRegister::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.buttonLogin).setOnClickListener {

            val user = userEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (user.isNotEmpty() && password.isNotEmpty()) {
                loginUser(user, password)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun loginUser(username: String, password: String) {

        db.collection("Users").whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val storedName = document.getString("username")
                        val storedPassword = document.getString("password")
                        val storedId = document.id

                        if (storedPassword == password && storedName == username) {
                            if (rememberMeCheckBox.isChecked) {
                                saveData(username, password)
                            } else {
                                deleteSavedData()
                            }
                            Toast.makeText(this, "Successful login", Toast.LENGTH_SHORT).show()
                            val intent =
                                Intent(applicationContext, MainActivityWorkouts::class.java)
                            intent.putExtra("id", storedId)
                            startActivity(intent)
                            finish()
                            break
                        } else {
                            Toast.makeText(this, "Password Incorrect", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error General", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveData(user: String, password: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", user)
        editor.putString("password", password)
        editor.putBoolean("rememberMe", true)
        editor.apply()
    }

    private fun loadSavedData() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        val password = sharedPreferences.getString("password", "")
        val rememberMe = sharedPreferences.getBoolean("rememberMe", false)

        userEditText.setText(username)
        passwordEditText.setText(password)
        rememberMeCheckBox.isChecked = rememberMe
    }

    private fun deleteSavedData() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("username")
        editor.remove("password")
        editor.putBoolean("rememberMe", false)
        editor.apply()
    }

}