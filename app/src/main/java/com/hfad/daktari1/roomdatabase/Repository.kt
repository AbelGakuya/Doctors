package com.hfad.daktari1.roomdatabase

import androidx.lifecycle.LiveData

class Repository(private val availabilityDao: AvailabilityDao) {


    val readAllData: LiveData<List<Availability>> = availabilityDao.getAll()

    suspend fun addAvailability(availability: Availability){
        availabilityDao.insert(availability)
    }

    suspend fun deleteAvailability(availability: Availability){
        availabilityDao.delete(availability)
    }

}