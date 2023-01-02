package com.hfad.daktari1.agora

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hfad.daktari1.R
import com.hfad.daktari1.databinding.ActivityMeetingDetailsBinding

class MeetingDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMeetingDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMeetingDetailsBinding.inflate(layoutInflater)
        val view = binding.root


        binding.btnJoinChannel.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        setContentView(view)
    }
}