package com.hfad.daktari1

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.hfad.daktari1.agora.MeetingDetailsActivity
import com.hfad.daktari1.databinding.ActivityOrderBinding
import com.hfad.daktari1.notifications.NotificationData
import com.hfad.daktari1.notifications.PushNotification
import com.hfad.daktari1.notifications.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        val view = binding.root

        val token2 = intent.getStringExtra("client_token")
        val title = intent.getStringExtra("title")
        val name = intent.getStringExtra("name")
        val docName = intent.getStringExtra("docName")

        val date = intent.getStringExtra("date")
        val startTime = intent.getStringExtra("startTime")
        val endTime = intent.getStringExtra("endTime")

        binding.txtRequest.text = "Apointment request by $name"
        binding.txtMessage.text = "On $date from $startTime to $endTime"




        binding.btnAccept.setOnClickListener {


            val title = "Appointment ACCEPTED"
            val message = "Online appointment on $date from $startTime to $endTime"

            if (message.isNotEmpty()){
                PushNotification(
                    NotificationData(title, message,docName
                        ,date,startTime,endTime),
                    token2
                ).also {
                    sendNotification(it)
                }
            }

            Toast.makeText(this, "Sent to $docName", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MeetingDetailsActivity::class.java)
            startActivity(intent)


        }

        binding.btnDecline.setOnClickListener {
            val title = "Appointment DECLINED"
            val message = "Please choose another doctor"

            if (message.isNotEmpty()){
                PushNotification(
                    NotificationData(title, message,docName,date,startTime,endTime),
                    token2
                ).also {
                    sendNotification(it)
                }
            }
        }

        setContentView(view)
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful){
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody().toString())
            }

        }catch (e: Exception){
            Log.e(TAG, e.toString())
        }

    }
}