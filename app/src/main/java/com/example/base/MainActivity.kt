package com.example.base

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var db: SQLiteDatabase

    @SuppressLint("Range")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Создаем базу данных и таблицу, если они не существуют
        val dbHelper = DatabaseHelper(this)
        db = dbHelper.writableDatabase
        createTable()

        // Читаем имена студентов из файла и добавляем их в базу данных
        val names = resources.assets.open("names.txt").bufferedReader().readLines()
        val random = Random()
        for (name in names) {
            val values = ContentValues().apply {
                put("name", name)
                put("age", random.nextInt(25) + 18) // возраст от 18 до 42 лет
                put("weight", random.nextInt(80) + 50) // вес от 50 до 129 кг
                put("height", random.nextInt(40) + 150) // рост от 150 до 189 см
            }
            db.insert("students", null, values)
        }


        // Получаем список студентов из базы данных, отсортированный по возрасту
        val limit = "20"
        val cursor = db.query("students", null, null, null, null, null, "age", limit)
        val students = mutableListOf<Student>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val age = cursor.getInt(cursor.getColumnIndex("age"))
            val weight = cursor.getInt(cursor.getColumnIndex("weight"))
            val height = cursor.getInt(cursor.getColumnIndex("height"))
            students.add(Student(name, age, weight, height))
        }
        cursor.close()

        // Выводим список студентов в таблицу
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
        val headerView = LayoutInflater.from(this).inflate(R.layout.table_header, null)
        tableLayout.addView(headerView)
        for (student in students) {
            val row = LayoutInflater.from(this).inflate(R.layout.table_row, null) as TableRow
            row.findViewById<TextView>(R.id.nameTextView).text = student.name
            row.findViewById<TextView>(R.id.ageTextView).text = student.age.toString()
            row.findViewById<TextView>(R.id.weightTextView).text = student.weight.toString()
            row.findViewById<TextView>(R.id.heightTextView).text = student.height.toString()
            tableLayout.addView(row)
        }
    }

    private fun createTable() {
        db.execSQL("CREATE TABLE IF NOT EXISTS students (name TEXT, age INTEGER, weight INTEGER, height INTEGER)")
    }

    override fun onDestroy() {
        super.onDestroy()
        db.close()
    }

    // Вспомогательный класс для работы с базой данных
    private class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "students.db", null, 1) {

        override fun onCreate(db: SQLiteDatabase) {}

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    }

    // Класс для представления студента
    private data class Student(val name: String, val age: Int, val weight: Int, val height: Int)
}
