package com.example.home.domain.value_object.template

data class TemplateUseFlg(val value: Int) {
    companion object {
        val MIN = 0
        val MAX = 1
    }

    init {
        require(value in MIN..MAX) { "UserLeaderFlg must be in range ${MIN}..${MAX}" }
    }
}
