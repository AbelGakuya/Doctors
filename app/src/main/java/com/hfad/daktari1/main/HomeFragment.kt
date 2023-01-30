package com.hfad.daktari1.main

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.adapters.CalendarViewBindingAdapter.setDate
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.hfad.daktari1.R
import com.hfad.daktari1.RegisterFragmentDirections
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

import com.hfad.daktari1.databinding.FragmentHomeBinding
import com.hfad.daktari1.firebase.AvailabilityF
import com.hfad.daktari1.roomdatabase.*
import kotlinx.coroutines.Job
import java.nio.file.Files.delete
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: AvailabilityAdapter
    private lateinit var availability:List<Availability>
    private lateinit var mViewModel: AvailabilityViewModel
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var uid: String? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        mAuth = FirebaseAuth.getInstance()
        uid = mAuth.currentUser?.uid
        //   val uid = mAuth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().
        getReference("doctors").child(uid!!).child("Availability")
        val date = binding.date

        date.setOnClickListener {
            setDate()
        }

        mViewModel = ViewModelProvider(this).get(AvailabilityViewModel::class.java)

        binding.saveDate.setOnClickListener {
            insertToDatabase()
        }

        adapter = AvailabilityAdapter()
        recyclerView = binding.availabilityList
        recyclerView.adapter = adapter

        //viewModel
        mViewModel.readAllData.observe(viewLifecycleOwner, Observer { availability ->
            adapter.setData(availability)

        })
        delete()
        return view
    }

    private fun insertToDatabase(){

        val date = binding.date.text.toString()
        val startTime = binding.startTime.text.toString()
        val endTime = binding.endTime.text.toString()
        val availabilityId = Random.nextInt()

        val availability = Availability(0,availabilityId, date, startTime, endTime)
        mViewModel.addAvailability(availability)


        //adding to Firebase
        val availabilityF = AvailabilityF(availabilityId, date, startTime, endTime)

        if (uid != null){
            databaseReference.child(availabilityId.toString()).
            setValue(availabilityF).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "Not added to firebase", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun setDate(){
        val datePicker = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { view : DatePicker?,
                                                        year: Int, month: Int, dayOfMonth : Int ->

            datePicker[Calendar.YEAR] = year
            datePicker[Calendar.MONTH] = month
            datePicker[Calendar.DAY_OF_MONTH] = dayOfMonth
            val dateFormat = "dd-MMMM-yyyy"
            val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
            binding.date.text = simpleDateFormat.format(datePicker.time)

        }

        DatePickerDialog(requireContext(), date, datePicker[Calendar.YEAR],
            datePicker[Calendar.MONTH], datePicker[Calendar.DAY_OF_MONTH]).show()
    }

    fun setData(availability: List<Availability>){
        this.availability = availability
    }






  fun delete(){
        adapter.setOnItemClickListener(object : AvailabilityAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                 mViewModel.readAllData.observe(viewLifecycleOwner, Observer { availability ->
        setData(availability)
    })

                val availability1 = availability[position]

                val id = availability1.availabilityId

                mViewModel.deleteAvailability(availability1)

                if (uid != null){
                    databaseReference.child(id.toString()).
                    removeValue().addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(requireContext(), "Successfully deleted", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(context, "Unsuccessful", Toast.LENGTH_SHORT).show()
                        }
                    }
                }




            }

        })
  }

}