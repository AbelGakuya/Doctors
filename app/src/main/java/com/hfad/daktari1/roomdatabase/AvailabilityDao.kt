package com.hfad.daktari1.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AvailabilityDao {

    @Insert
    suspend fun insert(availability: Availability)

    @Update
    suspend fun update(availability: Availability)

    @Delete
    suspend fun delete(availability: Availability)

    @Query("SELECT * FROM availability_table WHERE availabilityId = :availabilityId")
    fun get(availabilityId: Long) : LiveData<Availability>

    @Query("SELECT * FROM availability_table ORDER BY availabilityId DESC")
    fun getAll(): LiveData<List<Availability>>


}