package com.example.conferencerommapp.Helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.conferencerommapp.BuildingT
import com.example.conferencerommapp.R
import kotlin.random.Random

class BuildingRecyclerAdapter(private val buildingList: List<BuildingT>, val btnlistener: BtnClickListener) : androidx.recyclerview.widget.RecyclerView.Adapter<BuildingRecyclerAdapter.ViewHolder>() {

    companion object {
        var mClickListener: BtnClickListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.building_dashboard_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mClickListener = btnlistener

        holder.building = buildingList[position]
        holder.txvBuilding.text = buildingList[position].BName
        var id = buildingList[position].BId
        var building_name = buildingList[position].BName
        holder.itemView.setOnClickListener { v ->
            mClickListener?.onBtnClick(id, building_name)
        }
    }

    override fun getItemCount(): Int {
        return buildingList.size
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val txvBuilding: TextView = itemView.findViewById(R.id.building_name)
        var building: BuildingT? = null

        override fun toString(): String {
            return """${super.toString()} '${txvBuilding.text}'"""
        }
    }

    open interface BtnClickListener {
        fun onBtnClick(buildingId: Int?,buildingname: String?)
    }
}

