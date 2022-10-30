package com.hfad.daktari1.roomdatabase

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.hfad.daktari1.R
import com.hfad.daktari1.main.HomeFragment

class AvailabilityAdapter
    : RecyclerView.Adapter<AvailabilityAdapter.AvailabilityViewHolder>() {

   private var data = emptyList<Availability>()

   private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }






    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailabilityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.date, parent, false) as CardView
        return AvailabilityViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: AvailabilityViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)






    }

    override fun getItemCount(): Int = data.size

    fun setData(availability: List<Availability>){
        this.data = availability
        notifyDataSetChanged()
    }

   class AvailabilityViewHolder(val rootView : CardView,  listener: onItemClickListener)
        :RecyclerView.ViewHolder(rootView){

        val date = rootView.findViewById<TextView>(R.id.date)
        val startTime = rootView.findViewById<TextView>(R.id.startTime)
        val endTime = rootView.findViewById<TextView>(R.id.endTime)
       val parent = rootView.findViewById<CardView>(R.id.parent_layout)

      init {

           itemView.setOnClickListener {
               listener.onItemClick(adapterPosition)
           }
       }

        fun bind(item: Availability){
            date.text = item.date
            startTime.text = item.startTime
            endTime.text = item.endTime
        }

        }

}