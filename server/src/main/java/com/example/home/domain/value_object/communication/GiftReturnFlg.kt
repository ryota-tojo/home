package com.example.home.domain.value_object.communication

data class GiftReturnFlg(val value: Int) {
    companion object {
        val MIN = 0
        val MAX = 2
    }

    init {
        require(value in MIN..MAX) { "GiftReturnFlg must be in range ${MIN}..${MAX}" }
    }
}