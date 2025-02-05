package com.example.hogasample.control.hoga

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.hogasample.R
import com.example.hogasample.data.HogaData

class PriceBarItem : View {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint_font = Paint(Paint.ANTI_ALIAS_FLAG)

    var price = 0
    var amount = 0
    var bgColor = Color.GRAY  // 배경색
    var barColor = Color.GRAY // Bar 색상
    var txtColor = Color.GRAY // 글자 색상
    var align = Align.CENTER  // 글자 정렬
    var maxAmount = 0 // 최대값

    lateinit var rect_bg : RectF  // 배경 사각형
    lateinit var rect_bar : RectF // Bar 사각형

//    var mark : String? = null // 시 고 저 마크 표시 유무 >  Center Bar Item 에 넣기!!

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.strokeWidth = 2F
        paint.isAntiAlias = true
        paint.isDither = true

        /// 배경 & 봉 길이 측정
        val bgWidth = (width * (maxAmount - amount) / maxAmount).toFloat()
        val barWidth = (width - bgWidth)

        // 시작점 X,Y (왼쪽부터: 매도 / 오른쪽부터: 매수)
        var startX = 0F // if (align == Align.LEFT) 0F else (width - bgWidth)
        var startY = 0F
        var endX = width.toFloat()
        var endY = height.toFloat()

        /** 1. 배경색 칠하기 */

        // 1. 색깔 지정
        paint.color = this.bgColor
        // 2. 좌표
        this.rect_bg = RectF(startX, startY, endX, endY)
        // 3. 그리기
        canvas.drawRect(rect_bg, paint)

        var startBarX = if (align == Align.LEFT) bgWidth else 0F
        var endBarX = if (align == Align.LEFT) width.toFloat() else barWidth

        /** 2. 봉 칠하기 */
        // 1. 색깔 지정
        paint.color = this.barColor
        // 2. 좌표
        rect_bar = RectF(startBarX, startY, endBarX, endY)
        // 3. 그리기
        canvas.drawRect(rect_bar, paint)

        /** 3. 텍스트 그리기 */
        // 1. 색깔 지정
        paint_font.color = Color.BLACK //this.txtColor
        paint_font.textSize = 40F
        paint_font.style = Paint.Style.FILL


        // 2. 좌표
        /// TODO : 텍스트 좌표 구하기..
        // 3. 그리기
        var x = 0f
        var y = 0f

        /**
         * TODO: Y축(높이) 기준으로 리사이즈 동시 적용시켜야 한다.
         */
        var textWidth = paint_font.measureText(price.toString())
        //텍스트 정렬
        when (this.align) {
            Align.LEFT -> {
                x = rect_bg.right - textWidth - 20 // padding
                y = (rect_bg.height() - paint_font.ascent()) / 2 + rect_bg.top
            }

            Align.CENTER -> {
                x = (rect_bg.width() / 2 - textWidth / 2 + rect_bg.left).toFloat()
                y = (rect_bg.height() - paint_font.ascent()) / 2 + rect_bg.top /* - (mOutLine.getSize() * 2)*/ // Line 적용할 때넣음
            }

            Align.RIGHT -> {
                x = rect_bg.left + 20 // padding
                y = (height - paint_font.ascent()) / 2 + rect_bg.top
            }
        }
        x = (rect_bg.width() / 2 - textWidth / 2 + rect_bg.left).toFloat()
        y = (rect_bg.height() - paint_font.ascent()) / 2 + rect_bg.top /* - (mOutLine.getSize() * 2)*/ // Line 적용할 때넣음
        canvas.drawText(price.toString(), x, y, paint_font)

        /** 4. 테두리 칠하기 */
        var borderPaint = Paint()
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 20F
        canvas.drawRect(rect_bg, borderPaint)
    }


    fun setData(
        item: HogaData,
        align: Align,
        max: Int
    ) {
        this.align = align
        this.price = item.price
        this.amount = item.amount
        this.bgColor = if (item.sign < 0) ContextCompat.getColor(context, R.color.hogaBgBlue) else ContextCompat.getColor(context, R.color.hogaBgRed)
        this.barColor = if (item.sign < 0) ContextCompat.getColor(context, R.color.hogaBarBlue) else ContextCompat.getColor(context, R.color.hogaBarRed)
        this.txtColor = if (item.sign < 0) ContextCompat.getColor(context, R.color.hogaTxtBlue) else ContextCompat.getColor(context, R.color.hogaTxtRed)
        this.maxAmount = max
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}