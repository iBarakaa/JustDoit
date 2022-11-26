package com.example.justdoit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.justdoit.R
import com.example.justdoit.databinding.FragmentAddTaskPopUpBinding
import com.example.justdoit.utils.TaskData
import com.google.android.material.textfield.TextInputEditText


class AddTaskPopUpFragment : DialogFragment() {

    private lateinit var binding: FragmentAddTaskPopUpBinding
    private lateinit var listener: DialogNextBtnClickListener
    private var taskData: TaskData? = null

    fun setListener(listener: DialogNextBtnClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "AddTaskPopUpFragment"

        @JvmStatic
        fun newInstance(taskId: String, task: String) = AddTaskPopUpFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddTaskPopUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            taskData = TaskData(
                arguments?.getString("taskId").toString(),
                arguments?.getString("task").toString()
            )

            binding.taskEt.setText(taskData?.task)
        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.taskNextBtn.setOnClickListener {
            val doTask = binding.taskEt.text.toString()
            if (doTask.isNotEmpty()) {
                if(taskData == null) {
                    listener.onSaveTask(doTask, binding.taskEt)
                }else {
                    taskData?.task = doTask
                    listener.onUpdateTask(taskData!! , binding.taskEt)
                }

            } else {
                Toast.makeText(context, "Just Do It ;)", Toast.LENGTH_SHORT).show()
            }
        }

        binding.taskClose.setOnClickListener {
            dismiss()
        }
    }

    interface DialogNextBtnClickListener {
        fun onSaveTask(doTask: String, taskEt: TextInputEditText)
        fun onUpdateTask(taskData: TaskData, taskEt: TextInputEditText)

    }
}