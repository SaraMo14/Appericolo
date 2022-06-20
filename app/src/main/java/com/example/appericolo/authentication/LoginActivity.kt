package com.example.appericolo.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.appericolo.R
import com.example.appericolo.utils.CommonInfo
import com.example.appericolo.MainActivity
import com.example.appericolo.ui.preferiti.contacts.ContactViewModel
import com.example.appericolo.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var email: String
    lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // to keep the user logged
        var auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser !=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.registerFromLoginButton.setOnClickListener {
            val register_intent = Intent(this, RegisterActivity::class.java)
            startActivity(register_intent)

        }

        binding.loginButton.setOnClickListener {
            email = binding.loginEmail.text.toString()
            password = binding.loginPassword.text.toString()
            if (checkFields()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            //poi cambiare

                            var contactViewModel : ContactViewModel
                            contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
                            contactViewModel.deleteAll()
                            contactViewModel.putFavFromRemoteToLocal()
                            val MoveToMain = Intent(this, MainActivity::class.java)

                            // store registration token
                            //CommonInfo.retrieveAndStoreToken()
                            contactViewModel.retrieveAndStoreToken()
                            //

                            startActivity(MoveToMain)
                            finish()
                        } else Toast.makeText(
                            this,
                            "Please insert correct username and password.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else Toast.makeText(this, "Please fill the fields.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkFields(): Boolean {
        return email != "" && password != ""
    }

    //for fcm
    /*private fun retrieveAndStoreToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            if(it.isSuccessful){
                val token = it.result
                val userUid= FirebaseAuth.getInstance().currentUser!!.uid
                //tokens/userUid/
                FirebaseDatabase.getInstance().getReference("tokens")
                    .child(userUid)
                    .setValue(token)
            }
        }
    }*/
}