package com.example.home.domain.value_object.analysis

data class UseRate(val value: Double) {
    init {
        requireNotNull(value) { "UseRate must not be null." }
    }
}
