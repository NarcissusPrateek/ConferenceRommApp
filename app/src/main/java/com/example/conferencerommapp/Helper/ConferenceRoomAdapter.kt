package com.example.conferencerommapp.Helper

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.conferencerommapp.Model.ConferenceRoom
import com.example.conferencerommapp.R


class ConferenceRoomAdapter(private val conferenceRoomList: List<ConferenceRoom>, val btnlistener: BtnClickListener) : androidx.recyclerview.widget.RecyclerView.Adapter<ConferenceRoomAdapter.ViewHolder>() {

	companion object {
		var mClickListener: BtnClickListener? = null
	}
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

		val view = LayoutInflater.from(parent.context).inflate(R.layout.room_availablity, parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		mClickListener = btnlistener
		holder.conferenceRoom = conferenceRoomList[position]

		holder.txvRoom.text = conferenceRoomList[position].conf_name

		holder.txvRoomCapacity.text = conferenceRoomList[position].conf_capacity


		holder.txvStatus.text = conferenceRoomList[position].Status


		if(holder.txvStatus.text.equals("Available")) {
			holder.txvStatus.setTextColor(Color.parseColor("#4C9A2A"))
		}
		else if(holder.txvStatus.text.equals("Booked")) {
			holder.txvStatus.setTextColor(Color.parseColor("#A9A9A9"))
		}else if(holder.txvStatus.text.equals("Blocked")) {
			holder.txvStatus.setTextColor(Color.parseColor("#FE7D6A"))
		}

		holder.itemView.setOnClickListener { v ->
			if(holder.txvStatus.text.equals("Available")) {

				val context = v.context
				val roomId = conferenceRoomList[position].conf_id
				val roomname = conferenceRoomList[position].conf_name
				mClickListener?.onBtnClick(roomId.toString(),roomname)
			}else if(holder.txvStatus.text.equals("Booked")) {

			}else if(holder.txvStatus.text.equals("Blocked")) {

			}

		}
	}

	override fun getItemCount(): Int {
		return conferenceRoomList.size
	}

	class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

		val txvRoom: TextView = itemView.findViewById(R.id.txv_room)
		val txvRoomCapacity: TextView = itemView.findViewById(R.id.txv_room_capacity)
     	val txvStatus: TextView = itemView.findViewById(R.id.status_txv)
		var cardview: CardView = itemView.findViewById(R.id.cardview2)
		var conferenceRoom: ConferenceRoom? = null

		override fun toString(): String {
			return """${super.toString()} '${txvRoom.text}'"""
		}
	}
	open interface BtnClickListener {
		fun onBtnClick(roomId: String?,roomname: String?)
	}

}