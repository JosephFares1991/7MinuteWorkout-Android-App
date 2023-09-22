package com.example.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FinishActivity : AppCompatActivity() {
    lateinit var toolbarFinishActivity:Toolbar
    lateinit var finishBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)
        finishBtn = findViewById(R.id.btnFinish)
        toolbarFinishActivity = findViewById<Toolbar>(R.id.toolbar_finish_activity)

        setSupportActionBar(toolbarFinishActivity)
        val actionBar = supportActionBar
       actionBar?.let {
           actionBar.setDisplayHomeAsUpEnabled(true)
       }

        toolbarFinishActivity.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        finishBtn.setOnClickListener {
            finish()

        }
        addDateToDatabase()
    }

    private fun addDateToDatabase(){
        val calender = Calendar.getInstance()
        val dateTime = calender.time
        Log.i("DATE","" + dateTime)

        val sdf = SimpleDateFormat("dd MMM yyy HH:mm:ss",Locale.getDefault())
        val date = sdf.format(dateTime)

        val dbHandler = SqliteOpenHelper(this,null)
        dbHandler.addDate(date)
        Log.i("Date","Added")
    }
}