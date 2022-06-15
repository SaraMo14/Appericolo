package com.example.appericolo.ui.preferiti.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.appericolo.R
import com.example.appericolo.ui.preferiti.contacts.database.Contact


class ContactsListAdapter(private val context: Context, private val dataSource: ArrayList<Contact>) : BaseAdapter() {

    private var contactList = emptyList<Contact>()

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    //1
    override fun getCount(): Int {
        return dataSource.size
    }

    //2
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.item_contact, parent, false)

        val contactName = rowView.findViewById(R.id.tv_name) as TextView

        val contactNumber = rowView.findViewById(R.id.tv_number) as TextView

        val contactImage = rowView.findViewById(R.id.iv_profile) as ImageView

        // 1
        val contact = getItem(position) as Contact

// 2
        contactName.text = contact.name
        contactNumber.text = contact.number

// 3

        contactImage.setImageResource(R.drawable.mich)

        return rowView
    }

    fun setData(contact: List<Contact>){
        this.contactList = contact
        notifyDataSetChanged()
    }

}
