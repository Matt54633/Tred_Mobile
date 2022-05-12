package com.example.tredmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import kotlin.math.abs

class HistoryActivity : AppCompatActivity() {

    private lateinit var detector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        detector = GestureDetectorCompat(this, HistoryGestureListener())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (detector.onTouchEvent(event)) {
            true
        }else{
            super.onTouchEvent(event)
        }
    }

    inner class HistoryGestureListener: GestureDetector.SimpleOnGestureListener(){

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
                        this@HistoryActivity.onSwipeRight()
                    } else {
                        this@HistoryActivity.onSwipeLeft()
                    }
                    true
                } else {
                    super.onFling(downEvent, moveEvent, velocityX, velocityY)
                }
            } else {
                if (abs(diffY) > SwipeThreshold && abs(velocityY) > SwipeVelocityThreshold) {
                    if (diffY > 0) {
                        this@HistoryActivity.onSwipeDown()
                    } else {
                        this@HistoryActivity.onSwipeUp()
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
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun onSwipeRight() {
        Toast.makeText(this, "swipe right", Toast.LENGTH_LONG).show()

    }
}