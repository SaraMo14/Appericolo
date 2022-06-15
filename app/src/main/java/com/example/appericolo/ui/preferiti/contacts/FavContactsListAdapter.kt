package com.example.appericolo.ui.preferiti.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appericolo.R
import com.example.appericolo.ui.preferiti.contacts.database.Contact

class FavContactsListAdapter: RecyclerView.Adapter<FavContactsListAdapter.MyViewHolder>() {

    private lateinit var cListener: onItemClickListener
    interface onItemClickListener{

        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        cListener = listener
    }

    private var contactsList = emptyList<Contact>()

    class MyViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent,false), cListener)
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