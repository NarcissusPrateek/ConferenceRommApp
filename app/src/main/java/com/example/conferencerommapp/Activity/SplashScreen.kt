package com.example.conferencerommapp.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.conferencerommapp.BlockedDashboard
import com.example.conferencerommapp.R
import com.example.conferencerommapp.RegistrationActivity
import com.example.conferencerommapp.SignIn
import com.example.conferencerommapp.services.ConferenceService
import com.example.globofly.services.Servicebuilder
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_splash_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashScreen : AppCompatActivity() {

    var prefs: SharedPreferences? = null
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val logoHandler: Handler = Handler()

        val logoRunnable: Runnable = Runnable {
            progressDialog = ProgressDialog(this@SplashScreen)
            progressDialog!!.setMessage("Loading....")
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
            //logo.visibility = View.GONE
            //login_progress.visibility = View.VISIBLE
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account != null) {
                prefs = getSharedPreferences("myPref", Context.MODE_PRIVATE)
                connectTOBackend(account.email)
                //finish()
            } else {
//                val intent : Intent = Intent(applicationContext,UserInputActivity::class.java)
//                startActivity(intent)
//                finish()
                prefs = getSharedPreferences("myPref", Context.MODE_PRIVATE)
                val intent: Intent = Intent(applicationContext, SignIn::class.java)
                startActivity(intent)
                finish()
            }
        }
        logoHandler.postDelayed(logoRunnable, 3000)
    }

    fun connectTOBackend(email: String?) {
        val service = Servicebuilder.buildService(ConferenceService::class.java)
        val requestCall: Call<Int> = service.getRequestCode(email)
        requestCall.enqueue(object : Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                progressDialog!!.dismiss()
               // login_progress.visibility = View.GONE
                Toast.makeText(
                    this@SplashScreen,
                    "Server not found!! Please check your connection!",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                //login_progress.visibility = View.GONE
                progressDialog!!.dismiss()
                if (response.isSuccessful) {
                    var code = response.body()
                    val editor = prefs!!.edit()
                    editor.putInt("Code", code!!)
                    editor.apply()
                    goAction(code)
//                    Toast.makeText(
//                        this@SplashScreen,
//                        "code is ${getSharedPreferences("myPref", Context.MODE_PRIVATE).getInt("Code", 5)}",
//                        Toast.LENGTH_LONG
//                    ).show()
                } else {
                    Toast.makeText(this@SplashScreen, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show()
                }

            }
        })

    }
    fun goAction(code: Int?) {
        when (code) {
            11 -> {
                startActivity(Intent(this@SplashScreen, Main2Activity::class.java))
                finish()
            }
            10 -> {
                startActivity(Intent(this@SplashScreen, Main2Activity::class.java))
                finish()
            }
            2 -> {
                startActivity(Intent(this@SplashScreen, Main2Activity::class.java))
                finish()
            }
            0 -> {
                startActivity(Intent(this@SplashScreen, RegistrationActivity::class.java))
                finish()
            }
            else -> {
                val builder = AlertDialog.Builder(this@SplashScreen)
                builder.setTitle("Error!")
                builder.setMessage("Plese Restart the application..")
                builder.setPositiveButton("Ok") { dialog, which ->
                    finish()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

        }
    }

}

