package com.example.conferencerommapp.Activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.conferencerommapp.R
import kotlinx.android.synthetic.main.activity_user_inputs.*
import java.text.SimpleDateFormat
import java.util.*




class UserInputActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_inputs)
        val actionBar = supportActionBar
        actionBar!!.setTitle(Html.fromHtml("<font font-size = \"23px\" color=\"#FFFFFF\">" + getString(R.string.Booking_Details) + "</font>"))


        var timeFormat = SimpleDateFormat("HH:mm ", Locale.US)
        var dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        var capacity = ""
        var date_text: EditText = findViewById(R.id.date)
        var fromtime: EditText = findViewById(R.id.fromTime)
        var totime: EditText = findViewById(R.id.toTime)
        var building_avtivity_button: Button = findViewById(R.id.next)

        fromtime.setOnClickListener {
            val now = Calendar.getInstance()
            val timePickerDialog =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)

                    var nowtime = timeFormat.format(selectedTime.time).toString()
                    fromtime.text = Editable.Factory.getInstance().newEditable(nowtime)
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }



        totime.setOnClickListener {
            val now = Calendar.getInstance()
            val timePickerDialog =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val selectedTime = Calendar.getInstance()
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedTime.set(Calendar.MINUTE, minute)

                    var nowtime = timeFormat.format(selectedTime.time).toString()
                    totime.text = Editable.Factory.getInstance().newEditable(nowtime)
                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }


        date_text.setOnClickListener {
            val now = Calendar.getInstance()
            val datePicker =
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    //
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    var nowDate: String = dateFormat.format(selectedDate.time).toString()
                    date_text.text = Editable.Factory.getInstance().newEditable(nowDate)
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
            datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePicker.show()

        }

        var options = arrayOf(2, 4, 6, 8, 10, 12, 14, 16)
        spinner2.adapter = ArrayAdapter<Int>(this, android.R.layout.simple_list_item_1, options)
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                capacity = "2"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                capacity = spinner2.getItemAtPosition(position).toString()
            }
        }
        building_avtivity_button.setOnClickListener {
            if (TextUtils.isEmpty(fromtime.text.trim())) {
                Toast.makeText(applicationContext, "Invalid From Time", Toast.LENGTH_SHORT).show()

            } else if (TextUtils.isEmpty(totime.text.trim())) {
                Toast.makeText(applicationContext, "Invalid To Time", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(date_text.text.trim())) {
                Toast.makeText(applicationContext, "Invalid Date", Toast.LENGTH_SHORT).show()
            } else if (capacity.equals("Select Capacity")) {
                Toast.makeText(applicationContext, "Invalid Capacity", Toast.LENGTH_SHORT).show()
            } else {
                val startTime = fromTime.text.toString()
                val endTime = totime.text.toString()
                try {
                    val sdf = SimpleDateFormat("HH:mm")
                    val sdf1 = SimpleDateFormat("yyyy-M-dd HH:mm")
                    val d1 = sdf.parse(startTime)
                    val d2 = sdf.parse(endTime)
                    val d3 = sdf1.parse(date_text.text.toString() + " " + fromTime.text.toString())
                    val elapsed = d2.time - d1.time
                    var min: Long = 900000
                    val currtime = System.currentTimeMillis()
                    val elapsed2 = d3.time - currtime
                    Log.i("-----time---",d3.time.toString() + d3.toString())
                    Log.i("-----current---",currtime.toString())
                    Log.i("--------diff------", " " + elapsed2 )
                    var max: Long = 14400000
                    if(elapsed2 < 0) {
                        val builder = AlertDialog.Builder(this@UserInputActivity)
                        builder.setTitle("Check...")
                        builder.setMessage("From-Time must be greater than the current time...")
                        builder.setPositiveButton("Ok") { dialog, which ->
                        }
                        val dialog: AlertDialog = builder.create()
                        dialog.setCanceledOnTouchOutside(false)
                        dialog.show()
                    }
                    else if ((min <= elapsed) && (max >= elapsed)) {
                        val buildingintent = Intent(this@UserInputActivity, BuildingsActivity::class.java)
                        buildingintent.putExtra("FromTime", fromtime.text.toString())
                        buildingintent.putExtra("ToTime", totime.text.toString())
                        buildingintent.putExtra("Date", date_text.text.toString())
                        buildingintent.putExtra("Capacity", capacity)
                        startActivity(buildingintent)
                    }
                    else {
                        val builder = AlertDialog.Builder(this@UserInputActivity)
                        builder.setTitle("Check...")
                        builder.setMessage("From-Time must be greater then To-Time and the meeting time must be less then 4 Hours")
                        builder.setPositiveButton("Ok") { dialog, which ->
                        }
                        val dialog: AlertDialog = builder.create()
                        dialog.setCanceledOnTouchOutside(false)
                        dialog.show()
                    }
                } catch (e: Exception) {
                    Log.i("---------", e.message)
                    Toast.makeText(this@UserInputActivity, "Details are Invalid!!!", Toast.LENGTH_LONG).show()
                }

            }


        }

    }


}
