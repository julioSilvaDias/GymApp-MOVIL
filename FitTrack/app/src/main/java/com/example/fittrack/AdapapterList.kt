package com.example.fittrack

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AdapterList(context: Context, private val dataSource: ArrayList<Historic>) : ArrayAdapter<Historic>(context, 0, dataSource) {

    private class ViewHolder {
        lateinit var name: TextView
        lateinit var level: TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_historic, parent, false)
            viewHolder = ViewHolder()
            viewHolder.name = view.findViewById(R.id.name)
            viewHolder.level = view.findViewById(R.id.level)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val historic = getItem(position)

        historic?.let {
            viewHolder.name.text = it.nameWorkout
            viewHolder.level.text = "Level: " + it.level
        }

        return view
    }
}
