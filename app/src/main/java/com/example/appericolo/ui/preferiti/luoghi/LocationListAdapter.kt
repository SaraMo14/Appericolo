package com.example.appericolo.ui.preferiti.luoghi


import com.example.appericolo.ui.preferiti.luoghi.database.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appericolo.R

/**
 * Adapter per la lista dei luoghi preferiti
 */
class LocationListAdapter(val listener: RowClickListener) : ListAdapter<Location, LocationListAdapter.LocationViewHolder>(
    LocationsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener{
            listener.onItemClickListener(current)
        }
        //
        holder.bind(current)
       // holder.bind(current.indirizzo)
    }

    class LocationViewHolder(itemView: View, val listener: RowClickListener) : RecyclerView.ViewHolder(itemView) {
        private val locationItemView: TextView = itemView.findViewById(R.id.textViewIndirizzo)
        //val delButton: ImageView = itemView.findViewById(R.id.delLocationButton)
        fun bind(data: Location?) {
            locationItemView.text = data?.indirizzo
            /*delButton.setOnClickListener {
                if (data != null) {
                    listener.onDeleteLocationClickListener(data)
                }
            }*/
        }


        companion object {
            fun create(parent: ViewGroup, listener: RowClickListener): LocationViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item_luoghi, parent, false)
                return LocationViewHolder(view, listener)
            }
        }
    }

    interface RowClickListener{
        fun onDeleteLocationClickListener(location: Location)
        fun onItemClickListener(location: Location)

    }

    class LocationsComparator : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.latitude == newItem.latitude &&  oldItem.longitude == newItem.longitude
        }
    }

}