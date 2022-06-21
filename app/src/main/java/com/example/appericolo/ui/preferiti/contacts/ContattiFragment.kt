package com.example.appericolo.ui.preferiti.contacts

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appericolo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Fragment contenuto nel tab 1 delle preferenze dell'utente. Esso consente all'utente di aggiungere, eliminare e visualizzare
 * i contatti stretti
 */
class ContattiFragment : Fragment() {
    lateinit var  contactViewModel: ContactViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        //Log.i("ContattiFragment", "dentro")

        val view = inflater.inflate(R.layout.fragment_contatti, container, false)

        val adapter = FavContactsListAdapter()
        val rv = view.findViewById<RecyclerView>(R.id.recyclerView)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this.requireContext())

        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        //contactViewModel.deleteAll()
        //contactViewModel.putFavFromRemoteToLocal()
        contactViewModel.readAllData.observe(this.requireActivity(), Observer { contact->
            adapter.setData(contact)
        })
        adapter.setOnItemClickListener(object : FavContactsListAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                var selectedItem =  adapter.getItem(position)
                val builder = AlertDialog.Builder(this@ContattiFragment.requireContext())
                builder.setMessage("Sicuro di voler eliminare "+  selectedItem.name + " dalla lista dei contatti preferiti?")
                    .setCancelable(false)
                    .setPositiveButton("Sì") { dialog, id ->
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
       /* //in teoria da togliere
        contactViewModel.tokens.observe(viewLifecycleOwner, Observer(){
            Log.i("treno", it.size.toString())
        })*/
        //per aggiungere un nuovo contatto:
        view.findViewById<FloatingActionButton>(R.id.addFavContFAB).setOnClickListener{
            val intent = Intent(this.requireContext(), NewContactActivity::class.java)
            startActivity(intent)

        }
        return view
    }


}