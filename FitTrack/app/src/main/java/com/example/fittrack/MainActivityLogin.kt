package com.example.fittrack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.security.MessageDigest

class MainActivityLogin : AppCompatActivity() {

    private lateinit var db:FirebaseFirestore

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

        loadSavedCredentials()

        findViewById<Button>(R.id.buttonRegister).setOnClickListener {

            val intent = Intent(applicationContext, MainActivityRegister::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.buttonLogin).setOnClickListener {

            val user = userEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (user.isNotEmpty() && password.isNotEmpty()){
                loginUser(user, password)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(user : String, password : String){

        db.collection("Users").document(user).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val storedPassword = document.getString("password")
                    if (storedPassword == password){
                        if (rememberMeCheckBox.isChecked){
                            saveCredentials(user, password)
                        } else {
                            clearCredentials()
                        }
                        val intent = Intent(applicationContext, MainActivityWorkouts::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error General", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveCredentials(user:String, password: String){
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", user)
        editor.putString("password", password)
        editor.putBoolean("rememberMe", true)
        editor.apply()
    }

    private fun loadSavedCredentials(){
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        val password = sharedPreferences.getString("password", "")
        val rememberMe = sharedPreferences.getBoolean("rememberMe", false)

        userEditText.setText(username)
        passwordEditText.setText(password)
        rememberMeCheckBox.isChecked = rememberMe
    }

    private fun clearCredentials(){
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("username")
        editor.remove("password")
        editor.putBoolean("rememberMe", false)
        editor.apply()
    }

}