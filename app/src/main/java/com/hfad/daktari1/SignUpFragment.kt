package com.hfad.daktari1

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.hfad.daktari1.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.loader.visibility = View.INVISIBLE

        mAuth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            binding.loader.visibility = View.VISIBLE
            invisible()
            loader()
            signUp(email,password)
        }

        return view
}

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





}