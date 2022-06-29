package com.example.appericolo.authentication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.appericolo.utils.CommonInfo
import com.example.appericolo.ui.preferiti.contacts.database.Contact
import com.example.appericolo.databinding.ActivityRegisterBinding
import com.example.appericolo.ui.preferiti.contacts.ContactViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Classe per la gestione della registrazione di un nuovo utente
 */
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


        mFirebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users")

        binding.registerButton.setOnClickListener {
            set_helpers()
            check_fileds()
        }
    }

    fun set_helpers(){
        binding.EmailContainer.helperText = validEmail()
        binding.passwordContainer.helperText = validPassword()
        binding.phoneContainer.helperText = validPhone()
        binding.NameContainer.helperText = validName()
        binding.SurnameContainer.helperText = validSurame()
    }

    private fun check_fileds() {

        val validName = binding.NameContainer.helperText == null
        val validSurname = binding.SurnameContainer.helperText == null
        val validEmail = binding.EmailContainer.helperText == null
        val validPassword = binding.passwordContainer.helperText == null
        val validPhone = binding.phoneContainer.helperText == null

        //se gli helper text sono tutti null -> creo l'account
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

        mFirebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = User(name, surname, cell_number, email)
                    //inserisco l'utente nel db realtime
                    database.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user)
                    val intent = Intent(this, LoginActivity::class.java)
                    //store registration token
                    val contactViewModel : ContactViewModel = ViewModelProvider(this).get(
                        ContactViewModel::class.java)
                    contactViewModel.retrieveAndStoreToken()
                    //
                    startActivity(intent)
                    finish()

                } else {
                    //Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@RegisterActivity, "Autenticazione fallita. Riprova di nuovo.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun validEmail(): String?
    {
        val emailText = binding.registerEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Indirizzo email non valido"
        }
        return null
    }

    private fun validPassword(): String?
    {
        val passwordText = binding.registerPassword.text.toString()
        if(passwordText.length < 8)
        {
            return "Password di minimo 8 caratteri"
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex()))
        {
            return "Deve contenere 1 carattere maiuscolo."
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
        {
            return "Deve contenere 1 carattere minuscolo."
        }
        if(!passwordText.matches(".*[@#\$%^&+=].*".toRegex()))
        {
            return "Deve contenere 1 carattere speciale. (@#\$%^&+=)"
        }

        return null
    }

    private fun validPhone(): String?
    {
        val phoneText = binding.registerCell.text.toString()
        if(!phoneText.matches(".*[0-9].*".toRegex()))
        {
            return "Deve contenere solo cifre"
        }
        if(phoneText.length != 10)
        {
            return "Deve contenere 10 cifre"
        }
        return null
    }

    private fun validName(): String?
    {
        val nameText = binding.registerName.text.toString()
        if(nameText =="")
        {
            return "Inserire un nome"
        }
        return null
    }

    private fun validSurame(): String?
    {
        val surnameText = binding.registerSurname.text.toString()
        if(surnameText =="")
        {
            return "Inserire un cognome"
        }
        return null
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
}