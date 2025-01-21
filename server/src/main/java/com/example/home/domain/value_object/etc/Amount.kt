package com.example.home.domain.value_object.etc

data class Amount(val value: Int){

    companion object{
        val MIN = 0
    }
    init{
        requireNotNull(value) { "Amount must not be null." }
        require(value >= MIN) { "Amount must be greater than or equal to $MIN." }

    }
}
