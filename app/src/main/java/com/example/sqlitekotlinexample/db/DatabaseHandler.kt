package com.example.sqlitekotlinexample.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.sqlitekotlinexample.models.Tasks
import java.util.*

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DatabaseHandler.DB_NAME, null, DatabaseHandler.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $NAME TEXT, $DESC TEXT,$COMPLETED TEXT, $PRIORITY TEXT, $DATE INTEGER)"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addTask(tasks: Tasks): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, tasks.name)
        values.put(DESC, tasks.desc)
        values.put(COMPLETED, tasks.completed)
        values.put(PRIORITY, tasks.priority)
        values.put(DATE, tasks.date)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedId", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun getTask(_id: Int): Tasks {
        val taskObj = Tasks()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        taskObj.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        taskObj.name = cursor.getString(cursor.getColumnIndex(NAME))
        taskObj.desc = cursor.getString(cursor.getColumnIndex(DESC))
        taskObj.completed = cursor.getString(cursor.getColumnIndex(COMPLETED))
        taskObj.priority = cursor.getString(cursor.getColumnIndex(PRIORITY))
        taskObj.date = cursor.getInt(cursor.getColumnIndex(DATE))
        cursor.close()
        db.close()
        return taskObj
    }

    fun getAllTasks(date: Int): List<Tasks> {
        val taskList = ArrayList<Tasks>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $DATE = $date"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val tasks = Tasks()
                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    tasks.name = cursor.getString(cursor.getColumnIndex(NAME))
                    tasks.desc = cursor.getString(cursor.getColumnIndex(DESC))
                    tasks.completed = cursor.getString(cursor.getColumnIndex(COMPLETED))
                    tasks.priority = cursor.getString(cursor.getColumnIndex(PRIORITY))
                    tasks.date = cursor.getInt(cursor.getColumnIndex(DATE))
                    taskList.add(tasks)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return taskList
    }

    fun updateTask(tasks: Tasks): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, tasks.name)
        values.put(DESC, tasks.desc)
        values.put(COMPLETED, tasks.completed)
        values.put(PRIORITY, tasks.priority)
        values.put(DATE, tasks.date)
        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(tasks.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteAllTasks(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    companion object {
        private val DB_VERSION = 2
        private val DB_NAME = "MyTasks"
        private val TABLE_NAME = "Tasks"
        private val ID = "Id"
        private val NAME = "Name"
        private val DESC = "Desc"
        private val COMPLETED = "Completed"
        private val PRIORITY = "Priority"
        private val DATE = "Date"
    }
}