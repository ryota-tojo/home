package com.example.home.domain.value_object.member

import com.example.home.domain.value_object.Constants
import com.example.home.util.StringUtil

data class MemberName(val value: String) {
    companion object {
        val MAX_BYTE_LENGTH = 64;
    }

    init {
        requireNotNull(value) { "MemberName must not be empty" }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "MemberName must not exceed $MAX_BYTE_LENGTH bytes, actual size: ${StringUtil.byteSize(value)} bytes"
        }
        require(Constants.INVALID_SYMBOL.none { it in value }) {
            "MemberName must not contain forbidden characters: ${Constants.INVALID_SYMBOL.joinToString(", ")}"
        }
        require(Constants.INVALID_WORD.none { it in value }) {
            "MemberName must not contain forbidden characters: ${Constants.INVALID_WORD.joinToString(", ")}"
        }
    }
}
