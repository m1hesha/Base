package com.example.base

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val students = mutableListOf<Student>()
        for (i in 1..10) {
            val name = ('A'..'Z').random().toString() + ('a'..'z').random().toString()
            val height = Random.nextDouble(1.5, 2.0) // meters
            val weight = Random.nextDouble(50.0, 100.0) // kg
            val age = Random.nextInt(18, 25)
            students.add(Student(name, height, weight, age))
        }

        val sortedStudents = students.sortedBy { it.age }
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)

        sortedStudents.forEach { student ->
            val tableRow = TableRow(this)
            val nameTextView = TextView(this)
            nameTextView.text = student.name
            val heightTextView = TextView(this)
            heightTextView.text = "%.2f".format(student.height)
            val weightTextView = TextView(this)
            weightTextView.text = "%.2f".format(student.weight)
            val ageTextView = TextView(this)
            ageTextView.text = student.age.toString()

            tableRow.addView(nameTextView)
            tableRow.addView(heightTextView)
            tableRow.addView(weightTextView)
            tableRow.addView(ageTextView)

            val layoutParams = TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            tableRow.layoutParams = layoutParams
            tableLayout.addView(tableRow)
        }
    }
}

data class Student(
    val name: String,
    val height: Double,
    val weight: Double,
    val age: Int
)