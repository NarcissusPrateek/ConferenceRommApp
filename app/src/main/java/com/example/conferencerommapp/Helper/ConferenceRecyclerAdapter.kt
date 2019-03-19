package com.example.conferencerommapp

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.example.myapplication.Models.ConferenceList

class ConferenceRecyclerAdapter(private val conferencceList:List<ConferenceList>) : androidx.recyclerview.widget.RecyclerView.Adapter<ConferenceRecyclerAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConferenceRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.conference_list, parent, false)
        return ConferenceRecyclerAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
       Log.i("--------Size----", conferencceList.size.toString())
        //Toast.makeText(,conferencceList.size.toString(),Toast.LENGTH_SHORT).show()
        return  conferencceList.size

    }

    override fun onBindViewHolder(holder: ConferenceRecyclerAdapter.ViewHolder, position: Int) {
        holder.conferencelist=conferencceList[position]
        holder.conferenceName.text=conferencceList[position].CName
        holder.conferencecapacity.text= conferencceList[position].Capacity.toString()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
            val conferenceName : TextView = itemView.findViewById(R.id.room_name_show)
            val conferencecapacity : TextView = itemView.findViewById(R.id.conference_room_capacity_show)
            var conferencelist: ConferenceList?= null
    }

}