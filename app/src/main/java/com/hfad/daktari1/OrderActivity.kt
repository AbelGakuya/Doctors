package com.hfad.daktari1

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.gson.Gson
import com.hfad.daktari1.agora.MeetingDetailsActivity
import com.hfad.daktari1.agora.token
import com.hfad.daktari1.databinding.ActivityOrderBinding
import com.hfad.daktari1.model.PatientData
import com.hfad.daktari1.notifications.NotificationData
import com.hfad.daktari1.notifications.PushNotification
import com.hfad.daktari1.notifications.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var databaseReference: DatabaseReference

    var token1: String? = "245"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        val view = binding.root

        databaseReference = FirebaseDatabase.getInstance().getReference("patients")
       // val token2 = intent.getStringExtra("client_token")
       // val title = intent.getStringExtra("title")
        val name = intent.getStringExtra("name")
        val docName = intent.getStringExtra("docName")

        val date = intent.getStringExtra("date")
        val startTime = intent.getStringExtra("startTime")
        val endTime = intent.getStringExtra("endTime")
        val uid = intent.getStringExtra("uid")
      //  Toast.makeText(this, "Token is $uid", Toast.LENGTH_LONG).show()



//
//
//        databaseReference.child(uid!!).addValueEventListener(object :ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    for (patientSnapshot in snapshot.children){
//                        val patient = patientSnapshot.getValue(PatientData::class.java)
//                        token = patient?.token
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//               // Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
//            }
//
//        })

        getPatientToken(uid)

        binding.txtRequest.text = "Apointment request by $name"
        binding.txtMessage.text = "On $date from $startTime to $endTime"

     //   Toast.makeText(this, "Token is $token1", Toast.LENGTH_LONG).show()




        binding.btnAccept.setOnClickListener {

          //  Toast.makeText(this, "Token is $token1", Toast.LENGTH_LONG).show()


            val title = "Appointment ACCEPTED"
            val message = "Online appointment on $date from $startTime to $endTime"

            if (message.isNotEmpty()){
                PushNotification(
                    NotificationData(title, message,docName
                        ,date,startTime,endTime),
                    token1
                ).also {
                    sendNotification(it)
                }
            }

            Toast.makeText(this, "Sent to $name", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MeetingDetailsActivity::class.java)
            startActivity(intent)


        }

        binding.btnDecline.setOnClickListener {
            val title = "Appointment DECLINED"
            val message = "Please choose another doctor"

            if (message.isNotEmpty()){
                PushNotification(
                    NotificationData(title, message,docName,date,startTime,endTime),
                    token1
                ).also {
                    sendNotification(it)
                }
            }
        }

        setContentView(view)
    }

    private fun getPatientToken(uid: String?) {
        databaseReference = FirebaseDatabase.getInstance().getReference("patients")
        //var token:String? = null

        databaseReference.child(uid!!).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    val patient = snapshot.getValue(PatientData::class.java)
                      var  token = patient?.token
                    sendToken(token)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

       // return token



    }
        fun sendToken(token: String?){
        token1 = token

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