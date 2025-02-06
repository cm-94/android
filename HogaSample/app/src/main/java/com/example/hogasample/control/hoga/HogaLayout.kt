package com.example.hogasample.control.hoga

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import com.example.hogasample.R
import com.example.hogasample.data.HogaDataSet
import com.example.hogasample.util.Constants
import com.google.android.material.divider.MaterialDivider
import org.json.JSONObject

class HogaLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    var hogaDataSet: HogaDataSet? = null
    var hogaType: String? = null

    private var subView1: ViewGroup? = null
    private var subView2: ViewGroup? = null

    override fun dispatchDraw(canvas: Canvas) {
        /// TODO : 테스트용. 추후 삭제
        setSubView1(createTopRightView())
        setSubView2(createBotLeftView()) //
        /// END ====================

        super.dispatchDraw(canvas)
        hogaDataSet?.let { dataSet ->
            /// SubView1 추가
            val vWidth = (width - paddingLeft - paddingRight).toDouble()
            val vHeight = (height - paddingTop - paddingBottom).toDouble()

            if (subView1 != null) {
                val params = LayoutParams(
                    (vWidth / 3).toInt(),
                    (vHeight / 2).toInt()
                )
                params.marginStart = (vWidth / 3 * 2).toInt()
                subView1!!.layoutParams = params
                subView1!!.setPadding(0, 20, 0, 20)
                this.addView(subView1)
            }

            /// SubView2 추가
            if (subView2 != null) {
                val params = LayoutParams(
                    (vWidth / 3).toInt(),
                    (vHeight / 2).toInt()
                )
                params.topMargin = (vHeight / 2).toInt()
                subView2!!.layoutParams = params
                this.addView(subView2)
            }

            // DrawItem Param
//            Rect    // 위치 & 사이즈
//            Paint   // 색상
//            dataSet // 데이터
            var startX = 0.0 + paddingLeft
            var startY = 0.0 + paddingTop
            var endX = 0.0 + paddingRight
            var endY = 0.0 + paddingRight

            /** 매도 */
            for (i in 0..9 step (1)) {
                val data = dataSet.data.get("ask_" + (i + 1))!!
                val barItem = HogaCellItem(context)
                barItem.setData(data, HogaCellType.ASK, dataSet.maxAmount)
                barItem.setSize((vWidth / 3), (vHeight / 20))
                barItem.setPosition(startX, startY)
                barItem.draw(canvas)

                /** 가격 */
                val priceItem = HogaCellItem(context)
                priceItem.setData(data, HogaCellType.PRICE, dataSet.maxAmount)
                priceItem.setSize((vWidth / 3).toDouble(), (vHeight / 20).toDouble())
                priceItem.setPosition((startX + vWidth / 3), startY)
                priceItem.draw(canvas)

                startY += vHeight / 20
            }
            /** 매수 */
            for (i in 0..9 step (1)) {
                val data = dataSet.data.get("bid_" + (i + 1))!!
                val barItem = HogaCellItem(context)
                barItem.setData(data, HogaCellType.BID, dataSet.maxAmount)
                barItem.setSize((vWidth / 3), (vHeight / 20))
                barItem.setPosition((startX + vWidth / 3 * 2), startY)
                barItem.draw(canvas)

                /** 가격 */
                val priceItem = HogaCellItem(context)
                priceItem.setData(data, HogaCellType.PRICE, dataSet.maxAmount)
                priceItem.setSize((vWidth / 3), (vHeight / 20))
                priceItem.setPosition((startX + vWidth / 3), startY)
                priceItem.draw(canvas)

                startY += vHeight / 20
            }
        }
    }

    fun setData(hogaDataSet: HogaDataSet, hogaType: String?) {
        this.hogaDataSet = hogaDataSet
        if (hogaType != null) {
            this.hogaType = hogaType
        }
    }

    fun setProperties() {
//        this.layoutParams = LayoutParams(
//            200,
//            500
//        )
    }

    // 화면 그리기
    fun draw() {
        invalidate()
    }

    fun bind(customProps: JSONObject?) {
        customProps?.let { props ->
            /// 호가 타입 ( "NORMAL" / "ORDER" / "SMALL" )
            hogaType = props.optString("hoga_type").ifBlank { Constants.HOGA_TYPE_NORMAL }

            // Layout Width & Height
            val lWidth = props.optString("width")
            val lHeight = props.optString("height")

            val params = LayoutParams(
                if (lWidth.toIntOrNull() == null) LayoutParams.MATCH_PARENT else if (lWidth.equals("match_parent")) LayoutParams.MATCH_PARENT else lWidth.toInt(),
                if (lHeight.toIntOrNull() == null) LayoutParams.MATCH_PARENT else if (lHeight.equals(
                        "match_parent"
                    )
                ) LayoutParams.MATCH_PARENT else lHeight.toInt()
            )

            // Layout Padding
            if (props.optJSONObject("paddings") != null) {
                val objPadding = props.optJSONObject("paddings")!!
                val pLeft = objPadding.optString("left").toIntOrNull() ?: 0
                val pTop = objPadding.optString("top").toIntOrNull() ?: 0
                val pRight = objPadding.optString("right").toIntOrNull() ?: 0
                val pBot = objPadding.optString("bottom").toIntOrNull() ?: 0
                this.setPadding(pLeft, pTop, pRight, pBot)
            }

            // Layout Margin
            if (props.optJSONObject("margins") != null) {
                val objMargin = props.optJSONObject("margins")!!
                val mLeft = objMargin.optString("left").toIntOrNull() ?: 0
                val mTop = objMargin.optString("top").toIntOrNull() ?: 0
                val mRight = objMargin.optString("right").toIntOrNull() ?: 0
                val mBot = objMargin.optString("bottom").toIntOrNull() ?: 0
                params.setMargins(mLeft, mTop, mRight, mBot)
            }

            this.layoutParams = params

            // 텍스트 스타일 설정
            // 1. Size
            //  - Text Size
            //  - Subtext Size
            // 2. Typeface ( Bold or Normal )
            // 3. Color
            //  - Ask Color ( 매도 )
            //  - Bid Color ( 매수 )
            //  - Price Color ( 현재가 )
            //  - Rate Color ( 증감율 )

            props.optString("font_size")
            props.optString("sub_font_size")


//
//            numberTextView.textSize = it.optInt("label_size").toFloat()
//            typeTextView.textSize = it.optInt("unit_size").toFloat()
//            numberTextView.setTypeface(null, if (it.optString("label_weight") == "bold") Typeface.BOLD else Typeface.NORMAL)
//            typeTextView.setTypeface(null, if (it.optString("unit_weight") == "bold") Typeface.BOLD else Typeface.NORMAL)
//
//            val labelColor = it.optString("label_color")
//            val parsedLabelColor = try {
//                Color.parseColor(if (labelColor.isNullOrBlank()) "#000000" else labelColor)
//            } catch (e: IllegalArgumentException) {
//                Color.parseColor("#000000") // 기본 색상
//            }
//
//            val unitColor = it.optString("unit_color")
//            val parsedUnitColor = try {
//                Color.parseColor(if (unitColor.isNullOrBlank()) "#c4c4c4" else unitColor)
//            } catch (e: IllegalArgumentException) {
//                Color.parseColor("#c4c4c4") // 기본 색상
//            }
//            numberTextView.setTextColor(parsedLabelColor)
//            typeTextView.setTextColor(parsedUnitColor)
//
//            /// 배경 코드로 생성 - 곡선율, 두께, 색상 추가
//            val borderRadius = it.optInt("radius",8)
//            val borderColor = try {
//                Color.parseColor(it.optString("border_color") ?: "#C4C4C4")
//            } catch (e: IllegalArgumentException) {
//                Color.parseColor("#C4C4C4")
//            }
//            val borderWidth = it.optInt("border_width", 1) // 기본값 1dp
//            val drawable = GradientDrawable().apply {
//                shape = GradientDrawable.RECTANGLE
//                cornerRadius = borderRadius * resources.displayMetrics.density
//                setStroke(borderWidth, borderColor)
//            }
//            val lineWidth = (borderWidth * resources.displayMetrics.density).toInt()
//
//            listOf(lineView1, lineView2).forEach { view ->
//                view.layoutParams = view.layoutParams.apply {
//                    width = lineWidth
//                }
//                view.setBackgroundColor(borderColor)
//            }
//            parentLayoutView.background = drawable
//
//            updateUI()
        }
    }

    fun createTopRightView(): LinearLayout {
        /// 대출한도 ~ 자사주
        var commonParam = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        commonParam.topMargin = 24

        val tv1 = TextView(context)
        tv1.setTextColor(Color.RED)
        tv1.textSize = 12F
        tv1.setText("대출한도 10억")
        tv1.setPadding(24, 0, 0, 0)
        tv1.layoutParams = commonParam

        val tv2 = TextView(context)
        tv2.setTextColor(Color.RED)
        tv2.textSize = 12F
        tv2.setText("신용한도 10억")
        tv2.setPadding(24, 0, 0, 0)
        tv2.layoutParams = commonParam

        val tv3 = TextView(context)
        tv3.setTextColor(Color.RED)
        tv3.textSize = 12F
        tv3.setText("증거금 30%")
        tv3.setPadding(24, 0, 0, 0)
        tv3.layoutParams = commonParam

        val tv4 = TextView(context)
        tv4.setTextColor(Color.RED)
        tv4.textSize = 12F
        tv4.setText("자사주")
        tv4.setPadding(24, 0, 0, 0)
        tv4.layoutParams = commonParam

        ///////////////////////////////////////////
        ///  구분선
        val divLine = MaterialDivider(context)
        val divParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            2
        )
        divLine.setBackgroundColor(getColor(context, R.color.hogabdGray))
        divParams.topMargin = 40
        divParams.bottomMargin = 40
        divLine.layoutParams = divParams

        /** 상승VI ~ 전일종가 */
        val subParam = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        subParam.weight = 1F
        /// 상승 VI
        val tv5_1 = TextView(context)
        tv5_1.setTextColor(Color.RED)
        tv5_1.textSize = 12F
        tv5_1.setText("상승VI")
        tv5_1.setPadding(24, 0, 0, 0)
        tv5_1.layoutParams = subParam

        val tv5_2 = TextView(context)
        tv5_2.setTextColor(Color.RED)
        tv5_2.textSize = 12F
        tv5_2.setText("62,500")
        tv5_2.setPadding(0, 0, 24, 28)

        var linSub1 = LinearLayout(context)
        linSub1.orientation = LinearLayout.HORIZONTAL
        linSub1.addView(tv5_1)
        linSub1.addView(tv5_2)

        // 하락 VI
        val tv6_1 = TextView(context)
        tv6_1.setTextColor(Color.BLUE)
        tv6_1.textSize = 12F
        tv6_1.setText("하락VI")
        tv6_1.setPadding(24, 0, 0, 0)
        tv6_1.layoutParams = subParam

        val tv6_2 = TextView(context)
        tv6_2.setTextColor(Color.BLUE)
        tv6_2.textSize = 12F
        tv6_2.setText("51,100")
        tv6_2.setPadding(0, 0, 24, 28)

        var linSub2 = LinearLayout(context)
        linSub2.orientation = LinearLayout.HORIZONTAL
        linSub2.addView(tv6_1)
        linSub2.addView(tv6_2)

        // 전일종가
        val tv7_1 = TextView(context)
        tv7_1.setTextColor(Color.BLUE)
        tv7_1.textSize = 12F
        tv7_1.setText("전일종가")
        tv7_1.setPadding(24, 0, 0, 0)
        tv7_1.layoutParams = subParam

        val tv7_2 = TextView(context)
        tv7_2.setTextColor(Color.BLUE)
        tv7_2.textSize = 12F
        tv7_2.setText("51,100")
        tv7_2.setPadding(0, 0, 24, 28)

        val linSub3 = LinearLayout(context)
        linSub3.orientation = LinearLayout.HORIZONTAL
        linSub3.addView(tv7_1)
        linSub3.addView(tv7_2)

        /// 전체 합치기
        val linearLayout = LinearLayout(context)
        linearLayout.setBackgroundColor(Color.WHITE)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.right = 0

        linearLayout.addView(tv1)
        linearLayout.addView(tv2)
        linearLayout.addView(tv3)
        linearLayout.addView(tv4)
        linearLayout.addView(divLine)

        linearLayout.addView(linSub1)
        linearLayout.addView(linSub2)
        linearLayout.addView(linSub3)

        return linearLayout
    }

    fun createBotLeftView() : LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.setBackgroundColor(Color.WHITE)
        return linearLayout
    }


    fun setSubView1(subView: ViewGroup) {
        this.subView1 = subView
    }

    fun setSubView2(subView: ViewGroup) {
        this.subView2 = subView
    }
}