package com.example.home.domain.value_object.etc

data class Amount(val value: Int){
    init{
        requireNotNull(value) { "Amount must not be null." }
    }
    companion object{
        fun isMinAmountGreaterThanMaxAmount(minAmount: Amount?, maxAmount: Amount?): Boolean{
            // min, maxのどちらかがnullの場合はfalse
            if(minAmount == null || maxAmount == null){
                return false
            }
            // minがmaxを上回っていたらtrue
            if(minAmount.value > maxAmount.value){
                return true
            }
            return false
        }
    }
}
