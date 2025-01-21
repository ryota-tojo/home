package com.example.home.domain.value_object.user

import com.example.home.util.StringUtil

data class UserPassword(val value: String) {
    companion object {
        val MAX_BYTE_LENGTH = 64;
    }

    init {
        requireNotNull(value) { "Password must not be empty" }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "Password must not exceed ${MAX_BYTE_LENGTH} bytes, actual size: ${StringUtil.byteSize(value)} bytes"
        }
    }
}