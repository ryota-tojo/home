package com.example.home.domain.value_object.template

data class TemplateDeleteFlg(val value: Int) {
    companion object {
        val MIN = 0
        val MAX = 1
    }

    init {
        require(value in MIN..MAX) { "TemplateDeleteFlg must be in range ${MIN}..${MAX}" }
    }
}
