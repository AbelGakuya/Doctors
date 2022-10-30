package com.hfad.daktari1.main

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

import com.hfad.daktari1.databinding.FragmentHomeBinding
import com.hfad.daktari1.roomdatabase.*
import kotlinx.coroutines.Job
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: AvailabilityAdapter
    private lateinit var availability:List<Availability>
    private lateinit var mViewModel: AvailabilityViewModel

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root


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


     /*   val application = requireNotNull(this.activity).application
        val dao = AvailabilityDatabase.getInstance(application).availabilityDao
        val viewModelFactory = AvailabilityViewModelFactory(dao)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(AvailabilityViewModel::class.java)


        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        adapter = AvailabilityAdapter(requireActivity())
        binding.availabilityList.adapter = adapter


       /* availability = viewModel.availability.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })*/

        availability = viewModel.availability
        adapter.data = availability*/



        return view
    }

    private fun insertToDatabase(){

        val date = binding.date.text.toString()
        val startTime = binding.startTime.text.toString()
        val endTime = binding.endTime.text.toString()

        val availability = Availability(0, date, startTime, endTime)

        mViewModel.addAvailability(availability)
        Toast.makeText(requireContext(), "Successfully added", Toast.LENGTH_SHORT).show()

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


                mViewModel.deleteAvailability(availability1)
            }

        })
  }

}