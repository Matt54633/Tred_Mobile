package com.example.tredmobile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs



class MainActivity : AppCompatActivity(), SensorEventListener {

    val tredMobilePath: String get() =  this.applicationInfo.dataDir.toString()
    val tredStorageDir: String  get() = "$tredMobilePath/TredStorage"

    private lateinit var detector: GestureDetectorCompat

    private var sensorManager: SensorManager? = null

    // variable gives the running status
    private var running = false

    // variable counts total steps
    private var totalSteps = 0f

    private val ACTIVITY_RECOGNITION_REQUEST_CODE = 100

    lateinit var barList:ArrayList<BarEntry>

    lateinit var barDataSet: BarDataSet
    lateinit var barData: BarData

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //json



        //Log.d("Spatula", "Size: ${users.data.size}")



        createFile()
        //json
        val settingsButton = findViewById<ImageView>(R.id.settingsLogo)
        settingsButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }


        //check if permission isn't already granted, request the permission
        if (isPermissionGranted()) {
            requestPermission()
        }

        detector = GestureDetectorCompat(this, MainGestureListener())


        //initializing sensorManager instance
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager


        barList = ArrayList()
        barList.add(BarEntry(10f, 6090f))
        barList.add(BarEntry(15f, 2030f))
        barList.add(BarEntry(20f, 12445f))
        barList.add(BarEntry(25f, 4533f))
        barList.add(BarEntry(30f, 9875f))
        barList.add(BarEntry(35f, 12123f))
        barList.add(BarEntry(40f, 5432f))
        barDataSet= BarDataSet(barList, "Steps")
        barData= BarData(barDataSet)
        barDataSet.setColor(Color.parseColor("#197BBD"), 255)
        var barchart = findViewById<BarChart>(R.id.barchart)
        barData.barWidth = 2f
        barchart.setTouchEnabled(false)
        barchart.xAxis.textColor = Color.WHITE
        barchart.xAxis.setDrawGridLines(false)
        barchart.axisLeft.setDrawGridLines(false)
        barchart.axisRight.setDrawGridLines(false)
        barchart.legend.isEnabled = false
        barchart.description.isEnabled = false
        barchart.axisLeft.textColor = Color.WHITE
        barchart.axisRight.textColor = Color.argb(0, 255, 255, 255)
        barchart.data = barData
        barDataSet.valueTextColor= Color.argb(0, 255, 255, 255)
        barDataSet.valueTextSize=15f


        barList = ArrayList()
        barList.add(BarEntry(10f, 3.01f))
        barList.add(BarEntry(15f, 1.03f))
        barList.add(BarEntry(20f, 6.21f))
        barList.add(BarEntry(25f, 2.26f))
        barList.add(BarEntry(30f, 4.98f))
        barList.add(BarEntry(35f, 6.10f))
        barList.add(BarEntry(40f, 2.23f))
        barDataSet= BarDataSet(barList, "Distance Walked")
        barData= BarData(barDataSet)
        barDataSet.setColor(Color.parseColor("#197BBD"), 255)
        var barchart2 = findViewById<BarChart>(R.id.barchart2)
        barData.barWidth = 2f
        barchart2.setTouchEnabled(false)
        barchart2.xAxis.textColor = Color.WHITE
        barchart2.xAxis.setDrawGridLines(false)
        barchart2.axisLeft.setDrawGridLines(false)
        barchart2.axisRight.setDrawGridLines(false)
        barchart2.legend.isEnabled = false
        barchart2.description.isEnabled = false
        barchart2.axisLeft.textColor = Color.WHITE
        barchart2.axisRight.textColor = Color.argb(0, 255, 255, 255)
        barchart2.data = barData
        barDataSet.valueTextColor= Color.argb(0, 255, 255, 255)
        barDataSet.valueTextSize=15f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (detector.onTouchEvent(event)) {
            true
        }else{
            super.onTouchEvent(event)
        }
    }

    inner class MainGestureListener: GestureDetector.SimpleOnGestureListener(){

        private val SwipeThreshold = 100
        private val SwipeVelocityThreshold = 100

        override fun onFling(
            downEvent: MotionEvent?,
            moveEvent: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var diffX = downEvent?.let { moveEvent?.x?.minus(it.x) } ?: 0.0F
            var diffY = moveEvent?.let { moveEvent.y.minus(it.y) } ?: 0.0F

            return if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > SwipeThreshold && abs(velocityX) > SwipeVelocityThreshold) {
                    if (diffX > 0) {
                        this@MainActivity.onSwipeRight()
                    } else {
                        this@MainActivity.onSwipeLeft()
                    }
                    true
                } else {
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            } else {
                if (abs(diffY) > SwipeThreshold && abs(velocityY) > SwipeVelocityThreshold) {
                    if (diffY > 0) {
                        this@MainActivity.onSwipeDown()
                    } else {
                        this@MainActivity.onSwipeUp()
                    }
                    true
                } else {
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            }
        }
    }

    private fun onSwipeUp() {
        Toast.makeText(this, "swipe up", Toast.LENGTH_LONG).show()
    }

    private fun onSwipeDown() {
        Toast.makeText(this, "swipe down", Toast.LENGTH_LONG).show()
    }

    private fun onSwipeLeft() {
        //Toast.makeText(this, "swipe left", Toast.LENGTH_LONG).show()
        val intent = Intent(this, RewardsActivity::class.java)
        startActivity(intent)
    }

    private fun onSwipeRight() {
        //Toast.makeText(this, "swipe right", Toast.LENGTH_LONG).show()
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)

    }

    override fun onResume() {

        super.onResume()
        running = true

        // TYPE_STEP_COUNTER:  A constant describing a step counter sensor
        // Returns the number of steps taken by the user since the last reboot while activated
        // This sensor requires permission android.permission.ACTIVITY_RECOGNITION.
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)


        if (stepSensor == null) {
            // show toast message, if there is no sensor in the device
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            // register listener with sensorManager
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        //unregister listener
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        // get textview by its id
        var tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)


        if (running) {

            //get the number of steps taken by the user.
            totalSteps = event!!.values[0]

            val currentSteps = totalSteps.toInt()

            // set current steps in textview
            tv_stepsTaken.text = ("$currentSteps")

            var milesWalked = findViewById<TextView>(R.id.milesWalked)

            milesWalked.text = String.format("%.2f", totalSteps*2.5f/5280) + "mi"
            Log.d("MainActivity", event.values[0].toString())
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        println("onAccuracyChanged: Sensor: $sensor; accuracy: $accuracy")
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                ACTIVITY_RECOGNITION_REQUEST_CODE
            )
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) != PackageManager.PERMISSION_GRANTED
    }

    //handle requested permission result(allow or deny)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACTIVITY_RECOGNITION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }
            }
        }
    }


    ///JSON FUNctions



//Creates folder if not there and creates dummy data file to be later accessed.

    fun createFile(){

        val fileName = "stepStorageFileJSON.json"
        val path = Paths.get(tredStorageDir)

        if (tredStorageDir != null) {
            if(!Files.isDirectory(path)){
                Files.createDirectory(path)
            }
        }

        val file = FileWriter("$tredStorageDir/$fileName/")
        file.write("{\"User\":[{\"steps\":2000},{\"steps\":23454},{\"steps\":12345},{\"steps\":69},{\"steps\":8000}]}") //to give dummy data
        file.flush()
        file.close()

    }



    fun lastDaySteps() {
        //TODO("grab the last entry from stepStorageFileJSON.json")
    }

    fun last7days() {
        //TODO ("Grab the last 7")
    }





}