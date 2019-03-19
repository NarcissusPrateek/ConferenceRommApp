package com.example.conferencerommapp

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.conferencerommapp.Helper.Unblock
import com.example.conferencerommapp.services.ConferenceService
import com.example.globofly.services.Servicebuilder


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BlockedRecyclerAdapter(private val blockedList: List<Blocked>, val contex: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<BlockedRecyclerAdapter.ViewHolder>() {

    var progressDialog: ProgressDialog? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.blocked_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.blocked = blockedList[position]
        holder.conferenceName.text = blockedList[position].CName
        holder.buildingName.text = blockedList[position].BName
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

        val conferenceName : TextView = itemView.findViewById(R.id.conferenceRoomName)
        val buildingName : TextView = itemView.findViewById(R.id.buildingname)
        val unblock :Button = itemView.findViewById(R.id.unblock)
        var blocked: Blocked? = null



    }
}