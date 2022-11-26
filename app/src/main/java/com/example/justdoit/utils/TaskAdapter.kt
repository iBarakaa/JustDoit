package com.example.justdoit.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.justdoit.databinding.EachTaskItemBinding

class TaskAdapter(private  val list: MutableList<TaskData>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    private var listener: TaskAdapterClicksInterface? = null
    fun setListener(listener : TaskAdapterClicksInterface) {
        this.listener = listener
    }

    inner class TaskViewHolder(val binding: EachTaskItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = EachTaskItemBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        with(holder){
            with(list[position]) {
                binding.doTask.text = this.task

                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteTaskBtnClicked(this)
                }

                binding.editTask.setOnClickListener {
                    listener?.onEditTaskBtnClicked(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface TaskAdapterClicksInterface {
        fun onDeleteTaskBtnClicked (taskData: TaskData)
        fun onEditTaskBtnClicked (taskData: TaskData)
    }

}