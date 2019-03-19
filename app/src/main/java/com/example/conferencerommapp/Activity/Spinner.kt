package com.example.conferencerommapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.example.conferencerommapp.Model.BlockRoom
import com.example.conferencerommapp.Model.BlockingConfirmation
import com.example.conferencerommapp.services.ConferenceService
import com.example.globofly.services.Servicebuilder
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_spinner.*
import kotlinx.android.synthetic.main.activity_user_inputs.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Spinner : AppCompatActivity() {

    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spinner)
        val actionBar = supportActionBar
        actionBar!!.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Block) + "</font>"))

        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setCancelable(false)
        var timeFormat = SimpleDateFormat("HH:mm ", Locale.US)
        var dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        fromTime_b.setOnClickListener {
            val now = Calendar.getInstance()
            val timePickerDialog =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)

                    var nowtime = timeFormat.format(selectedTime.time).toString()
                    fromTime_b.text = Editable.Factory.getInstance().newEditable(nowtime)
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }



        toTime_b.setOnClickListener {
            val now = Calendar.getInstance()
            val timePickerDialog =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)

                    var nowtime = timeFormat.format(selectedTime.time).toString()
                    toTime_b.text = Editable.Factory.getInstance().newEditable(nowtime)
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }


        date_block.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker =
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    //
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    var nowDate: String = dateFormat.format(selectedDate.time).toString()
                    date_block.text = Editable.Factory.getInstance().newEditable(nowDate)
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
            datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePicker.show()

        }

        val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
        progressDialog!!.show()
        getBuilding(acct!!.email.toString())
    }

    private fun getBuilding(email: String) {
        var room = BlockRoom()
        val buildingapi = Servicebuilder.buildService(ConferenceService::class.java)
        val requestCall: Call<List<BuildingT>> = buildingapi.getBuildings()
        requestCall.enqueue(object : Callback<List<BuildingT>> {
            override fun onFailure(call: Call<List<BuildingT>>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<BuildingT>>, response: Response<List<BuildingT>>) {
                progressDialog!!.dismiss()
                if (response.isSuccessful) {
                    response.body()?.let {
                        var items = mutableListOf<String>()
                        var items_id = mutableListOf<Int>()
                        for (item in it) {
                            items.add(item.BName!!)
                            items_id.add(item.BId!!)
                        }
                        //val adapter = ArrayAdapter<String>(this@Spinner, android.R.layout., items)
                        buiding_Spinner.adapter =
                            ArrayAdapter<String>(this@Spinner, android.R.layout.simple_list_item_1, items)
                        buiding_Spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                progressDialog!!.show()
                                val conferenceapi = Servicebuilder.buildService(ConferenceService::class.java)
                                room.BId = items_id[position]
                                val requestCall: Call<List<BuildingConference>> =
                                    conferenceapi.getBuildingsConference(items_id[position])
                                requestCall.enqueue(object : Callback<List<BuildingConference>> {
                                    override fun onFailure(call: Call<List<BuildingConference>>, t: Throwable) {
                                        progressDialog!!.dismiss()
                                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                                    }

                                    override fun onResponse(
                                        call: Call<List<BuildingConference>>,
                                        response: Response<List<BuildingConference>>
                                    ) {
                                        progressDialog!!.dismiss()
                                        if (response.isSuccessful) {
                                            response.body()?.let {
                                                var conference_name = mutableListOf<String>()
                                                var conference_id = mutableListOf<Int>()

                                                for (item in it) {
                                                    conference_name.add(item.CName!!)
                                                    conference_id.add(item.CId)
                                                }
                                                // val conferenceadapter = ArrayAdapter<String>(applicationContext,android.R.layout.,conference_name)
                                                conference_Spinner.adapter = ArrayAdapter<String>(
                                                    this@Spinner,
                                                    android.R.layout.simple_list_item_1,
                                                    conference_name
                                                )
                                                conference_Spinner.onItemSelectedListener =
                                                    object : AdapterView.OnItemSelectedListener {
                                                        override fun onNothingSelected(parent: AdapterView<*>?) {
                                                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                                        }

                                                        override fun onItemSelected(
                                                            parent: AdapterView<*>?,
                                                            view: View?,
                                                            position: Int,
                                                            id: Long
                                                        ) {
                                                            val blockRoom: Button = findViewById(R.id.block_conference)
                                                            blockRoom.setOnClickListener {
                                                                if (TextUtils.isEmpty(fromTime_b.text.trim())) {
                                                                    Toast.makeText(
                                                                        applicationContext,
                                                                        "Please enter the From-Time",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()

                                                                } else if (TextUtils.isEmpty(toTime_b.text.trim())) {
                                                                    Toast.makeText(
                                                                        applicationContext,
                                                                        "Please enter the To-Time",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                } else if (TextUtils.isEmpty(date_block.text.trim())) {
                                                                    Toast.makeText(
                                                                        applicationContext,
                                                                        "Please enter the Date",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }else if(Purpose.text.toString().trim().isEmpty()){
                                                                    Toast.makeText(
                                                                        applicationContext,
                                                                        "Please enter the Purpose of Meeting",
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }
                                                                else {
                                                                    val startTime = fromTime_b.text.toString()
                                                                    val endTime = toTime_b.text.toString()
                                                                    try {
                                                                        val sdf = SimpleDateFormat("HH:mm")
                                                                        val sdf1 = SimpleDateFormat("yyyy-M-dd HH:mm")
                                                                        val d1 = sdf.parse(startTime)
                                                                        val d2 = sdf.parse(endTime)
                                                                        val d3 = sdf1.parse(date_block.text.toString() + " " + startTime)
                                                                        val currTime = System.currentTimeMillis()
                                                                        val elapsed = d2.time - d1.time
                                                                        val elapsed2 = d3.time - currTime
                                                                        if(elapsed2 < 0) {
                                                                            val builder = AlertDialog.Builder(this@Spinner)
                                                                            builder.setTitle("Check...")
                                                                            builder.setMessage("From-Time must be greater than the current time...")
                                                                            builder.setPositiveButton("Ok") { dialog, which ->
                                                                            }
                                                                            val dialog: AlertDialog = builder.create()
                                                                            dialog.setCanceledOnTouchOutside(false)
                                                                            dialog.show()
                                                                        }
                                                                        else if ((elapsed > 0)) {

                                                                            room.CId = conference_id[position]
                                                                            room.Email = email
                                                                            room.Status = "block"
                                                                            var date1 = date_block.text.toString()
                                                                            room.FromTime =
                                                                                date1 + " " + fromTime_b.text.toString()
                                                                            room.ToTime =
                                                                                date1 + " " + toTime_b.text.toString()
                                                                            room.Purpose = Purpose.text.toString()
                                                                            Log.i("----Block------", room.toString())
                                                                            progressDialog!!.show()
                                                                            blocking(room)

                                                                        } else {
                                                                            val builder = AlertDialog.Builder(this@Spinner)
                                                                            builder.setTitle("Blocked Status")
                                                                            builder.setMessage("From-Time must be greater then To-Time and the meeting time must be less then 4 Hours")
                                                                            builder.setPositiveButton("Ok") { dialog, which ->
                                                                            }
                                                                            val dialog: AlertDialog = builder.create()
                                                                            dialog.setCanceledOnTouchOutside(false)
                                                                            dialog.show()
                                                                        }
                                                                    } catch (e: Exception) {
                                                                        Log.i(
                                                                            "--------------####--",
                                                                            e.message.toString()
                                                                        )
                                                                        Toast.makeText(
                                                                            this@Spinner,
                                                                            "Details are Invalid!!!",
                                                                            Toast.LENGTH_LONG
                                                                        ).show()
                                                                    }

                                                                }

                                                            }
                                                        }

                                                    }
                                            }
                                        }
                                    }

                                })
                            }

                        }

                    }

                } else {
                    Toast.makeText(applicationContext, "Unable to Load ", Toast.LENGTH_LONG).show()
                }

            }

        })
    }

    private fun blocking(room: BlockRoom) {

        val blockroomapi = Servicebuilder.buildService(ConferenceService::class.java)
        val requestCall: Call<BlockingConfirmation> = blockroomapi.blockConfirmation(room)
        requestCall.enqueue(object : Callback<BlockingConfirmation> {
            override fun onFailure(call: Call<BlockingConfirmation>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<BlockingConfirmation>, response: Response<BlockingConfirmation>) {
                progressDialog!!.dismiss()
                val builder = AlertDialog.Builder(this@Spinner)
                builder.setTitle("Blocking Status")
                Log.i("---------Block---", response.body().toString())
                if (response.isSuccessful) {
                    var blockingConfirmation = response.body()
                    if (blockingConfirmation == null) {
                        blockConfirmed(room)
                    } else {
                        var name = blockingConfirmation.Name
                        var purpose = blockingConfirmation.Purpose
                        builder.setMessage(
                            "Room is already Booked by Employee ${name} for ${purpose}.\nAre you sure the 'BLOCKING' is Necessary?"
                        )
                        builder.setPositiveButton("Ok") { dialog, which ->
                            progressDialog!!.setMessage("Processing....")
                            progressDialog!!.show()
                            blockConfirmed(room)
                        }

                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }

                } else {
                    builder.setMessage("Something went wrong Room can't be Blocked.")
                    builder.setPositiveButton("Ok") { dialog, which ->

                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()

                }
            }

        })


    }
    fun blockConfirmed(room : BlockRoom) {
        val blockroomapi = Servicebuilder.buildService(ConferenceService::class.java)
        val requestCall: Call<ResponseBody> = blockroomapi.blockconference(room)
        requestCall.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressDialog!!.dismiss()
                val builder =
                    AlertDialog.Builder(this@Spinner)
                builder.setTitle("Blocking Status")
                Log.i("---------Block---", response.body().toString())
                if (response.isSuccessful) {
                    builder.setMessage(
                        "Room is " +
                                " Blocked..."
                    )
                    builder.setPositiveButton("Ok") { dialog, which ->
                        finish()
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                } else {
                    builder.setMessage("Something went wrong Room can't be Blocked.")
                    builder.setPositiveButton("Ok") { dialog, which ->

                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()

                }
            }

        })

    }
}
