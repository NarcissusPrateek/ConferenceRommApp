package com.example.conferencerommapp

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.conferencerommapp.Activity.BuildingDashboard
import com.example.conferencerommapp.Activity.Main2Activity
import com.example.conferencerommapp.services.ConferenceService
import com.example.globofly.services.Servicebuilder
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.activity_blocked_dashboard.*
import kotlinx.android.synthetic.main.content_main2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlockedDashboard : AppCompatActivity() {

    var mGoogleSignInClient: GoogleSignInClient? = null
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.setCancelable(false)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocked_dashboard)
        val actionBar = supportActionBar
        actionBar!!.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Blocked_Rooms) + "</font>"))
        val addConferenceRoom: FloatingActionButton = findViewById(R.id.add_conference)
        val maintenance: FloatingActionButton = findViewById(R.id.maintenance)
        val menu: FloatingActionMenu = findViewById(R.id.menu)

        menu.setClosedOnTouchOutside(true);
        maintenance.setOnClickListener {
            val maintenanceintent = Intent(applicationContext, Spinner::class.java)
            startActivity(maintenanceintent)
            //finish()
        }

        addConferenceRoom.setOnClickListener {
            val addConferenceintent = Intent(applicationContext, BuildingDashboard::class.java)
            startActivity(addConferenceintent)
            //finish()
        }

    }
     override fun onBackPressed() {
        startActivity(Intent(this, Main2Activity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        progressDialog!!.show()
        loadBlocking()
    }

    private fun loadBlocking() {

        val blockServices = Servicebuilder.buildService(ConferenceService::class.java)
        val requestCall: Call<List<Blocked>> = blockServices.getBlockedConference()
        requestCall.enqueue(object : Callback<List<Blocked>> {
            override fun onFailure(call: Call<List<Blocked>>, t: Throwable) {
                progressDialog!!.dismiss()
                empty_view_blocked.visibility = View.VISIBLE
                empty_view_blocked.setImageResource(R.drawable.error_shot)
                r2_block_dashboard.setBackgroundColor(Color.WHITE)

                //Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Blocked>>, response: Response<List<Blocked>>) {
                progressDialog!!.dismiss()
                if (response.isSuccessful) {

                    val blockedList: List<Blocked>? = response.body()
                    Log.i("-------List------", blockedList.toString())
                    if (blockedList!!.size == 0) {
                        empty_view_blocked.visibility = View.VISIBLE
                        empty_view_blocked.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }else {

                        empty_view_blocked.visibility = View.GONE
                        conference_blocked_list.adapter = BlockedDashboardNew(blockedList!!, this@BlockedDashboard)

                    }
                } else {
                    Toast.makeText(applicationContext, "Unable to Load ", Toast.LENGTH_LONG).show()
                }

            }

        })
    }
}
