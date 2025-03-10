package com.example.home.domain.value_object.analysis

data class Ratio(val value: Double) {
    init {
        requireNotNull(value) { "Ratio must not be null." }
    }
}
