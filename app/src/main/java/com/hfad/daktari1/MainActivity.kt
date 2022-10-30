package com.hfad.daktari1

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.hfad.daktari1.databinding.ActivityMainBinding
import com.hfad.daktari1.main.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //private lateinit var mDataBase: DatabaseReference
   // private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val homeFragment = HomeFragment()

        val extras : Bundle? = intent.extras
        if (extras != null && extras.containsKey("OpenF")) {
            var openF2 = extras?.getBoolean("openF2")
            if (openF2 == true){
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.nav_host_fragment, homeFragment)
                    commit()
                }
            }
        }





      /*  mAuth = FirebaseAuth.getInstance()
        mDataBase = FirebaseDatabase.getInstance().getReference("doctors")


        var uid = mAuth.currentUser?.uid

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Add token to database
            if (uid != null){
            mDataBase.child(uid).child("token").setValue(token)

            }

        })*/

    }
}