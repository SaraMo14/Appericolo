package com.example.appericolo.ui.preferiti.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.appericolo.R
import com.example.appericolo.ui.preferiti.contacts.database.Contact
import kotlin.collections.ArrayList

class NewContactActivity : AppCompatActivity() {
    private lateinit var contactViewModel: ContactViewModel
    var cols = listOf<String>(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
    ).toTypedArray()

    var contactslist : MutableList<Contact> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_new_contact)

        //controllo permessi
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                Array(1) { Manifest.permission.READ_CONTACTS },
                111
            )
        } else
            getContacts(null,null)

        //funzioni che mi permettono di cercare contatti al variare della searchbar
        findViewById<SearchView>(R.id.search_contact).setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                getContacts("${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?", Array(1){"$p0%"})
                return false
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            getContacts(null, null)
    }

    private fun getContacts(selection: String?, selectionArgs: Array<String>?){
        var response = sendQuery(selection, selectionArgs)
        populateRecyclerView(response)

    }
    private fun sendQuery(selection: String?, selectionArgs: Array<String>?): Cursor? {
        return contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            cols, selection, selectionArgs, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
    }

    @SuppressLint("Range")
    private fun populateRecyclerView(rs: Cursor?) {
        contactslist.clear()
        if (rs?.count!! > 0) {
            while (rs.moveToNext()) {
                var name =
                    rs.getString(rs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var number =
                    rs.getString(rs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                var contact = Contact(number,name)

                contactslist.add(contact)
            }
        }

        val lv: ListView = findViewById(R.id.contact_list)
        val adapter = ContactsListAdapter(this, contactslist as ArrayList<Contact>)
        lv.adapter = adapter

        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        lv.setOnItemClickListener { parent,_, position, _ ->
            var selectedItem = parent.getItemAtPosition(position) as Contact
            Toast.makeText(this, selectedItem.name+selectedItem.number, Toast.LENGTH_SHORT).show()
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to add "+  selectedItem.name + " to your favourite contacts list?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    var contact = Contact(selectedItem.number, selectedItem.name)
                    contactViewModel.addContact(contact)
                    Toast.makeText(this,"Aggiunto al db", Toast.LENGTH_SHORT).show()
                    finish()

                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

    }

}