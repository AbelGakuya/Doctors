package com.hfad.daktari1.roomdatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AvailabilityViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Availability>>
    private val repository: Repository

    init {
        val availabilityDao = AvailabilityDatabase.getInstance(application).availabilityDao()
        repository = Repository(availabilityDao)
        readAllData = repository.readAllData
    }




  /*  var newDate = ""
    var startTime = ""
    var endTime = ""

    val availability = suspend {
        dao.getAll()
    }*/

    fun addAvailability(availability:Availability){

        viewModelScope.launch(Dispatchers.IO) {
            repository.addAvailability(availability)
        }
    }

    fun deleteAvailability(availability: Availability){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAvailability(availability)
        }
    }

}