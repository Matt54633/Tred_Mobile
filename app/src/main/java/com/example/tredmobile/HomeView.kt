package com.example.tredmobile



import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.util.Log

class HomeView: View {
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
    }

    //canvas size
    private val canvasHeight: Float
        get() = height.toFloat()
    private val canvasWidth: Float
        get() = width.toFloat()


    companion object {
        const val LOGTAG = "MyTask"
    }

    //onDraw function
    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, canvasWidth, canvasHeight, backgroundPaint)
        drawTopBar(canvas)
        drawCards(canvas)
    }

    private fun drawTopBar(canvas: Canvas) {
        canvas.drawRect(0f, 0f, canvasWidth, canvasHeight / 12f, snowPaint)
        canvas.drawText("- Today -", canvasWidth / 2f, canvasHeight / 17f, orangeTextPaint)
    }

    private fun drawCards(canvas: Canvas) {
        val heightDivider: Float = canvasHeight / 30f
        canvas.drawRoundRect(canvasWidth / 30f, heightDivider * 4, canvasWidth - (canvasWidth / 30f), heightDivider * 9, 30f, 30f, cardPaint)
        canvas.drawRoundRect(canvasWidth / 30f, heightDivider * 10, canvasWidth - (canvasWidth / 30f), heightDivider * 15, 30f, 30f, cardPaint)
        canvas.drawRoundRect(canvasWidth / 30f, heightDivider * 16, canvasWidth - (canvasWidth / 30f), heightDivider * 22, 30f, 30f, cardPaint)
        canvas.drawRoundRect(canvasWidth / 30f, heightDivider * 23, canvasWidth - (canvasWidth / 30f), heightDivider * 29, 30f, 30f, cardPaint)
    }


}