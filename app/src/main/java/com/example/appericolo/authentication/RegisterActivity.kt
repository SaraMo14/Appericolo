package com.example.appericolo.authentication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.appericolo.utils.CommonInfo
import com.example.appericolo.ui.preferiti.contacts.database.Contact
import com.example.appericolo.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    lateinit var database: DatabaseReference
    lateinit var mFirebaseAuth: FirebaseAuth

    lateinit var name: String
    lateinit var surname: String
    lateinit var email: String
    lateinit var password: String
    lateinit var cell_number: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameFocusListener()
        surnameFocusListener()
        emailFocusListener()
        phoneFocusListener()
        passwordFocusListener()

        mFirebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users")

        binding.registerButton.setOnClickListener {
            check_fileds()
        }
    }


    private fun check_fileds() {
        val validName = binding.NameContainer.helperText == null
        val validSurname = binding.SurnameContainer.helperText == null
        val validEmail = binding.EmailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null
        val validPhone = binding.phoneContainer.helperText == null


        if (validName && validSurname && validEmail && validPassword && validPhone)
            createAccount()
        else
            invalidForm()
    }

    private fun createAccount() {
        name = binding.registerName.text.toString()
        surname = binding.registerSurname.text.toString()
        cell_number = binding.registerCell.text.toString()
        password = binding.registerPassword.text.toString()
        email = binding.registerEmail.text.toString()

        mFirebaseAuth!!
            .createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = User(name, surname, cell_number, email)
                    database.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user)
                    val intent = Intent(this, LoginActivity::class.java)
                    // store registration token
                    CommonInfo.retrieveAndStoreToken()
                    //
                    startActivity(intent)
                    finish()

                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@RegisterActivity, "Authentication failed. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun invalidForm()
    {
        var message = ""
        if(binding.EmailContainer.helperText != null)
            message += "\n\nEmail: " + binding.EmailContainer.helperText
        if(binding.passwordContainer.helperText != null)
            message += "\n\nPassword: " + binding.passwordContainer.helperText
        if(binding.phoneContainer.helperText != null)
            message += "\n\nPhone: " + binding.phoneContainer.helperText
        if(binding.NameContainer.helperText != null)
            message += "\n\nName: " + binding.NameContainer.helperText
        if(binding.SurnameContainer.helperText != null)
            message += "\n\nSurname: " + binding.SurnameContainer.helperText

        AlertDialog.Builder(this)
            .setTitle("Invalid Form")
            .setMessage(message)
            .setPositiveButton("Okay"){ _,_ ->
                // do nothing
            }
            .show()
    }

    private fun emailFocusListener() {
        binding.registerEmail.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.EmailContainer.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String?
    {
        val emailText = binding.registerEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Invalid Email Address"
        }
        return null
    }

    private fun passwordFocusListener()
    {
        binding.registerPassword.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.passwordContainer.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String?
    {
        val passwordText = binding.registerPassword.text.toString()
        if(passwordText.length < 8)
        {
            return "Minimum 8 Character Password"
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex()))
        {
            return "Must Contain 1 Upper-case Character"
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
        {
            return "Must Contain 1 Lower-case Character"
        }
        if(!passwordText.matches(".*[@#\$%^&+=].*".toRegex()))
        {
            return "Must Contain 1 Special Character (@#\$%^&+=)"
        }

        return null
    }

    private fun phoneFocusListener()
    {
        binding.registerCell.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.phoneContainer.helperText = validPhone()
            }
        }
    }

    private fun validPhone(): String?
    {
        val phoneText = binding.registerCell.text.toString()
        if(!phoneText.matches(".*[0-9].*".toRegex()))
        {
            return "Must be all Digits"
        }
        if(phoneText.length != 10)
        {
            return "Must be 10 Digits"
        }
        return null
    }

    private fun nameFocusListener() {
        binding.registerName.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.NameContainer.helperText = validName()
            }
        }
    }

    private fun validName(): String?
    {
        val nameText = binding.registerName.text.toString()
        if(nameText =="")
        {
            return "Must insert a name"
        }
        return null
    }

    private fun surnameFocusListener() {
        binding.registerSurname.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.SurnameContainer.helperText = validSurame()
            }
        }
    }

    private fun validSurame(): String?
    {
        val surnameText = binding.registerSurname.text.toString()
        if(surnameText =="")
        {
            return "Must insert a surname"
        }
        return null
    }
}