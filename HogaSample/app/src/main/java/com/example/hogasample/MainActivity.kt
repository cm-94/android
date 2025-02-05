package com.example.hogasample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hogasample.control.hoga.HogaLayout
import com.example.hogasample.data.HogaData
import com.example.hogasample.data.HogaDataSet

class MainActivity : AppCompatActivity() {
    lateinit var hogaDataSet : HogaDataSet
    lateinit var hogaLayout : HogaLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        createDummyData()
        hogaLayout = findViewById(R.id.hoga)
        hogaLayout.setData(hogaDataSet, null)
    }

    fun createDummyData(){
//        for (i in 0 until 10){
//        for (i in 0..10 step 1){
        var currPrice = 1000 /// 현재가
        var lowPrice = (currPrice * 0.7).toInt()// 하한가
        var highPrice = (currPrice * 1.3).toInt() // 상한가

        var hashData = HashMap<String, HogaData>()

        for (i in 0..9 step(1)){
            val askValue = (lowPrice..currPrice).random() // generated random from 0 to 10 included
            val rate = ((currPrice - askValue) / currPrice).toFloat()
            val amtValue = (0..5000).random() // generated random from 0 to 10 included
            val askData = HogaData(askValue,amtValue, rate,1)
            hashData.put("ask" + i + 1, askData)
        }

        for (i in 0..9 step(1)){
            val bidValue = (currPrice..highPrice).random() // generated random from 0 to 10 included
            val rate = ((bidValue - currPrice) / currPrice).toFloat()
            val amtValue = (0..5000).random() // generated random from 0 to 10 included
            val bidData = HogaData(bidValue,amtValue, rate,1)
            hashData.put("bit" + (i + 1), bidData)
        }
        hogaDataSet = HogaDataSet(hashData)
    }
}