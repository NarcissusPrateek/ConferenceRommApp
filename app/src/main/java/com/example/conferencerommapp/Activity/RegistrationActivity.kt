package com.example.conferencerommapp

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.conferencerommapp.Activity.Main2Activity
import com.example.conferencerommapp.Model.Employee
import com.example.conferencerommapp.services.ConferenceService
import com.example.globofly.services.Servicebuilder
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationActivity : AppCompatActivity() {

    var mGoogleSignInClient: GoogleSignInClient? = null
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(com.example.conferencerommapp.R.layout.activity_registration)
        setContentView(R.layout.activity_registration)
        val actionBar = supportActionBar
        actionBar!!.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.Registration) + "</font>"))

       // Toast.makeText(applicationContext, "Registration Activity", Toast.LENGTH_LONG).show()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
        val employee = Employee()

        var options = arrayOf(
            "Intern",
            "SDE-1",
            "SDE-2",
            "SDE-3",
            "Principal Engineer",
            "Project Manager",
            "HR",
            "CEO",
            "CTO",
            "COO"
        )
        spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                employee.Role = "Intern"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                employee.Role = spinner.getItemAtPosition(position).toString()
            }
        }
        button_add.setOnClickListener(View.OnClickListener {
            if (edittext_id.text.trim().isEmpty()) {
                Toast.makeText(this@RegistrationActivity, "Invalid Name", Toast.LENGTH_SHORT).show()
            } else if (textView_name.text.trim().isEmpty()) {
                Toast.makeText(this@RegistrationActivity, "Invalid Id", Toast.LENGTH_SHORT).show()
            } else if (employee.Role.toString().equals("Select Role")) {
                Toast.makeText(this@RegistrationActivity, "Invalid Role", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog = ProgressDialog(this@RegistrationActivity)
                progressDialog!!.setMessage("Adding....")
                progressDialog!!.setCancelable(false)
                progressDialog!!.show()

                employee.EmpId = edittext_id.text.toString().trim()
                employee.Name = textView_name.text.toString().trim()
                employee.ActivationCode = "abc"
                employee.Email = acct!!.email.toString().trim()
                employee.Verified = false
                addEmployee(employee)
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        finishAffinity()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this@RegistrationActivity, gso)
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(this) {
                //Toast.makeText(applicationContext, "Successfully signed out", Toast.LENGTH_LONG).show()
                startActivity(Intent(applicationContext, SignIn::class.java))
                finish()
            }

    }

    fun addEmployee(employee: Employee) {
        val service = Servicebuilder.buildService(ConferenceService::class.java)
        val requestCall: Call<Int> = service.addEmployee(employee)
        requestCall.enqueue(object : Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                progressDialog!!.dismiss()
                Toast.makeText(applicationContext, "on failure in registration ${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    progressDialog!!.dismiss()
                    Toast.makeText(applicationContext, "Information added Successfully", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@RegistrationActivity, Main2Activity::class.java))
                    finish()
                } else {
                    progressDialog!!.dismiss()
                    Toast.makeText(applicationContext, "Response is wrong ${response.body()}", Toast.LENGTH_LONG).show()
                }

            }
        })
    }
}