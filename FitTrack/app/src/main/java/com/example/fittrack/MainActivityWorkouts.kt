package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivityWorkouts : AppCompatActivity() {

    private lateinit var adapter: AdapterList
    private lateinit var historicList: ArrayList<Historic>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workouts)

        val userId = intent.getStringExtra("id")
        val username = intent.getStringExtra("username")
        var isUser = getUserType(username)

        val button = findViewById<Button>(R.id.button3)

       if(isUser) {
           button.visibility = View.VISIBLE
       }else{
           button.visibility = View.INVISIBLE
       }

        getHistoric(username)

        val listView = findViewById<ListView>(R.id.historicList)
        historicList = ArrayList()
        adapter = AdapterList(this, historicList)
        listView.adapter = adapter


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

    private fun getHistoric(id: String?) {
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

    private fun getUserType(username: String?): Boolean {
        var ret = false
        val db = FirebaseFirestore.getInstance()
        db.collection("Users")
            .whereEqualTo("username", "julio")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val type = document.getString("userType")
                        if (type.equals("trainer")) {
                            ret = true
                            break;
                        } else if(type.equals("user")){
                            ret = false
                            break
                        }
                    }
                }

            }
        return ret
    }
}
