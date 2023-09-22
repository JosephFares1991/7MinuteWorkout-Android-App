package com.example.a7minuteworkout

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(),TextToSpeech.OnInitListener {

    private var restTimer:CountDownTimer?=null
    private var restProgress = 0
    lateinit var tvTimer:TextView
    lateinit var llRestView:LinearLayout
    lateinit var llExerciseView:LinearLayout
    lateinit var nextExercise:TextView
    lateinit var exerciseTimer:TextView
    lateinit var restProgressBar: ProgressBar
    lateinit var exerciseProgressBar: ProgressBar
    lateinit var ivImage:ImageView
    lateinit var tvExerciseName:TextView
    lateinit var rvExerciseStatus:RecyclerView
    var restViewFinished:Boolean = true

    private lateinit var exerciseList:ArrayList<ExerciseModel>

    private var tts:TextToSpeech? = null
    private var player:MediaPlayer?=null

    private var exerciseAdapter:ExerciseStatusAdapter?=null

    companion object{
        var currentExercisePosition = -1
        fun getCurrentPosition():Int{
            return currentExercisePosition
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        val toolBarExerciseActivity =
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_exercise_activity)

        tvTimer = findViewById(R.id.tvTimer)
        llRestView = findViewById(R.id.llRestView)
        llExerciseView = findViewById(R.id.llExerciseView)
        nextExercise = findViewById(R.id.nextExerciseName)
        exerciseTimer = findViewById(R.id.exerciseTimer)
        restProgressBar = findViewById<ProgressBar>(R.id.progressBar)
        exerciseProgressBar = findViewById<ProgressBar>(R.id.exerciseProgressBar)
        tvExerciseName = findViewById(R.id.tvExerciseName)
        ivImage = findViewById(R.id.ivImage)
        tts = TextToSpeech(this,this)
        currentExercisePosition = -1


        setSupportActionBar(toolBarExerciseActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolBarExerciseActivity.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        exerciseList = Constants.defaultExerciseList()
        setUpRestView()

    }

    override fun onDestroy() {
        restTimer?.let {
            it.cancel()
            restProgress = 0
        }
        tts?.let {
            it.stop()
            it.shutdown()

        }
        player?.let {
            it.stop()
        }
        super.onDestroy()
    }

    private fun setRestProgressBar(start:Long, toastString: String, textView: TextView, progressBar: ProgressBar){

        progressBar.progress = restProgress
        restTimer = object :CountDownTimer(start,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = (start/1000).toInt()-restProgress
                textView.text = "${(start/1000).toInt()-restProgress}"
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity,
                    toastString,
                    Toast.LENGTH_SHORT).show()

                if(restViewFinished){
                    restViewFinished = false
                    llRestView.visibility = INVISIBLE
                    setUpExerciseView()


                }else if(restViewFinished == false && currentExercisePosition<exerciseList.size-1){
                    llExerciseView.visibility = INVISIBLE
                    setUpRestView()
                    rvExerciseStatus.visibility = GONE
                    exerciseList[currentExercisePosition].setIsSelected(false)
                    exerciseList[currentExercisePosition].setIsComplete(true)
                    exerciseAdapter!!.notifyDataSetChanged()

                }else{
                    finish()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    fun setUpRestView(){

        try {
            player = MediaPlayer.create(applicationContext,R.raw.press_start)
            player!!.isLooping = false
            player!!.start()
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }

        restTimer?.cancel()
        restProgress = 0
        llRestView.visibility = VISIBLE
        restViewFinished = true
        nextExercise.text = exerciseList[currentExercisePosition+1].getName()
        setRestProgressBar(2000,"Here now we will start the exercise",tvTimer,restProgressBar)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setUpExerciseView() {
        restTimer?.cancel()
        restProgress = 0
        if(currentExercisePosition<exerciseList.size-1){
            currentExercisePosition++

            exerciseList[currentExercisePosition].setIsSelected(true)
            if(currentExercisePosition>1){
                exerciseList[currentExercisePosition-1].setIsComplete(true)
            }
            setUpExerciseStatusRecyclerView()
            exerciseAdapter!!.notifyDataSetChanged()
            rvExerciseStatus.visibility = VISIBLE
            ivImage.setImageResource(exerciseList[currentExercisePosition].getImage())
            tvExerciseName.text = exerciseList[currentExercisePosition].getName()
            llExerciseView.visibility = VISIBLE
            setRestProgressBar(
                2000,
                "Finish Exercise, Well Done!",
                exerciseTimer,
                exerciseProgressBar
            )
            speakOut(exerciseList[currentExercisePosition].getName())
        }
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "The language specified is not supported")
            }else{
                Log.e("TTS", "Initialization failed!")
            }
        }

    }
    private fun speakOut(text:String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH,null,"")
    }

    private fun setUpExerciseStatusRecyclerView(){
        rvExerciseStatus = findViewById(R.id.rvExerciseStatus)
        rvExerciseStatus.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,
            false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList,this)
        rvExerciseStatus.adapter = exerciseAdapter

    }
    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_configuration)
        val tvYes = customDialog.findViewById<Button>(R.id.tvYes)
        val tvNo = customDialog.findViewById<Button>(R.id.tvNo)
        tvYes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }



}