package com.example.hogasample.control.hoga

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import androidx.core.content.ContextCompat.getColor
import com.example.hogasample.R
import com.example.hogasample.data.HogaData

class PriceBarItem(var context: Context) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint_font = Paint(Paint.ANTI_ALIAS_FLAG)

    /** Rect */
    var startX = 0.0
    var startY = 0.0

    lateinit var rect_bg: RectF  // 배경 사각형
    lateinit var rect_bar: RectF // Bar 사각형

    var width = 0.0
    var height = 0.0

    /** 데이터 */
    var price = 0
    var amount = 0
    var rate = 0F
    var bgColor = Color.GRAY  // 배경색
    var bdColor = Color.GRAY  // 테두리색
    var barColor: Int? = null // Bar 색상
    var txtColor = Color.GRAY // 글자 색상
    var subTxtColor: Int? = null // 글자 색상
    var type = HogaType.PRICE  // 글자 정렬
    var maxAmount = 0 // 최대값

//    var mark : String? = null // 시 고 저 마크 표시 유무 >  Center Bar Item 에 넣기!!

    fun draw(canvas: Canvas) {
        paint.strokeWidth = 2F
        paint.isAntiAlias = true
        paint.isDither = true

        /// 배경 & 봉 길이 측정
        val barWidth = width * amount / maxAmount

        // 시작점 X,Y ( 왼쪽부터: 매도(Ask) / 오른쪽부터: 매수(Bid) )

        /** 1. 배경색 칠하기 */
        // 1. 색깔 지정
        paint.color = this.bgColor
        // 2. 좌표
        this.rect_bg = RectF(
            startX.toFloat(),
            startY.toFloat(),
            (startX + width).toFloat(),
            (startY + height).toFloat()
        )
        // 3. 그리기
        canvas.drawRect(rect_bg, paint)

        /// 매도 & 매수
        if (type != HogaType.PRICE) {
            /** 2. 봉 칠하기 */
            // 1. 색깔 지정
            paint.color = this.barColor!!
            // 2. 좌표
            val startBarX =
                if (type == HogaType.ASK) (width - barWidth + startX).toFloat() else startX.toFloat()
            val endBarX =
                if (type == HogaType.ASK) (width + startX).toFloat() else (startX + barWidth).toFloat()
            rect_bar = RectF(startBarX, startY.toFloat(), endBarX, (startY + height).toFloat())

            // 3. 그리기
            canvas.drawRect(rect_bar, paint)

            /** 3. 텍스트 그리기 */
            // 1. 색깔 지정
            paint_font.color = txtColor //this.txtColor
            paint_font.textSize = 40F
            paint_font.style = Paint.Style.FILL

            // 2. 좌표
            // 3. 그리기
            /**
             * TODO: Y축(높이) 기준으로 리사이즈 동시 적용시켜야 한다.
             */
            //텍스트 정렬
            var textWidth = paint_font.measureText(amount.toString())
            var textHeight = paint_font.ascent()
            canvas.drawText(
                amount.toString(),
                (startX + width / 2 - textWidth / 2).toFloat(),
                ((height - textHeight) / 2 + startY).toFloat(),
                paint_font
            )
        }
        /// 가격
        else {
            /** 3. 텍스트 그리기 */
            // 1. 색깔 지정
            paint_font.color = txtColor //this.txtColor
            paint_font.textSize = 42F
            paint_font.style = Paint.Style.FILL
            paint_font.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)

            // 2. 좌표
            // 3. 그리기

            /**
             * TODO: Y축(높이) 기준으로 리사이즈 동시 적용시켜야 한다.
             */
            //텍스트 정렬
            var textWidth = paint_font.measureText(amount.toString())
            var textHeight = paint_font.ascent()

            canvas.drawText(
                price.toString(),
//                (startX * 1.5 - textWidth / 2).toFloat(),
                (startX + width / 2 - textWidth / 2).toFloat(),
                ((height - textHeight) / 2 + startY).toFloat(),
                paint_font
            )

            /// 증감율
            paint_font.color = subTxtColor!!
            paint_font.textSize = 22F
            textWidth = paint_font.measureText(rate.toString() + "%")
            textHeight = paint_font.ascent()

            canvas.drawText(
                rate.toString() + "%",
//                (startX * 2 - textWidth).toFloat(),
                (width + startX - textWidth - 10).toFloat(),
                ((height - textHeight) / 2 + startY).toFloat(),
                paint_font
            )
        }
        /** 4. 테두리 칠하기 */
        var borderPaint = Paint()
        borderPaint.style = Paint.Style.STROKE
        borderPaint.color = bdColor
        borderPaint.strokeWidth = 2F
        canvas.drawRect(rect_bg, borderPaint)
    }


    fun setData(
        item: HogaData,
        type: HogaType,
        max: Int
    ) {
        this.type = type
        this.price = item.price
        this.amount = item.amount
        this.rate = item.rate
        this.maxAmount = max

        if (type == HogaType.ASK) {
            this.bgColor = getColor(context, R.color.hogaBgBlue)
            this.barColor = getColor(context, R.color.hogaBarBlue)
            this.txtColor = getColor(context, R.color.hogaTxtBlue)
        } else if (type == HogaType.BID) {
            this.bgColor = getColor(context, R.color.hogaBgRed)
            this.barColor = getColor(context, R.color.hogaBarRed)
            this.txtColor = getColor(context, R.color.hogaTxtRed)
        } else {
            this.bgColor = getColor(context, R.color.hogaBgWhite)
            this.txtColor = getColor(context, R.color.hogaTxtRed)
            this.subTxtColor = getColor(context, R.color.hogaTxtGray)
        }
    }

    fun setSize(width: Double, height: Double) {
        this.width = width
        this.height = height
    }

    fun setPosition(startX: Double, startY: Double) {
        this.startX = startX
        this.startY = startY
    }
}

enum class HogaType {
    ASK { // 매도
        override fun value(): String {
            return "ASK"
        }
    },
    BID { // 매수
        override fun value(): String {
            return "BID"
        }
    },
    PRICE { // 가격
        override fun value(): String {
            return "PRICE"
        }
    };

    abstract fun value(): String
}