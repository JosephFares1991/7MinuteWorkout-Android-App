package com.example.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputLayout
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    lateinit var rgUnits:RadioGroup
    lateinit var rbMetricsUnits:RadioButton
    lateinit var rbUSUnits:RadioButton
    lateinit var tilMetricUnitWeight:TextInputLayout
    lateinit var etMetricUnitWeight:AppCompatEditText
    lateinit var tilMetricUnitHeight:TextInputLayout
    lateinit var etMetricUnitHeight:AppCompatEditText
    lateinit var tilUSUnitWeight:TextInputLayout
    lateinit var etUSUnitWeight:AppCompatEditText
    lateinit var etUSUnitHeight:AppCompatEditText
    lateinit var tilUsUnitHeightFeet:TextInputLayout
    lateinit var etUsUnitHeightFeet:AppCompatEditText
    lateinit var tilUsUnitHeightInch:TextInputLayout
    lateinit var etUsUnitHeightInch:AppCompatEditText
    lateinit var tvYourBMI:TextView
    lateinit var tvBMIValue:TextView
    lateinit var tvBMIType:TextView
    lateinit var tvBMIDescription:TextView
    lateinit var btnCalculateUnits:Button
    lateinit var llsUnitsHeight:LinearLayout
    lateinit var llDisplayBMIResult:LinearLayout
    val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val US_UNIT_VIEW = "US_UNIT_VIEW"
    var currentVisibleView = METRIC_UNITS_VIEW



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiactivity)

        rgUnits = findViewById(R.id.rgUnits)
        rbMetricsUnits = findViewById(R.id.rbMetricUnits)
        rbUSUnits = findViewById(R.id.rbUsUnits)
        tilMetricUnitWeight = findViewById(R.id.tilMetricUnitWeight)
        etMetricUnitWeight = findViewById(R.id.etMetricUnitWeight)
        tilMetricUnitHeight = findViewById(R.id.tilMetricUnitHeight)
        etMetricUnitHeight = findViewById(R.id.etMetricUnitHeight)
        tilUSUnitWeight = findViewById(R.id.tilUsUnitWeight)
        etUSUnitWeight = findViewById(R.id.etUsUnitWeight)
        tilUsUnitHeightFeet = findViewById(R.id.tilUsUnitHeightFeet)
        etUsUnitHeightFeet = findViewById(R.id.etUsUnitHeightFeet)
        tilUsUnitHeightInch = findViewById(R.id.tilUsUnitHeightInch)
        etUsUnitHeightInch = findViewById(R.id.etUsUnitHeightInch)
        tvYourBMI = findViewById(R.id.tvYourBMI)
        tvBMIValue = findViewById(R.id.tvBMIValue)
        tvBMIType = findViewById(R.id.tvBMIType)
        tvBMIDescription = findViewById(R.id.tvBMIDescription)
        btnCalculateUnits = findViewById(R.id.btnCalculateUnits)
        llsUnitsHeight = findViewById(R.id.llUsUnitsHeight)
        llDisplayBMIResult = findViewById(R.id.llDisplayBMIResult)


        val toolBarBMIActivity = findViewById<Toolbar>(R.id.toolbar_bmi_activity)
        setSupportActionBar(toolBarBMIActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolBarBMIActivity.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnCalculateUnits.setOnClickListener {
            if(currentVisibleView.equals(METRIC_UNITS_VIEW)){
                if(validateMetricUnits()){
                    val heightValue:Float = etMetricUnitHeight.text.toString().toFloat() / 100
                    val weightValue:Float = etMetricUnitWeight.text.toString().toFloat()
                    val bmi:Float = weightValue / (heightValue * heightValue)
                    displayBMIResult(bmi)

                }else{
                    Toast.makeText(this@BMIActivity,"Please enter valid values",Toast.LENGTH_SHORT).show()
                }
            }else{
                if(validateUSUnits()){
                    val usUnitHeightFeet:String = etUsUnitHeightFeet.text.toString()
                    val usUnitHeightInch:String = etUsUnitHeightInch.text.toString()
                    val usUnitWeightValue:Float = etUSUnitWeight.text.toString().toFloat()

                    val heightValue = usUnitHeightInch.toFloat() + usUnitHeightFeet.toFloat()*12
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                    displayBMIResult(bmi)

                }else{
                    Toast.makeText(this@BMIActivity,"Please enter valid values",Toast.LENGTH_SHORT).show()

                }
            }

        }
        makeVisibleMetricUnitView()
        rgUnits.setOnCheckedChangeListener{
                group, checkedId ->
            if(checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitView()
            }else{
                makeVisibleUSUnitView()
            }
        }
    }

    private fun displayBMIResult(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (java.lang.Float.compare(bmi, 15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 15f) > 0 && java.lang.Float.compare(
                bmi,
                16f
            ) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 16f) > 0 && java.lang.Float.compare(
                bmi,
                18.5f
            ) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 18.5f) > 0 && java.lang.Float.compare(
                bmi,
                25f
            ) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 30f) > 0 && java.lang.Float.compare(
                bmi,
                35f
            ) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 35f) > 0 && java.lang.Float.compare(
                bmi,
                40f
            ) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        llDisplayBMIResult.visibility = View.VISIBLE


        // This is used to round of the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tvBMIValue.text = bmiValue // Value is set to TextView
        tvBMIType.text = bmiLabel // Label is set to TextView
        tvBMIDescription.text = bmiDescription // Description is set to TextView
    }


    private fun validateMetricUnits():Boolean{
        var isValid = true
        if(etMetricUnitWeight.text.toString().isEmpty()){
            isValid = false
        }else if(etMetricUnitHeight.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun validateUSUnits():Boolean{
        var isValid = true
        if(etUsUnitHeightFeet.text.toString().isEmpty()){
            isValid = false
        }else if(etUsUnitHeightInch.text.toString().isEmpty()){
            isValid = false
        }else if(etUSUnitWeight.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun makeVisibleMetricUnitView(){
        currentVisibleView = METRIC_UNITS_VIEW
        tilMetricUnitWeight.visibility = View.VISIBLE
        tilMetricUnitHeight.visibility = View.VISIBLE

        etMetricUnitWeight.text!!.clear()
        etMetricUnitHeight.text!!.clear()

        tilUSUnitWeight.visibility = View.GONE
        llsUnitsHeight.visibility = View.GONE
        llDisplayBMIResult.visibility = View.GONE
    }

    private fun makeVisibleUSUnitView(){
        currentVisibleView = US_UNIT_VIEW
        tilMetricUnitWeight.visibility = View.GONE
        tilMetricUnitHeight.visibility = View.GONE

        etMetricUnitWeight.text!!.clear()
        etUsUnitHeightFeet.text!!.clear()
        etUsUnitHeightInch.text!!.clear()

        tilUSUnitWeight.visibility = View.VISIBLE
        llsUnitsHeight.visibility = View.VISIBLE
        llDisplayBMIResult.visibility = View.GONE

    }
}