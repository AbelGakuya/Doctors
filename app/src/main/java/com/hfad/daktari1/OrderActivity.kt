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
//import kotlinx.coroutines.NonCancellable.message
//import kotlinx.coroutines.NonCancellable.message
import kotlinx.coroutines.launch

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var databaseReference: DatabaseReference

    var token1: String? = "245"
    var name: String? = "rtg"
    var docName: String? = ""
    var date: String?=""
    var startTime: String?=""
    var endTime: String?=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        val view = binding.root

        databaseReference = FirebaseDatabase.getInstance().getReference("patients")

        //val intent = Intent()

        val extras : Bundle? = intent.extras
        if (extras != null){
             name = extras.getString("name1")
             docName = intent.getStringExtra("docName1")
             date = intent.getStringExtra("date1")
             startTime = intent.getStringExtra("startTime1")
             endTime = intent.getStringExtra("endTime1")

            val uid = extras.getString("uid1")
//         val uid = "aIaxOUIke2YdsZj2FxxbWOFT6Kz1"
            Toast.makeText(this, "Token is $uid", Toast.LENGTH_LONG).show()

            getPatientToken(uid)
            setDetails(name,date,startTime,endTime)



        }


       // Toast.makeText(this, "Token is $name", Toast.LENGTH_SHORT).show()



        binding.btnAccept.setOnClickListener {
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
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
            finish()
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
            val intent = Intent(this, MeetingDetailsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
            finish()
        }

        setContentView(view)
    }

    private fun setDetails(name: String?, date: String?, startTime: String?, endTime: String?) {
        binding.txtRequest.text = "Appointment request by $name"
        binding.txtMessage.text = "Online appointment on $date at $startTime to $endTime"
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