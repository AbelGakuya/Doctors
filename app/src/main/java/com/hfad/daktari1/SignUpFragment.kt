package com.hfad.daktari1

import android.R
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.datatransport.runtime.retries.Retries.retry
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.messaging.FirebaseMessaging
import com.hfad.daktari1.databinding.FragmentSignUpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
   // private lateinit var mDataBase: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.loader.visibility = View.INVISIBLE

        mAuth = FirebaseAuth.getInstance()

        databaseReference = FirebaseDatabase.getInstance().getReference("doctors")

        //  mDataBase = FirebaseDatabase.getInstance().getReference("doctors")




        binding.btnSignUp.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (email.isNullOrBlank()&&password.isNullOrBlank()){
                Toast.makeText(requireContext(), "Please fill the required details", Toast.LENGTH_LONG).show()
            } else {
                binding.loader.visibility = View.VISIBLE
                invisible()
                loader()
                signUp(email, password)
                // getRegistrationToken()

            }
        }

        return view
}

   /* private fun getRegistrationToken() {
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

        })
    }*/

    private fun loader(){
        val loader = binding.loader
        loader.defaultColor = ContextCompat.getColor(requireActivity(), R.color.holo_blue_dark)
        loader.selectedColor = ContextCompat.getColor(requireActivity(), R.color.holo_blue_bright)
        loader.bigCircleRadius = 80
        loader.radius = 24
        loader.animDur = 300
        loader.showRunningShadow = true
        loader.firstShadowColor = ContextCompat.getColor(requireActivity(), R.color.holo_blue_light)
        loader.secondShadowColor = ContextCompat.getColor(requireActivity(), R.color.holo_blue_light)
    }


    fun invisible(){
        binding.edtName.visibility = View.INVISIBLE
        binding.edtEmail.visibility = View.INVISIBLE
        binding.edtPassword.visibility = View.INVISIBLE
        binding.btnSignUp.visibility = View.INVISIBLE
        binding.appLogo.visibility = View.INVISIBLE
    }

    private fun signUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {

                    getRegistrationToken()

                    val directions = SignUpFragmentDirections.actionSignUpFragmentToRegisterFragment()
                    findNavController().navigate(directions)

                  //  activity?.finish()
                } else {
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    retry()
                }
            }

    }

    private fun retry(){
        binding.loader.visibility = View.INVISIBLE
        binding.appLogo.visibility = View.VISIBLE
        binding.edtEmail.visibility = View.VISIBLE
        binding.edtPassword.visibility = View.VISIBLE
        binding.btnSignUp.visibility = View.VISIBLE
    }

    fun getRegistrationToken() = CoroutineScope(Dispatchers.IO).launch{
        var uid = mAuth.currentUser?.uid

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            //  sharedViewModelClientToken.saveContent(token)

            // Add token to database
            if (uid != null){
                databaseReference.child(uid).child("token").setValue(token)

            }

        })


    }




}