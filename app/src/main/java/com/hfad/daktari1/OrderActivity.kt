package com.hfad.daktari1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hfad.daktari1.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        val view = binding.root


        binding.btnAccept.setOnClickListener {

        }

        setContentView(view)
    }
}