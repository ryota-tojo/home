package com.example.home.domain.value_object.user

import com.example.home.domain.value_object.Constants
import com.example.home.util.StringUtil

data class UserSettingKey(val value: String) {
    companion object{
        val MAX_BYTE_LENGTH = 512;
    }

    init {
        requireNotNull(value) { "UserSettingKey must not be empty" }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "UserSettingKey must not exceed ${MAX_BYTE_LENGTH} bytes, actual size: ${StringUtil.byteSize(value)} bytes"
        }
        require(Constants.INVALID_SYMBOL.none { it in value }) {
            "UserSettingKey must not contain forbidden characters: ${Constants.INVALID_SYMBOL.joinToString(", ")}"
        }
    }
}