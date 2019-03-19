package com.example.conferencerommapp

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.*
import com.example.conferencerommapp.services.ConferenceService
import com.example.globofly.services.Servicebuilder
import kotlinx.android.synthetic.main.activity_adding_conference.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddingConference : AppCompatActivity() {

    var options1 = arrayOf(2, 4, 6, 8, 10, 12, 14)
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_conference)
        val actionBar = supportActionBar
        actionBar!!.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Add_Room) + "</font>"))
        var capacity = ""
        conference_Capacity.adapter =
            ArrayAdapter<Int>(this@AddingConference, android.R.layout.simple_list_item_1, options1)
        conference_Capacity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                capacity = "3"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                capacity = conference_Capacity.getItemAtPosition(position).toString()
            }

        }
        val bundle: Bundle = intent.extras
        val buildingId = bundle.get("BuildingId").toString().toInt()

        add_conference_room.setOnClickListener {
            val conferenceRoom: EditText = findViewById(R.id.conference_Name)
            var room = addConferenceRoom()
            if (conferenceRoom.text.trim().isEmpty()) {
                Toast.makeText(this@AddingConference, "Please Enter Room Name", Toast.LENGTH_LONG).show()
            } else if (capacity.equals("Please Select Capacity")) {
                Toast.makeText(this@AddingConference, "Please Select Capacity", Toast.LENGTH_LONG).show()
            } else {
                room.BId = buildingId
                room.CName = conferenceRoom.text.toString().trim()
                room.Capacity = capacity.toInt()
                progressDialog = ProgressDialog(this)
                progressDialog!!.setMessage("Adding...")
                progressDialog!!.setCancelable(false)
                progressDialog!!.show()
                addingRoom(room)
            }
        }

    }

    private fun addingRoom(room: addConferenceRoom) {
        val conferenceRoomapi = Servicebuilder.buildService(ConferenceService::class.java)
        val addconferencerequestCall: Call<ResponseBody> = conferenceRoomapi.addConference(room)
        addconferencerequestCall.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(applicationContext, "onFailure", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressDialog!!.dismiss()
                val builder = AlertDialog.Builder(this@AddingConference)
                builder.setTitle("Status")
                if (response.isSuccessful) {
                    builder.setMessage("Room added Successfully.")
                    builder.setPositiveButton("Ok") { dialog, which ->
                        finish()
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.setCanceledOnTouchOutside(false)
                    dialog.show()
                } else {
                    builder.setMessage("Unable to Add! Please try again.")
                    builder.setPositiveButton("Ok") { dialog, which ->
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.setCanceledOnTouchOutside(false)
                    dialog.show()
                    //Toast.makeText(applicationContext, "Unable to post", Toast.LENGTH_SHORT).show()
                }
            }

        })

    }
}