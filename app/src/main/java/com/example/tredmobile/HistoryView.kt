package com.example.tredmobile

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class HistoryView: View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    //scale value
    private val scaleValue = context.resources.displayMetrics.density

    //paints
    //background paint
    private val backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.rgb(41, 41, 41)
        textSize = 20f * scaleValue
        typeface = Typeface.SANS_SERIF
        isFakeBoldText = true
    }

    private val cardPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.rgb(59, 59, 59)
    }

    private val orangePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.rgb(255, 107, 0)
    }

    private val orangeTextPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(255, 107, 0)
        textAlign = Paint.Align.CENTER
        textSize = 30f * scaleValue
        typeface = Typeface.SANS_SERIF
        isFakeBoldText = true
    }

    private val bluePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.rgb(25, 123, 189)
    }

    private val snowPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.rgb(248, 243, 243)
        textSize = 20f * scaleValue
        typeface = Typeface.SANS_SERIF
        isFakeBoldText = true
    }

    private val snowPaintSmall: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.rgb(248, 243, 243)
        textSize = 15f * scaleValue
        typeface = Typeface.SANS_SERIF
    }

    private val boldNavPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 28f
        color = Color.rgb(248, 243, 243)
    }

    private val navPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 28f
        color = Color.rgb(117, 117, 117)
    }

    private val linePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.SQUARE
        strokeWidth = 8f
        color = Color.rgb(117, 117, 117)
    }

    //canvas size
    private val canvasHeight: Float
        get() = height.toFloat()
    private val canvasWidth: Float
        get() = width.toFloat()


    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, canvasWidth, canvasHeight, backgroundPaint)
        drawTopBar(canvas)
        drawCards(canvas)
        drawBottomNav(canvas)
        drawHistoryData(canvas)
    }

    private fun drawTopBar(canvas: Canvas) {
        canvas.drawRect(0f, 0f, canvasWidth, canvasHeight / 12f, snowPaint)
        canvas.drawText("- History -", canvasWidth / 2f, canvasHeight / 17f, orangeTextPaint)
    }

    private fun drawCards(canvas: Canvas) {
        val heightDivider: Float = canvasHeight / 30f
        //history card
        canvas.drawRoundRect(canvasWidth / 30f, heightDivider * 3, canvasWidth - (canvasWidth / 30f), heightDivider * 28, 30f, 30f, cardPaint)
        for (i in 1 until 10) {
            canvas.drawLine(canvasWidth / 15f, heightDivider * (3 + (i * 2.5f)), canvasWidth - (canvasWidth / 15f), heightDivider * (3 + (i * 2.5f)), linePaint)
        }
    }

    private fun drawBottomNav(canvas: Canvas) {
        val xSep: Float  = canvasWidth / 20f
        val offset: Float = canvasWidth / 2.22f
        var dotPaint: Paint
        for (i in 0 until 3) {
            dotPaint = if (i != 0) {
                navPaint
            } else {
                boldNavPaint
            }
            canvas.drawPoint((i * xSep) + offset, canvasHeight - (canvasHeight / 35f), dotPaint)
        }
    }

    private fun drawHistoryData(canvas: Canvas) {
        val heightDivider: Float = canvasHeight / 30f
        val stepsArray = arrayOf(6090, 2030, 12445, 4533, 9875, 12123, 5432, 3442, 8764, 8643)
        val distanceArray = arrayOf(3.01, 1.03, 6.21, 2.26, 4.98, 6.10, 2.23, 1.53, 4.34, 4.30)
        val datesArray = arrayOf("03-05-22", "04-05-22","05-05-22","06-05-22","07-05-22","08-05-22","09-05-22","10-05-22","11-05-22","12-05-22")
        for (i in 0 until 10) {
            canvas.drawText(datesArray[i], canvasWidth / 15f, heightDivider * ((i + 1.65f) * 2.5f), snowPaint)
            canvas.drawText((stepsArray[i].toString() + " Steps"), canvasWidth / 15f, heightDivider * ((i + 2.05f) * 2.5f), snowPaintSmall)
            canvas.drawText(distanceArray[i].toString() + " mi", canvasWidth / 2.8f, heightDivider * ((i + 2.05f) * 2.5f), snowPaintSmall)
        }
    }
}