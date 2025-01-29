package com.example.home.domain.value_object.group

import com.example.home.domain.value_object.Constants
import com.example.home.util.StringUtil

data class GroupSettingKey(val value: String) {
    companion object {
        const val MAX_BYTE_LENGTH = 512
    }
    init {
        requireNotNull(value) { "GroupSettingKey must not be null." }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "GroupSettingKey must not exceed $MAX_BYTE_LENGTH bytes, actual size: ${StringUtil.byteSize(value)} bytes"
        }
    }
}