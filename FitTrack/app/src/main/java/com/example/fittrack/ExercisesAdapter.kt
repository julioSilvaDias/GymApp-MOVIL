package com.example.fittrack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ExercisesAdapter(context: Context, dataSource: ArrayList<Exercise>) :
    ArrayAdapter<Exercise>(context, 0, dataSource) {

    private class ViewHolder {
        lateinit var name: TextView
        lateinit var description: TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false)
            viewHolder = ViewHolder()
            viewHolder.name = view.findViewById(R.id.nameExercise)
            viewHolder.description = view.findViewById(R.id.descriptionExercise)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val exercise = getItem(position)
        exercise?.let {
            viewHolder.name.text = it.exerciseName
            viewHolder.description.text = it.exerciseDescription
        }
        return view
    }
}
