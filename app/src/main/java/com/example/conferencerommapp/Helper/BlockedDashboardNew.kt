package com.example.conferencerommapp

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.conferencerommapp.Helper.Unblock
import com.example.conferencerommapp.services.ConferenceService
import com.example.globofly.services.Servicebuilder


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BlockedDashboardNew(private val blockedList: List<Blocked>, val contex: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<BlockedDashboardNew.ViewHolder>() {

    var progressDialog: ProgressDialog? = null
    var currentPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.block_dashboard_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.card.setOnClickListener(View.OnClickListener {
            currentPosition = position
            notifyDataSetChanged()

        })

        if(currentPosition == position) {
            if(holder.linearLayout.visibility==View.GONE) {
                var animmation: Animation = AnimationUtils.loadAnimation(contex, R.anim.animation)
                holder.linearLayout.visibility = View.VISIBLE
                holder.linearLayout.startAnimation(animmation)
            }
            else{
                var animation: Animation = AnimationUtils.loadAnimation(contex, R.anim.close)
                holder.linearLayout.visibility=View.GONE
                holder.linearLayout.startAnimation(animation)
            }
        }
        holder.blocked = blockedList[position]
        val date1 = blockedList[position].FromTime!!.split("T")[0]
        holder.date.text = date1
        holder.conferenceName.text = blockedList[position].CName
        holder.buildingName.text = blockedList[position].BName
        holder.purpose.text = blockedList[position].Purpose
        holder.fromtime.text = blockedList[position].FromTime!!.split("T")[1] + " - " + blockedList[position].ToTime!!.split("T")[1]

        var id = blockedList[position].CId
        holder.unblock.setOnClickListener {
            var block = Unblock()
            block.CId = blockedList[position].CId
            block.FromTime = blockedList[position].FromTime
            block.ToTime = blockedList[position].ToTime
            unBlock(block, contex)
        }
        holder.itemView.setOnClickListener { v ->
            var id = blockedList[position].CId

        }
    }

    private fun unBlock(room: Unblock, contex: Context) {
        progressDialog = ProgressDialog(contex)
        progressDialog!!.setMessage("Processing....")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()

        val unBlockApi = Servicebuilder.buildService(ConferenceService::class.java)
        val requestCall : Call<ResponseBody> = unBlockApi.unBlockingConferenceRoom(room)
        requestCall.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog!!.dismiss()
                val builder = AlertDialog.Builder(contex)
                builder.setTitle("Status...")
                builder.setMessage("Unblocking Failed! Try again.")
                builder.setPositiveButton("Ok") { dialog, which ->

                }
                val dialog: AlertDialog = builder.create()
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressDialog!!.dismiss()
                var message = "Room is Unblocked Successfully."
                val builder = AlertDialog.Builder(contex)
                builder.setTitle("Status...")
                builder.setMessage(message)
                builder.setCancelable(false)
                builder.setPositiveButton("Ok") { dialog, which ->
                    ContextCompat.startActivity(contex, Intent(contex, BlockedDashboard::class.java), null)
                    (contex as Activity).finish()
                }
                val dialog: AlertDialog = builder.create()
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
            }

        })
    }

    override fun getItemCount(): Int {
        return blockedList.size
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val conferenceName : TextView = itemView.findViewById(R.id.block_conferenceRoomName)
        val buildingName : TextView = itemView.findViewById(R.id.block_building_name)
        val purpose : TextView = itemView.findViewById(R.id.block_purpose)
        val fromtime : TextView = itemView.findViewById(R.id.block_from_time)
        val date : TextView = itemView.findViewById(R.id.block_date)
        val card: CardView = itemView.findViewById(R.id.card_block)
        val linearLayout:LinearLayout = itemView.findViewById(R.id.linearlayout_block)

        val unblock :Button = itemView.findViewById(R.id.unblock)
        var blocked: Blocked? = null



    }
}