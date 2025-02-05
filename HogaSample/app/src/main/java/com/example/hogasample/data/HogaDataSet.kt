package com.example.hogasample.data

class HogaDataSet {
    var data : HashMap<String,HogaData> = HashMap()
    var minValue = Int.MAX_VALUE
    var maxValue = Int.MIN_VALUE
    var maxAmount = Int.MIN_VALUE

    constructor(newData: HashMap<String,HogaData>) {
        if(newData.size > 0){
            for(item in newData){
                if(item.value.price < minValue) minValue = item.value.price
                if(item.value.price > maxValue) maxValue = item.value.price
                if(item.value.amount > maxAmount) maxAmount = item.value.amount
            }
        }
        this.data = newData
    }
}