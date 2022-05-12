package com.example.tredmobile

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class RewardsView: View {
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
        textSize = 18f * scaleValue
        typeface = Typeface.SANS_SERIF
        textAlign = Paint.Align.CENTER
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
    }

    private fun drawTopBar(canvas: Canvas) {
        canvas.drawRect(0f, 0f, canvasWidth, canvasHeight / 12f, snowPaint)
        canvas.drawText("- Rewards -", canvasWidth / 2f, canvasHeight / 17f, orangeTextPaint)
    }

    private fun drawCards(canvas: Canvas) {
        val heightDivider: Float = canvasHeight / 30f
        //trophies earnt card
        canvas.drawRoundRect(canvasWidth / 30f, heightDivider * 3, canvasWidth - (canvasWidth / 30f), heightDivider * 5, 30f, 30f, cardPaint)
        canvas.drawText("Across the last 7 day you earnt: 7 Trophies", canvasWidth / 2f, (heightDivider * 4.3).toFloat(), snowPaint)
        //trophy display card
        canvas.drawRoundRect(canvasWidth / 30f, heightDivider * 6, canvasWidth - (canvasWidth / 30f), heightDivider * 11, 30f, 30f, cardPaint)
        //wallet card
        canvas.drawRoundRect(canvasWidth / 30f, heightDivider * 12, canvasWidth - (canvasWidth / 30f), heightDivider * 14, 30f, 30f, cardPaint)
        canvas.drawText("5 | Tred Bucks", canvasWidth / 2f, (heightDivider * 13.3).toFloat(), snowPaint)
        //redeemable vouchers card
        canvas.drawRoundRect(canvasWidth / 30f, heightDivider * 15, canvasWidth - (canvasWidth / 30f), heightDivider * 21, 30f, 30f, cardPaint)
        canvas.drawText("Redeemable Vouchers", canvasWidth / 2f, heightDivider * 16, snowPaint)
        //redeemed vouchers card
        canvas.drawRoundRect(canvasWidth / 30f, heightDivider * 22, canvasWidth - (canvasWidth / 30f), heightDivider * 28, 30f, 30f, cardPaint)
        canvas.drawText("My Vouchers", canvasWidth / 2f, heightDivider * 23, snowPaint)
    }
    private fun drawBottomNav(canvas: Canvas) {
        val xSep: Float  = canvasWidth / 20f
        val offset: Float = canvasWidth / 2.22f
        var dotPaint: Paint
        for (i in 0 until 3) {
            dotPaint = if (i != 2) {
                navPaint
            } else {
                boldNavPaint
            }
            canvas.drawPoint((i * xSep) + offset, canvasHeight - (canvasHeight / 35f), dotPaint)
        }
    }
}