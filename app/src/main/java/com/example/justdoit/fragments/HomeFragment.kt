package com.example.justdoit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justdoit.R
import com.example.justdoit.databinding.FragmentHomeBinding
import com.example.justdoit.databinding.FragmentSignInBinding
import com.example.justdoit.utils.TaskAdapter
import com.example.justdoit.utils.TaskData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment(), AddTaskPopUpFragment.DialogNextBtnClickListener,
    TaskAdapter.TaskAdapterClicksInterface {

    private lateinit var auth : FirebaseAuth
    private lateinit var databaseRef : DatabaseReference
    private lateinit var navController : NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var popUpFragment: AddTaskPopUpFragment
    private lateinit var adapter: TaskAdapter
    private lateinit var mList:MutableList<TaskData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        getDataFromFirebase()
        registerEvents()
    }

    private fun registerEvents() {
        binding.addBtnHome.setOnClickListener {
            popUpFragment = AddTaskPopUpFragment()
            popUpFragment.setListener(this)
            popUpFragment.show(
                childFragmentManager,
                "AddTaskPopUpFragment"
            )
        }
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Tasks")
            .child(auth.currentUser?.uid.toString())

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = TaskAdapter(mList)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromFirebase() {
        databaseRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (taskSnapshot in snapshot.children) {
                    val doTask = taskSnapshot.key?.let {    // if key is not null its added to taskData class
                        TaskData(it , taskSnapshot.value.toString())
                    }

                    if(doTask != null) {
                        mList.add(doTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context , error.message , Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onSaveTask(doTask: String, taskEt: TextInputEditText) {

        databaseRef.push().setValue(doTask).addOnCompleteListener{
            if(it.isSuccessful) {
                Toast.makeText(context , "Task saved successfully" , Toast.LENGTH_SHORT).show()
                taskEt.text =null // ensures fresh added text for addition of other tasks

            }else {
                Toast.makeText(context , it.exception?.message , Toast.LENGTH_SHORT).show()
            }
            popUpFragment.dismiss()
        }
    }

    override fun onDeleteTaskBtnClicked(taskData: TaskData) {
        //databaseRef.child(TaskData.tId).removeValue().addOnCompleteListener()
    }

    override fun onEditTaskBtnClicked(taskData: TaskData) {
        TODO("Not yet implemented")
    }


}