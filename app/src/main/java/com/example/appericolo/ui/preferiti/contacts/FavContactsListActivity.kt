package com.example.appericolo.ui.preferiti.contacts

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appericolo.R
import com.example.appericolo.SummaryActivity

import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavContactsListActivity : AppCompatActivity() {
    lateinit var  contactViewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_contacts_list)

        val adapter = FavContactsListAdapter()
        val rv = findViewById<RecyclerView>(R.id.recyclerView)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(applicationContext)

        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        //contactViewModel.deleteAll()
        //contactViewModel.putFavFromRemoteToLocal()
        contactViewModel.readAllData.observe(this, Observer { contact->
            adapter.setData(contact)
        })
        adapter.setOnItemClickListener(object : FavContactsListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                var selectedItem =  adapter.getItem(position)
                val builder = AlertDialog.Builder(this@FavContactsListActivity)
                builder.setMessage("Are you sure you want to delete "+  selectedItem.name + " to your favourite contacts list?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        contactViewModel.removeContact(selectedItem)
                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }

        })

        findViewById<FloatingActionButton>(R.id.addFavContFAB).setOnClickListener{
            val intent = Intent(this, SummaryActivity::class.java)
            startActivity(intent)
        }


    }
}