package com.example.a7minuteworkout

import android.annotation.SuppressLint
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {
    lateinit var tvHistory:TextView
    lateinit var rvHistory:RecyclerView
    lateinit var tvNoDataAvailable:TextView

    @SuppressLint("AppCompatMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        tvHistory = findViewById(R.id.tvHistory)
        rvHistory = findViewById(R.id.rvHistory)
        tvNoDataAvailable = findViewById(R.id.tvNoDataAvailable)

        val toolBarBMIActivity = findViewById<Toolbar>(R.id.toolbar_history_activity)
        setSupportActionBar(toolBarBMIActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolBarBMIActivity.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        getAllCompletedDates()
    }

    private fun getAllCompletedDates(){
        val dbHandler = SqliteOpenHelper(this,null)
        val allCompletedDatesList = dbHandler.getAllCompletedDatesList()

        if(allCompletedDatesList.size > 0){
            tvHistory.visibility = View.VISIBLE
            rvHistory.visibility = View.VISIBLE
            tvNoDataAvailable.visibility = View.GONE
            rvHistory.layoutManager = LinearLayoutManager(this)
            val historyAdapter = HistoryAdapter(this, allCompletedDatesList)
            rvHistory.adapter = historyAdapter

        }else{
            tvNoDataAvailable.visibility = View.VISIBLE
            tvHistory.visibility = View.GONE
            rvHistory.visibility = View.GONE
        }
    }
}