package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.fittrack.util.ThemeUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class MainActivityWorkouts : AppCompatActivity() {

    private lateinit var adapter: AdapterList
    private lateinit var historicList: ArrayList<Historic>
    private lateinit var filteredList: ArrayList<Historic>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workouts)

       ThemeUtils.applyBackground(this, "login")
       ThemeUtils.applyTextTheme(this)

        val userId = intent.getStringExtra("id").toString()
        val username = intent.getStringExtra("username").toString()
        val textInput = findViewById<TextInputEditText>(R.id.seeker)
        val button = findViewById<Button>(R.id.button3)

        getUserType(username) { isUser ->
            if (isUser) {
                button.visibility = View.VISIBLE
            } else {
                button.visibility = View.INVISIBLE
            }
        }

        val listView = findViewById<ListView>(R.id.historicList)
        historicList = ArrayList()
        filteredList = ArrayList()
        adapter = AdapterList(this, filteredList)
        listView.adapter = adapter

        getHistoric(userId)

        textInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(i: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(i: Editable?) {
                filterList(i.toString())
            }
        })

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedHistoric = historicList[position]

            getVideo(selectedHistoric) { urlVideo ->
                val intent = Intent(this, MainActivityWorkoutsInfo::class.java)
                intent.putExtra("historicItem", selectedHistoric)
                intent.putExtra("url", urlVideo)
                startActivity(intent)

            }

        }

        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityProfile::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button5).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityLogin::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.button3).setOnClickListener {
            val intent = Intent()
            intent.putExtra("username", username)
            intent.putExtra("userid", userId)
        }


    }

    private fun filterList(level: String) {
        filteredList.clear()

        if (level.isEmpty()) {
            filteredList.addAll(historicList)
        } else {
            filteredList.addAll(historicList.filter { it.level == level })
        }

        adapter.notifyDataSetChanged()
    }

    private fun getHistoric(id: String) {
        val db = FirebaseFirestore.getInstance()

        val userRef = db.collection("Users").document(id)

        userRef.collection("Historic")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val historic = document.toObject(Historic::class.java)
                    historicList.add(historic)
                }
                filteredList.addAll(historicList)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Database error. \n No history found", Toast.LENGTH_SHORT)
                    .show()

            }
    }

    private fun getVideo(selectedHistoric: Historic, callback: (String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val workoutName = selectedHistoric.nameWorkout

        db.collection("Workouts")
            .whereEqualTo("name", workoutName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val videoUrl = document.getString("URL")
                        callback(videoUrl ?: "")
                    }
                } else {
                    callback("")
                }
            }
            .addOnFailureListener { exception ->
                callback("")
            }
    }

    private fun getUserType(username: String, callback: (Boolean) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                var isTrainer = false
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val type = document.getString("userType")
                        if (type.equals("Trainer")) {
                            isTrainer = true
                            break
                        }
                    }
                }
                callback(isTrainer)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
}
