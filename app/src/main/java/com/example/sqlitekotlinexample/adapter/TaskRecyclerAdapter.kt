package com.example.sqlitekotlinexample.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.sqlitekotlinexample.AddOrEditActivity

import com.example.sqlitekotlinexample.R
import com.example.sqlitekotlinexample.models.Tasks

import java.util.ArrayList

private const val TAG = "TaskRecyclerAdapter"
class TaskRecyclerAdapter(tasksList: List<Tasks>, internal var context: Context) : RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder>() {

    internal var tasksList: List<Tasks> = ArrayList()
    init {
        this.tasksList = tasksList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_tasks, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tasks = tasksList[position]
        holder.name.text = tasks.name
        holder.desc.text = tasks.desc
        Log.d(TAG, "onBindViewHolder: ${tasks.priority}")
        if(tasks.priority.isNotEmpty())
            holder.priority.text = tasks.priority.substring(0,1)
        when(tasks.priority){
            "high" -> holder.priority.setTextColor(context.getColor(R.color.highPriority))
            "medium" -> holder.priority.setTextColor(context.getColor(R.color.mediumPriority))
            "normal" -> holder.priority.setTextColor(context.getColor(R.color.normalPriority))
            "less" -> holder.priority.setTextColor(context.getColor(R.color.lessPriority))
            "no" -> holder.priority.setTextColor(context.getColor(R.color.noPriority))
        }
        if (tasks.completed == "Y")
            holder.list_item.background = ContextCompat.getDrawable(context, R.color.colorSuccess)
        else
            holder.list_item.background = ContextCompat.getDrawable(context, R.color.colorUnSuccess)

        holder.itemView.setOnClickListener {
            val i = Intent(context, AddOrEditActivity::class.java)
            i.putExtra("Mode", "Edit")
            i.putExtra("Id", tasks.id)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName) as TextView
        var desc: TextView = view.findViewById(R.id.tvDesc) as TextView
        var priority: TextView = view.findViewById(R.id.tvPriority) as TextView
        var list_item: LinearLayout = view.findViewById(R.id.list_item) as LinearLayout
    }
}
