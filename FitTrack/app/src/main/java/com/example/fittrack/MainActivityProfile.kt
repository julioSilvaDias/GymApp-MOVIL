package com.example.fittrack

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fittrack.util.ThemeUtils
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class MainActivityProfile : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var textUser: TextView
    private lateinit var textName: TextView
    private lateinit var textSurname: TextView
    private lateinit var textEmail: TextView
    private lateinit var textBirthdate: TextView
    private lateinit var buttonBack : Button
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switchMode: Switch
    private lateinit var spinnerChangeLanguage: Spinner
    private lateinit var rootLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        ThemeUtils.setLocale(this, ThemeUtils.getLocale(this)?: "en")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        db = FirebaseFirestore.getInstance()

        textUser = findViewById(R.id.textViewUsuario)
        textName = findViewById(R.id.textViewNombre)
        textSurname = findViewById(R.id.textViewApellido)
        textEmail = findViewById(R.id.textViewEmail)
        textBirthdate = findViewById(R.id.textViewFechaNacimiento)
        buttonBack = findViewById(R.id.buttonVolverPerfil)
        switchMode = findViewById(R.id.switch1)
        spinnerChangeLanguage = findViewById(R.id.spinnerChangeLanguage)
        rootLayout = findViewById(R.id.rootlayout)

        ThemeUtils.applyBackground(this, "profile")

        val isLightMode = getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
            .getInt("currentBackgroundIndex", 0) == 1
        switchMode.isChecked = isLightMode

        switchMode.setOnCheckedChangeListener { _, isChecked ->

            ThemeUtils.toggleTheme(this, "profile", isChecked)
            ThemeUtils.applyButtonTheme(this, buttonBack)
            ThemeUtils.applyTextTheme(this)
        }

        val username = intent.getStringExtra("username")

        if (username != null) {
            getAllData(username)
        } else {
            Toast.makeText(this, "username not found", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.buttonVolverPerfil).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityWorkouts::class.java)
            startActivity(intent)
            finish()
        }

        setUpSpinner()
    }

    private fun getAllData(username: String) {
        db.collection("Users").whereEqualTo("username", username).get()
            .addOnSuccessListener { documents ->

                if (!documents.isEmpty) {
                    val document = documents.first()
                    val userName = document.getString("name") ?: ""
                    val userSurname = document.getString("surname") ?: ""
                    val userEmail = document.getString("email") ?: ""
                    val userBirthdate = document.getString("birthdate") ?: ""

                    textUser.text = username
                    textName.text = userName
                    textSurname.text = userSurname
                    textEmail.text = userEmail
                    textBirthdate.text = userBirthdate
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener {
                Toast.makeText(this, "Error General", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setUpSpinner(){
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.changeLanguage,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerChangeLanguage.adapter = adapter

        spinnerChangeLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLanguage = if (position == 0) "en" else "es"
                val currentLanguage = Locale.getDefault().language
                if (selectedLanguage != currentLanguage) {
                    ThemeUtils.setLocale(this@MainActivityProfile, selectedLanguage)
                    recreate()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

}