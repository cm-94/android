package com.example.hogasample.data

class HogaData (
    var price : Int = 0, // 가격
    var amount : Int = 0, // 수량
    var rate : Float = 0.0F, // 증감율
    var sign : Int = 0 // 1: 매수 호가, -1: 매도 호가
){}