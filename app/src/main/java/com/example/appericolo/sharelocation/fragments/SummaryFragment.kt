package com.example.appericolo.sharelocation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appericolo.R
import com.example.appericolo.sharelocation.fcm.NotificationData
import com.example.appericolo.sharelocation.fcm.PushNotification
import com.example.appericolo.sharelocation.fcm.RetrofitInstance
import com.example.appericolo.ui.preferiti.contacts.ContactViewModel
import com.example.appericolo.ui.preferiti.contacts.database.Contact
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Fragment per mostrare all'utente il riepilogo dei dati sul viaggio selezionato
 */
class SummaryFragment : Fragment() {

    var database: DatabaseReference =
        FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users/" + Firebase.auth.currentUser?.uid.toString() + "/favContacts")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view= inflater.inflate(R.layout.fragment_summary, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.favcontactlist)
        val adapter = ListAdapter()
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this.requireContext())

        var contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        contactViewModel.readAllData.observe(viewLifecycleOwner, Observer { contact->
            adapter.setData(contact)
        })


        val orarioArrivo= arguments?.getString("orario_arrivo")
        val coordinate: LatLng? = arguments?.getParcelable("coordinate")

        val indirizzo = arguments?.getString("indirizzo")
        view.findViewById<TextView>(R.id.orarioArrivoTextView).text=orarioArrivo.toString()
        view.findViewById<TextView>(R.id.destinationTextView).text = indirizzo.toString()

        view.findViewById<Button>(R.id.confermaButton).setOnClickListener{

            //dati
            val title = "Appericolo"
            val message = FirebaseAuth.getInstance().currentUser!!.email + " sta condividendo la sua posizione con te"

            //in base ai contatti stretti selezionati in precedenza, si individua, per coloro che possiedono l'app,
            // il relativo registration token su firebase.
            //Sulla base del token viene inviata la notifica di inizio condivisione
            database.parent?.parent?.get()?.addOnSuccessListener { users->
                for (user in users.children){
                    //Log.i("contatti db remoto: ", user.child("name").getValue().toString())
                    for (favContact in contactViewModel.readAllData.value!!) {
                        //Log.i("contatti in db locale: ", favContact.name.toString())
                        if (favContact.number == user.child("cell_number").getValue().toString() ||
                            favContact.number == "+39" + user.child("cell_number").getValue().toString() ||
                            "+39" + favContact.number == user.child("cell_number").getValue().toString() ) {
                            //Log.i("contatto buono", user.child("name").getValue().toString())

                            val token = user.child("token").getValue().toString()
                            //invia notifica al contatto stretto selezionato
                            PushNotification(
                                NotificationData(title,
                                    message,
                                    FirebaseAuth.getInstance().currentUser!!.uid,
                                    false,
                                    orarioArrivo.toString(),
                                    coordinate?.latitude!!,
                                    coordinate?.longitude),
                                token
                            ).also {
                                sendDepartureNotification(it)
                            }
                        }
                    }
                }
            }


            val bundle = bundleOf("indirizzo" to indirizzo, "coordinate" to coordinate, "orarioArrivo" to orarioArrivo )
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.action_summaryFragment_to_locationUpdatesClientFragment, bundle)

        }


        return view
    }

    class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {


        private var contactsList = emptyList<Contact>()

        class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

            return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent,false))
        }

        override fun getItemCount(): Int {
            return contactsList.size
        }

        public fun getItem(position: Int): Contact {
            return contactsList[position]
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int){
            val currentItem = contactsList[position]
            holder.itemView.findViewById<TextView>(R.id.tv_number).text = currentItem.number.toString()
            holder.itemView.findViewById<TextView>(R.id.tv_name).text = currentItem.name
            holder.itemView.findViewById<ImageView>(R.id.iv_profile).setImageResource(R.drawable.ic_stat_name)
        }

        fun setData(contact: List<Contact>){
            this.contactsList = contact
            notifyDataSetChanged()
        }

    }


    //send notification request
    private fun sendDepartureNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try{
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.i("SummaryFragment", "Response success")
            }else{
                Log.e("SummaryFragment", response.errorBody().toString())
            }
        }catch (e: Exception){
            Log.e("SummaryFragment", e.toString())
        }
    }
}