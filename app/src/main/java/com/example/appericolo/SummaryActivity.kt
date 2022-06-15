package com.example.appericolo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appericolo.ui.preferiti.contacts.ContactViewModel
import com.example.appericolo.ui.preferiti.contacts.database.Contact

class SummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val rv = findViewById<RecyclerView>(R.id.favcontactlist)
        val adapter = ListAdapter()
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(applicationContext)

        var contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        contactViewModel.readAllDataFromLocal.observe(this, Observer { contact->
            adapter.setData(contact)
    })
}

    class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {


        private var contactsList = emptyList<Contact>()

        class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{

            return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent,false))
        }

        override fun getItemCount(): Int {
            return contactsList.size
        }

        public fun getItem(position: Int): Contact{
            return contactsList[position]
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int){
            val currentItem = contactsList[position]
            holder.itemView.findViewById<TextView>(R.id.tv_number).text = currentItem.number.toString()
            holder.itemView.findViewById<TextView>(R.id.tv_name).text = currentItem.name
            holder.itemView.findViewById<ImageView>(R.id.iv_profile).setImageResource(R.drawable.mich)
        }

        fun setData(contact: List<Contact>){
            this.contactsList = contact
            notifyDataSetChanged()
        }

    }
}