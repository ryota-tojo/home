package com.example.home.domain.value_object.etc

data class MM(val value: Int) {
    companion object {
        val MIN = 1
        val MAX = 12
    }

    init {
        requireNotNull(value) { "Month must not be null." }
        require(value in MIN..MAX) { "Month must be in range ${MIN}..${MAX}" }

    }
}