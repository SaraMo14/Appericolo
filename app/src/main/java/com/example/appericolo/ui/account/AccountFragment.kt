package com.example.appericolo.ui.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appericolo.authentication.User
import com.example.appericolo.databinding.FragmentAccountBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class AccountFragment : Fragment() {


    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var database: DatabaseReference =
            FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users/")
        database.child(Firebase.auth.currentUser?.uid.toString()).get().addOnSuccessListener { infos->
            binding.etFirstName.text = infos.child("name").getValue().toString()
            binding.etLastName.text = infos.child("surname").getValue().toString()
            binding.etEmail.text = infos.child("email").getValue().toString()
            binding.etContactNo.text = infos.child("cell_number").getValue().toString()
        }.addOnFailureListener{
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}