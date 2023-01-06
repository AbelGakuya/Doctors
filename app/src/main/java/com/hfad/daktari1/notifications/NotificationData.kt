package com.hfad.daktari1.notifications

import com.hfad.daktari1.roomdatabase.DoctorData

data class NotificationData (
    val title: String,
    val message: String,
    val docName:String?,
    val date: String?,
    val startTime: String?,
    val endTime: String?
        )