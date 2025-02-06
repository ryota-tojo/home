package com.example.home.domain.value_object.etc

class Year(val value: Int) {
    companion object {
        val MIN = 1901
        val MAX = 2100
    }

    init {
        requireNotNull(value) { "Year must not be null." }
        require(value in MIN..MAX) { "Year must be in range ${MIN}..${MAX}" }
    }
}