package com.example.home.domain.value_object.shopping

data class ShoppingSettlement(val value:Int){
    companion object {
        val MIN = 0
        val MAX = 1
    }

    init {
        require(value in MIN..MAX) { "ShoppingClass must be in range ${MIN}..${MAX}" }
    }
}
