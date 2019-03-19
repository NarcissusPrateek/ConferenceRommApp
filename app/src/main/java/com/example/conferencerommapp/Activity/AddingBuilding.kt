package com.example.conferencerommapp.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.conferencerommapp.ConferenceDashBoard
import com.example.conferencerommapp.Model.addBuilding
import com.example.conferencerommapp.R
import com.example.conferencerommapp.services.ConferenceService
import com.example.globofly.services.Servicebuilder
import kotlinx.android.synthetic.main.app_bar_main2.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddingBuilding : AppCompatActivity() {


    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_building)
        val actionBar = supportActionBar

        actionBar!!.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Add_Buildings) + "</font>"))
        val add: Button = findViewById(R.id.addbuilding)
        add.setOnClickListener {
            val bName: TextView = findViewById(R.id.input_buildingName)
            val bplace: TextView = findViewById(R.id.input_buildingPlace)
            var build = addBuilding()
            build.BName = bName.text.toString().trim()
            build.Place = bplace.text.toString().trim()
            if (TextUtils.isEmpty(bName.text)) {
                Toast.makeText(this@AddingBuilding, "Please enter the Building Name!", Toast.LENGTH_LONG).show()
            } else if (TextUtils.isEmpty(bplace.text)) {
                Toast.makeText(this@AddingBuilding, "Please enter the place of building!", Toast.LENGTH_LONG).show()
            } else {
                progressDialog = ProgressDialog(this)
                progressDialog!!.setMessage("Adding...")
                progressDialog!!.setCancelable(false)
                progressDialog!!.show()
                addBuild(build)
            }
        }
    }

    private fun addBuild(build: addBuilding) {
        val buildapi = Servicebuilder.buildService(ConferenceService::class.java)
        val addconferencerequestCall: Call<ResponseBody> = buildapi.addBuilding(build)
        addconferencerequestCall.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(applicationContext, "some backend problem", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val builder = AlertDialog.Builder(this@AddingBuilding)
                builder.setTitle("Status")
                progressDialog!!.dismiss()
                if (response.isSuccessful) {
                    builder.setMessage("Building added successfully.")
                    builder.setPositiveButton("Ok") { dialog, which ->
                        finish()
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.setCanceledOnTouchOutside(false)
                    dialog.show()
                } else {
                    builder.setMessage("Unable to Add! Please try again")
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
