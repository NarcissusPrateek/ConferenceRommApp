package com.example.conferencerommapp.Helper

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import com.example.conferencerommapp.Activity.Main2Activity
import com.example.conferencerommapp.Model.CancelBooking
import com.example.conferencerommapp.Model.Dashboard
import com.example.conferencerommapp.R
import com.example.conferencerommapp.services.ConferenceService
import com.example.globofly.services.Servicebuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashBoardAdapter(val dashboardItemList: List<Dashboard>,val contex: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<DashBoardAdapter.ViewHolder>() {

	var progressDialog: ProgressDialog? = null
	var currentPosition = 0
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

		val view = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_list, parent, false)
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
		holder.dashboard = dashboardItemList[position]
        holder.txvBName.text = dashboardItemList[position].BName
        holder.txvRoomName.text = dashboardItemList[position].CName
		//holder.txvToTime.text = dashboardItemList[position].ToTime
        holder.txvPurpose.text = dashboardItemList[position].Purpose

		var fromtime = dashboardItemList[position].FromTime
		var totime = dashboardItemList[position].ToTime
		var datefrom = fromtime!!.split("T")
		var dateto = totime!!.split("T")

		holder.txvDate.text = datefrom.get(0)
		holder.txvFrom.text = datefrom.get(1) + " - " + dateto.get(1)

		if(dashboardItemList[position].Status.toString().trim() == "Cancelled") {
			holder.btnCancel.text = "Cancelled"
			holder.btnCancel.isEnabled = false
		}
		else {
				holder.btnCancel.text = "Cancel"
				holder.btnCancel.isEnabled = true

				holder.btnCancel.setOnClickListener{
				var builder = AlertDialog.Builder(contex)
				builder.setTitle("Confirm ")
				builder.setMessage("Are you sure you want to cancel the meeting?")

					builder.setPositiveButton("YES"){dialog, which ->
					progressDialog = ProgressDialog(contex)
					progressDialog!!.setMessage("Processing....")
					progressDialog!!.setCancelable(false)
					progressDialog!!.show()
					var cancel = CancelBooking()
					cancel.CId = dashboardItemList.get(position).CId
					cancel.ToTime = totime
					cancel.FromTime = fromtime
					cancel.Email = dashboardItemList.get(position).Email
					cancelBooking(cancel,contex)
				}
				builder.setNegativeButton("No"){ dialog, which ->
					Log.i("----------" ," Dil de diya")
				}
				val dialog: AlertDialog = builder.create()
					dialog.setCancelable(false)
				dialog.show()
				var button_p : Button = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
				var button_n : Button = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
				if (button_p != null) {
					button_p.setBackgroundColor(Color.WHITE)
					button_p.setTextColor(Color.parseColor("#0072bc"))
				}

				if(button_n != null) {
					Log.i("------", "Dilna diya")
					button_n.setBackgroundColor(Color.WHITE)
					button_n.setTextColor(Color.BLACK)
				}
			}
		}

	}
	 private fun cancelBooking(cancel: CancelBooking,contex: Context) {
		val service = Servicebuilder.buildService(ConferenceService::class.java )
		val requestCall : Call<Int> = service.cancelBooking(cancel)
		requestCall.enqueue(object: Callback<Int> {
			override fun onFailure(call: Call<Int>, t: Throwable) {
				progressDialog!!.dismiss()
				Toast.makeText(contex,"Error on Failure", Toast.LENGTH_LONG).show()
			}
			override fun onResponse(call: Call<Int>, response: Response<Int>) {
				progressDialog!!.dismiss()
				if(response.isSuccessful) {
					val code = response.body()
					Toast.makeText(contex,"Booking Cancelled Successfully", Toast.LENGTH_SHORT).show()
						startActivity(contex,Intent(contex, Main2Activity::class.java),null)
					(contex as Activity).finish()
				}
				else {
					Toast.makeText(contex,"Response Error", Toast.LENGTH_LONG).show()
				}

			}

		})
	}
	override fun getItemCount(): Int {
		return dashboardItemList.size
	}

	class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

		val txvBName: TextView = itemView.findViewById(R.id.building_name)
		val txvRoomName: TextView = itemView.findViewById(R.id.conferenceRoomName)
        val txvFrom: TextView = itemView.findViewById(R.id.from_time)
      	//val txvToTime: TextView = itemView.findViewById(R.id.to_time)
        val txvDate: TextView = itemView.findViewById(R.id.date)
        val txvPurpose: TextView = itemView.findViewById(R.id.purpose)
        val btnCancel: Button = itemView.findViewById(R.id.btnCancel)
		val linearLayout: LinearLayout = itemView.findViewById(R.id.linearlayout)
		val card: CardView = itemView.findViewById(R.id.card)
       //val hidden: TextView = itemView.findViewById(R.id.hidden_txv)
        var dashboard: Dashboard? = null

		//override fun toString(): String {
		//	return """${super.toString()} '${txvBuilding.text}'"""
		//}
	}

}