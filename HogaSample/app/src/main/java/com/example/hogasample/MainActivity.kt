package com.example.hogasample

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hogasample.control.hoga.HogaLayout
import com.example.hogasample.data.HogaData
import com.example.hogasample.data.HogaDataSet

class MainActivity : AppCompatActivity() {
    lateinit var hogaDataSet: HogaDataSet
    lateinit var hogaLayout: HogaLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        createDummyData()
//        hogaLayout = HogaLayout(context = baseContext)
        hogaLayout = findViewById(R.id.hoga)
        hogaLayout.setData(hogaDataSet, null)
        /// TODO : Settting Properties
        hogaLayout.setProperties()
        hogaLayout.draw()
    }


    @SuppressLint("DefaultLocale")
    fun createDummyData() {
//        for (i in 0 until 10){
//        for (i in 0..10 step 1){
        var currPrice = 1000 /// 현재가
        var lowPrice = (currPrice * 0.7).toInt()// 하한가
        var highPrice = (currPrice * 1.3).toInt() // 상한가

        var hashData = HashMap<String, HogaData>()

        /// 매도 호가
        for (i in 0..9 step (1)) {
            val askPrice = currPrice + 25 * (9 - i) // (currPrice..highPrice).random()
            val rate = (((askPrice - currPrice) / currPrice.toDouble()) * 100).toFloat()
            val amtValue = (0..5000).random()
            val askData = HogaData(askPrice, amtValue, rate, -1)
            hashData.put("ask_" + (i + 1), askData)
        }

        /// 매수 호가
        for (i in 0..9 step (1)) {
            val bidPrice = currPrice - 25 * (i + 1) // (lowPrice..currPrice).random()
            val rate = ((bidPrice - currPrice) / currPrice.toDouble() * 100).toFloat() * -1
            val amtValue = (0..5000).random()
            val bidData = HogaData(bidPrice, amtValue, rate, 1)
            hashData.put("bid_" + (i + 1), bidData)
        }
        hogaDataSet = HogaDataSet(hashData)
    }

    fun createTopRightView() : LinearLayout{
        val tv1 = TextView(baseContext)
        tv1.setTextColor(Color.RED)
        tv1.textSize = 20F
        tv1.setText("대출한도 10억")

        val tv2 = TextView(baseContext)
        tv2.setTextColor(Color.RED)
        tv2.textSize = 20F
        tv2.setText("신용한도 10억")

        val tv3 = TextView(baseContext)
        tv3.setTextColor(Color.RED)
        tv3.textSize = 20F
        tv3.setText("증거금 30%")

        val tv4 = TextView(baseContext)
        tv4.setTextColor(Color.RED)
        tv4.textSize = 20F
        tv4.setText("자사주")

        val linearLayout = LinearLayout(baseContext)
        linearLayout.addView(tv1)
        linearLayout.addView(tv2)
        linearLayout.addView(tv3)
        linearLayout.addView(tv4)

        return linearLayout

    }

    fun createBottomLeftView(){

    }
}