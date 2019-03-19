package com.example.conferencerommapp.Activity

import android.os.Bundle
import android.app.SearchManager
import android.content.Context
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.conferencerommapp.Helper.CheckBoxAdapter
import com.example.conferencerommapp.Model.EmployeeList
import com.example.conferencerommapp.R
import kotlinx.android.synthetic.main.activity_check_box_with_recycler_view.*

class CheckBoxRecycler : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_box_recycler)

        var names = ArrayList<EmployeeList>()
        for(i in 0..10) {
            var emp = EmployeeList()
            emp.Name = "Prateek${i + 1}"
            emp.Email = "${emp.Name}@nineleaps.com "
            names.add(emp)
        }
        checkBox_recycler_view.adapter = CheckBoxAdapter(names, this@CheckBoxRecycler)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        var inflater = menuInflater
//        inflater.inflate(R.menu.activity_main_drawer, menu)
//
//        var searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        var searchView: SearchView = menu!!.findItem(R.id.search).actionView as SearchView
//        searchView.setSearchableInfo(
//            searchManager.getSearchableInfo(componentName)
//        )
//
//        searchView.setIconifiedByDefault(false)
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//
//                return false
//            }
//            override fun onQueryTextChange(newText: String): Boolean {
//
//
//                return false
//            }
//        })
//        return false
//    }
}

