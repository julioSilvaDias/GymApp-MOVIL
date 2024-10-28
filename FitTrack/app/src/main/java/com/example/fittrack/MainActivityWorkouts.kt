package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivityWorkouts : AppCompatActivity() {

    private lateinit var adapter: AdapterList
    private lateinit var historicList: ArrayList<Historic>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workouts)

        val listView = findViewById<ListView>(R.id.historicList)
        historicList = ArrayList()
        adapter = AdapterList(this, historicList)
        listView.adapter = adapter

        getHistoric()

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
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.button5).setOnClickListener {
            val intent = Intent(applicationContext, MainActivityLogin::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun getHistoric() {
        val db = FirebaseFirestore.getInstance()
        val userId = "001"
        val userRef = db.collection("Users").document(userId)

        userRef.collection("Historic")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val historic = document.toObject(Historic::class.java)
                    historicList.add(historic)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->

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
}
