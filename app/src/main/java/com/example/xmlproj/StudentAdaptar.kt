package com.example.xmlproj

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.xmlproj.Student

class StudentAdapter(
    private val context: Context,
    private val studentList: List<Student>
) : BaseAdapter() {

    override fun getCount(): Int = studentList.size

    override fun getItem(position: Int): Any = studentList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context)
                .inflate(R.layout.item_student, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val student = studentList[position]
        holder.textName.text = student.name
        holder.textGrade.text = "Grade: ${student.grade}"
        holder.textEmail.text = student.email

        return view
    }

    private class ViewHolder(view: View) {
        val textName: TextView = view.findViewById(R.id.textStudentName)
        val textGrade: TextView = view.findViewById(R.id.textStudentGrade)
        val textEmail: TextView = view.findViewById(R.id.textStudentEmail)
    }
}