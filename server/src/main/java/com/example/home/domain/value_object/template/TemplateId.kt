package com.example.home.domain.value_object.template

import com.example.home.util.StringUtil

data class TemplateId(val value: String) {
    companion object {
        val MAX_BYTE_LENGTH = 64
    }

    init {
        requireNotNull(value) { "TemplateID must not be null." }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "TemplateID must not exceed $MAX_BYTE_LENGTH bytes. Actual size: ${StringUtil.byteSize(value)} bytes."
        }
    }
}