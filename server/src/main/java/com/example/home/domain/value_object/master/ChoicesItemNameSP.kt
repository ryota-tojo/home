package com.example.home.domain.value_object.master

import com.example.home.domain.value_object.Constants
import com.example.home.util.StringUtil

class ChoicesItemNameSP(val value: String) {
    companion object {
        val MAX_BYTE_LENGTH = 256
    }

    init {
        requireNotNull(value) { "ChoicesItemName must not be null." }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "ChoicesItemName must not exceed ${MAX_BYTE_LENGTH} bytes. Actual size: ${StringUtil.byteSize(value)} bytes."
        }
        require(Constants.INVALID_SYMBOL.none { it in value }) {
            "ChoicesItemName must not contain forbidden characters: ${Constants.INVALID_SYMBOL.joinToString(", ")}"
        }
    }
}