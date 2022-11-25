package com.example.justdoit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.justdoit.R
import com.example.justdoit.databinding.FragmentHomeBinding
import com.example.justdoit.databinding.FragmentSignInBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeFragment : Fragment(), AddTaskPopUpFragment.DialogNextBtnClickListener {
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseRef : DatabaseReference
    private lateinit var navController : NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var popUpFragment: AddTaskPopUpFragment

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


}