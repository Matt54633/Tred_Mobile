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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
//import jdk.nashorn.internal.parser.JSONParser
import org.json.JSONObject
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
        val jsonString = loadJson(this)
        Log.d("tomato", jsonString.toString())


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
        barList.add(BarEntry(10f, 500f))
        barList.add(BarEntry(20f, 400f))
        barList.add(BarEntry(30f, 300f))
        barList.add(BarEntry(40f, 200f))
        barList.add(BarEntry(50f, 100f))
        barDataSet= BarDataSet(barList, "Population")
        barData= BarData(barDataSet)
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS, 250)
        var barchart = findViewById<BarChart>(R.id.barchart)
        barchart.data = barData
        barDataSet.valueTextColor= Color.BLACK
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
    //does work, but had to give up working with the asset folder as they're read-only, which I discovered after 2 hours.
    private fun loadJson(context: Context): String? {
        var input: InputStream? = null
        var jsonString: String

        try {
            // Create InputStream
            input = context.assets.open("jsonData.json")

            val size = input.available()

            // Create a buffer with the size
            val buffer = ByteArray(size)

            // Read data from InputStream into the Buffer
            input.read(buffer)

            // Create a json String
            jsonString = String(buffer)
            return jsonString;
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            // Must close the stream
            input?.close()
        }

        return null
    }
    //original method of appending to list, this was fora 2 dimensional array
    // from https://stackoverflow.com/questions/44416855/adding-json-objects-to-existing-json-file
    fun addObject(path: String, name: String, value: String) {
        val gson = Gson()
        val reader: FileReader = FileReader(File(path))
        val type = object : TypeToken<Map<String, String>>() {}.type
        System.out.println("Type: " + type.toString())
        val existingJson = gson.fromJson<Map<String, String>>(JsonReader(reader), type)
        System.out.println("Existing Json: ${existingJson}")
        val newJsonMap = existingJson.plus(Pair(name, value))
        FileWriter(File(path)).use(
            { writer -> writer.write(gson.toJson(newJsonMap)) }
        )
    }


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

    fun appendSteps() {

        //TODO("
        // Read in json file as an object
        // Add a new entry to object
        // convert back into string and rewrite file
        // Was attempting with Gson and json-simple
        // ")

        class stepsTaken {
            var steps: String? = null

            constructor() : super() {}

            constructor(
                steps: String

            ) : super() {
                this.steps = steps

            }
        }
        //I was unable to get JSONPARSER() working
        //idea from https://stackoverflow.com/questions/54165223/appending-jsonobjects-when-writing-to-a-file
        /*
        val parser = JSONParser()
        var stepsJson: JSONObject? = null
        try {
            stepsJson = parser.parse(FileReader("$tredStorageDir/stepStorageFileJSON.json"))
        } catch (ex: ParseException) {
            ex.printStackTrace()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

         */

    }

    fun lastDaySteps() {
        //TODO("grab the last entry from stepStorageFileJSON.json")
    }

    fun last7days() {
        //TODO ("Grab the last 7")
    }





}