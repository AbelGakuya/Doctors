package com.hfad.daktari1

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hfad.daktari1.databinding.FragmentRegisterBinding
import android.graphics.Bitmap
import android.provider.Settings
import android.provider.Settings.Global.getString
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var dialog: Dialog


    private lateinit var doctor : Doctor


    private lateinit var filePath: Uri
    private val PICK_IMAGE_REQUEST = 22
    private lateinit var storageReference: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        mAuth = FirebaseAuth.getInstance()

     val uid = mAuth.currentUser?.uid

        databaseReference = FirebaseDatabase.getInstance().getReference("doctors")

      //  val uidd = databaseReference.child()

        binding.profileImage.setOnClickListener {
            selectImage()
        }

        binding.btnRegister.setOnClickListener {
            showProgressBar()
            val title = binding.edtTitle.text.toString()
            val firstName = binding.edtFirstName.text.toString()
            val lastName = binding.edtLastName.text.toString()
            val name = title + " " + firstName + " " + lastName
            val bio = binding.edtBio.text.toString()
            doctor = Doctor(title, firstName, lastName, name, bio, uid)
            addToDatabase()
          //  getRegistrationToken()

        }



        return view
    }

    private fun addToDatabase() = CoroutineScope(Dispatchers.IO).launch {
        val uid = mAuth.currentUser?.uid
        if (uid != null){
            databaseReference.child(uid).setValue(doctor).addOnCompleteListener {
                if (it.isSuccessful){
                    uploadImage()
                    val directions = RegisterFragmentDirections.actionRegisterFragmentToMainActivity2()
                    findNavController().navigate(directions)
                }else{
                    hideProgressBar()
                    Toast.makeText(context, "You are not a registered Doctor!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }



    private fun selectImage() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            // Get the Uri of data
            filePath = data.data!!
            try {

                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        getActivity()?.getContentResolver(),
                        filePath
                    )
                binding.profileImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        val  uid = mAuth.currentUser?.uid
        val filename = UUID.randomUUID().toString()
        storageReference = FirebaseStorage.getInstance().getReference("/images/$filename")
        storageReference.putFile(filePath).addOnSuccessListener {
            hideProgressBar()
            Toast.makeText(context, "Profile Successfully Updated", Toast.LENGTH_SHORT).show()
            storageReference.downloadUrl.addOnSuccessListener {
                it.toString()
                if (uid != null) {
                    databaseReference.child(uid).child("imageUrl").setValue(it.toString())
                }
            }


          //  val downloadUri: Task<Uri> = storageReference.downloadUrl
        /*    if (uid != null) {
                databaseReference.child(uid).child("imageUrl").setValue("/images/$filename")
            }*/
        }.addOnFailureListener{
            hideProgressBar()
            Toast.makeText(context,"Failed to update profile", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun getRegistrationToken() {
//        var uid = mAuth.currentUser?.uid
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Add token to database
//            if (uid != null){
//                databaseReference.child(uid).child("token").setValue(token)
//
//            }
//        })
//    }




    private fun showProgressBar(){
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun hideProgressBar(){
        dialog.dismiss()
    }

}



