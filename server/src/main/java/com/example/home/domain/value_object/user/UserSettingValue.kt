package com.example.home.domain.value_object.user

import com.example.home.util.StringUtil

data class UserSettingValue(val value: String) {
    companion object {
        val MAX_BYTE_LENGTH = 256;
    }

    init {
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "UserSettingValue must not exceed ${MAX_BYTE_LENGTH} bytes, actual size: ${StringUtil.byteSize(value)} bytes"
        }
    }
}