package com.example.hogasample.control.hoga

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import com.example.hogasample.R
import com.example.hogasample.data.HogaDataSet
import com.example.hogasample.util.Constants

class HogaLayout : LinearLayout {
    var hogaDataSet: HogaDataSet? = null
    var hogaType: String? = Constants.HOGA_TYPE_NORMAL
    lateinit var hogaLayout : TableLayout

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        if(context != null && attrs != null){
            val v: View = LayoutInflater.from(context).inflate(R.layout.hoga_layout, this, true)
            hogaLayout = v.findViewById(R.id.hogaLayout)
        }
    }

    constructor(context: Context?) : super(context){
        if(context != null){
            val v: View = LayoutInflater.from(context).inflate(R.layout.hoga_layout, this, true)
            hogaLayout = v.findViewById(R.id.hogaLayout)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
//        if(changed && hogaDataSet != null){
//            /// 현재가
//            var dataSet = hogaDataSet!!
//            var topLeft = LinearLayout(context)
//            var topCenter = LinearLayout(context)
//            var topRight = LinearLayout(context)
//
//            var botLeft = LinearLayout(context)
//            var botCenter = LinearLayout(context)
//            var botRight = LinearLayout(context)
//
//            for (data in dataSet.data) {
//                val bongItem = PriceBarItem(context)
//
//                bongItem.layoutParams = LayoutParams(
//                    (width / 3),  /// TODO : 동적으로 영역 조정 필요
//                    (height / 10) /// TODO : 10호가 5호가 등에 따른 동적처리 필요
//                )
//                bongItem.setData(data.value, Paint.Align.RIGHT, dataSet.maxAmount)
//
//                bongItem.requestLayout()
//                topLeft.addView(bongItem)
//            }
//            hogaLayout.addView(topLeft)
//        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if(hogaDataSet != null){
            /// 현재가
            var dataSet = hogaDataSet!!

            // DrawItem Param
//            Rect // 위치 & 사이즈
//            Paint // 색상
//            dataSet // 데이터

            var startX = 0.0 + paddingLeft
            var startY = 0.0 + paddingTop
            var endX = 0.0 + paddingRight
            var endY = 0.0 + paddingRight

            var vWidth = width - paddingLeft - paddingRight
            var vHeight = height - paddingTop - paddingBottom
            /** 매도 */
            for (i in 0..9 step(1)){
                val data = dataSet.data.get("ask_" + (i+1))!!
                val bongItem = PriceBarItem(context)
                bongItem.setData(data, HogaType.ASK, dataSet.maxAmount)
                bongItem.setSize((vWidth / 3).toDouble(), (vHeight / 20).toDouble())
                bongItem.setPosition(startX, startY)
                bongItem.draw(canvas)

                /** 가격 */
                val priceItem = PriceBarItem(context)
                priceItem.setData(data, HogaType.PRICE, dataSet.maxAmount)
                priceItem.setSize((vWidth / 3).toDouble(), (vHeight / 20).toDouble())
                priceItem.setPosition((startX + vWidth / 3).toDouble(), startY)
                priceItem.draw(canvas)

                startY += vHeight / 20
            }
            /** 매수 */
            for (i in 0..9 step(1)){
                val data = dataSet.data.get("bid_" + (i+1))!!
                val bongItem = PriceBarItem(context)
                bongItem.setData(data, HogaType.BID, dataSet.maxAmount)
                bongItem.setSize((vWidth / 3).toDouble(), (vHeight / 20).toDouble())
                bongItem.setPosition((startX + vWidth / 3 * 2).toDouble(), startY)
                bongItem.draw(canvas)

                /** 가격 */
                val priceItem = PriceBarItem(context)
                priceItem.setData(data, HogaType.PRICE, dataSet.maxAmount)
                priceItem.setSize((vWidth / 3).toDouble(), (vHeight / 20).toDouble())
                priceItem.setPosition((startX + vWidth / 3), startY)
                priceItem.draw(canvas)

                startY += vHeight / 20
            }
            /** 가격 */
            for (i in 0..19 step(1)){

            }
        }
    }

    fun setData(hogaDataSet: HogaDataSet, hogaType : String?){
        this.hogaDataSet = hogaDataSet
        if(hogaType != null){
            this.hogaType = hogaType
        }
    }
}