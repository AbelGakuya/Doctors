package com.hfad.daktari1.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hfad.daktari1.MainActivity
import com.hfad.daktari1.OrderActivity
import com.hfad.daktari1.R
import kotlin.random.Random

private const val CHANNEL_ID = "my_channel"

class FirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

//        val title = message.data["title"]
//        val message = message.data["message"]

        val bundle = Bundle()

        bundle.putString("uid1", message.data["uid"])
        bundle.putString("name1",message.data["name"])
        bundle.putString("date1", message.data["date"])
        bundle.putString("startTime1", message.data["startTime"])
        bundle.putString("endTime1", message.data["endTime"])
        bundle.putString("docName1", message.data["docName"])


        val intent = Intent(this, OrderActivity::class.java)
        //intent.putExtra("client_token", message.data["token2"])


        intent.putExtras(bundle)

//        intent.putExtra("uid1", message.data["uid"])
//        intent.putExtra("name1",message.data["name"])
//        intent.putExtra("docName1", message.data["docName"])
//        intent.putExtra("date1", message.data["date"])
//        intent.putExtra("startTime1", message.data["startTime"])
//        intent.putExtra("endTime1", message.data["endTime"])
//
//        intent.putExtra("message",message.data["message"])

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.icon)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }

    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    var databaseReference = FirebaseDatabase.getInstance().getReference("doctors")
    var uid = mAuth.currentUser?.uid
    override fun onNewToken(token: String) {
        Log.d(ContentValues.TAG, "Refreshed token: $token")


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        // Add token to database
        if (uid != null){
            databaseReference.child(uid!!).child("token").setValue(token)

        }
    }



}