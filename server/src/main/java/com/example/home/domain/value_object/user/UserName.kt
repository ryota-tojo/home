package com.example.home.domain.value_object.user

import com.example.home.domain.value_object.Constants
import com.example.home.util.StringUtil

data class UserName(val value: String) {
    companion object{
        val MAX_BYTE_LENGTH = 64
    }
    init{
        requireNotNull(value) { "UserName must not be empty" }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "UserName must not exceed ${MAX_BYTE_LENGTH} bytes, actual size: ${StringUtil.byteSize(value)} bytes"
        }
        require(Constants.INVALID_SYMBOL.none {  it in value }) {
            "UserName must not contain forbidden characters: ${Constants.INVALID_SYMBOL.joinToString(", ")}"
        }
    }
}