package com.example.sqlitekotlinexample

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import com.example.sqlitekotlinexample.adapter.TaskRecyclerAdapter
import com.example.sqlitekotlinexample.db.DatabaseHandler
import com.example.sqlitekotlinexample.models.Tasks
import java.util.Date

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    var taskRecyclerAdapter: TaskRecyclerAdapter? = null;
    var fab: FloatingActionButton? = null
    var recyclerView: RecyclerView? = null
    var datePicker: DatePicker? = null
    var button: Button? = null
    var dbHandler: DatabaseHandler? = null
    var listTasks: List<Tasks> = ArrayList<Tasks>()
    var linearLayoutManager: LinearLayoutManager? = null
    var date = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) +1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dateString = if(day<10 && month<10){
            StringBuilder().append(year).append(0).append(month).append(0).append(day).toString()
        } else if(day<10){
            StringBuilder().append(year).append(month).append(0).append(day).toString()
        } else if(month<10){
            StringBuilder().append(year).append(0).append(month).append(day).toString()
        } else{
            StringBuilder().append(year).append(month).append(day).toString()
        }

        date = dateString.toInt()

        Log.d(TAG, "onCreate: $year.$month.$day")
        
        initViews()
        initOperations()
    }

    fun initDB(date: Int) {
        Log.d(TAG, "initDB: data: $date")
        dbHandler = DatabaseHandler(this)
        listTasks = (dbHandler as DatabaseHandler).getAllTasks(date)
        taskRecyclerAdapter = TaskRecyclerAdapter(tasksList = listTasks, context = applicationContext)
        (recyclerView as RecyclerView).adapter = taskRecyclerAdapter
    }

    fun initViews() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        fab = findViewById<FloatingActionButton>(R.id.fab)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        datePicker = findViewById(R.id.date_picker)
        button = findViewById(R.id.ok)
        Log.d(TAG, "initViews: listtasks:  ${listTasks.size}")
        taskRecyclerAdapter = TaskRecyclerAdapter(tasksList = listTasks, context = applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
    }

    fun initOperations() {
        fab?.setOnClickListener { view ->
            val i = Intent(applicationContext, AddOrEditActivity::class.java)
            i.putExtra("Mode", "A")
            startActivity(i)
        }

        button?.setOnClickListener {
            Log.d(TAG, "ok button called")
            val year = datePicker?.year
            val month = datePicker?.month?.plus(1)
            val day = datePicker?.dayOfMonth

            val dateString = if(day!! <10 && month!! <10){
                StringBuilder().append(year).append(0).append(month).append(0).append(day).toString()
            } else if(day<10){
                StringBuilder().append(year).append(month).append(0).append(day).toString()
            } else if(month!! <10){
                StringBuilder().append(year).append(0).append(month).append(day).toString()
            } else{
                StringBuilder().append(year).append(month).append(day).toString()
            }

            date = dateString.toInt()
            initDB(date)
            initViews()
            datePicker!!.visibility = View.GONE
            button!!.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.action_calender){
            datePicker!!.visibility = View.VISIBLE
            button!!.visibility = View.VISIBLE
        }
        if (id == R.id.action_delete) {
            val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Click 'YES' Delete All Tasks")
                    .setPositiveButton("YES") { dialog, i ->
                        dbHandler!!.deleteAllTasks()
                        initDB(date)
                        dialog.dismiss()
                    }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
            dialog.show()
            return true
        }
        if (id == R.id.action_add){
            val i = Intent(applicationContext, AddOrEditActivity::class.java)
            i.putExtra("Mode", "A")
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        initDB(date)
    }
}
