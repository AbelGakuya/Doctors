package com.hfad.daktari1.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "availability_table")
data class Availability(

    @PrimaryKey(autoGenerate = true)
    var availabilityIdR: Long = 0,

    @ColumnInfo(name = "availabilityId")
    var availabilityId: Int = 0,

    @ColumnInfo(name = "date")
    var date: String = "",

    @ColumnInfo(name = "start_time")
    var startTime: String = "",

    @ColumnInfo(name = "end_time")
    var endTime: String = ""
)