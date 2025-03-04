package com.example.home.domain.value_object.etc

data class Amount(val value: Int){
    init{
        requireNotNull(value) { "Amount must not be null." }
    }
}
