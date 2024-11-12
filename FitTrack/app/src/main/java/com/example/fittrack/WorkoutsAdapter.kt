package com.example.fittrack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class WorkoutsAdapter(context: Context, dataSource: ArrayList<Workout>) :
    ArrayAdapter<Workout>(context, 0, dataSource) {

    private class ViewHolder {
        lateinit var name: TextView
        lateinit var level: TextView
        lateinit var exercises: TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_workout, parent, false)
            viewHolder = ViewHolder()
            viewHolder.name = view.findViewById(R.id.name)
            viewHolder.level = view.findViewById(R.id.level)
            viewHolder.exercises = view.findViewById(R.id.exercises)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val workout = getItem(position)
        workout?.let {
            if (it.level.toInt() == 0) {
                viewHolder.name.text = it.name
                viewHolder.level.text = buildString {
                    append("Level: Learner")
                }
                viewHolder.exercises.text = buildString {
                    append("Exercises: ")
                    append(it.exercises.toString())
                }
            } else if (it.level.toInt() == 1) {
                viewHolder.name.text = it.name
                viewHolder.level.text = buildString {
                    append("Level: Beginner")
                }
                viewHolder.exercises.text = buildString {
                    append("Exercises: ")
                    append(it.exercises.toString())
                }
            } else if (it.level.toInt() == 2) {
                viewHolder.name.text = it.name
                viewHolder.level.text = buildString {
                    append("Level: Intermediate")
                }
                viewHolder.exercises.text = buildString {
                    append("Exercises: ")
                    append(it.exercises.toString())
                }
            } else {
                viewHolder.name.text = it.name
                viewHolder.level.text = buildString {
                    append("Level: Advanced")
                }
                viewHolder.exercises.text = buildString {
                    append("Exercises: ")
                    append(it.exercises.toString())
                }
            }
        }
        return view
    }
}
