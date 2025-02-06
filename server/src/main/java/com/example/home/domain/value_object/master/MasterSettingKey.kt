package com.example.home.domain.value_object.master

import com.example.home.domain.value_object.Constants
import com.example.home.util.StringUtil

class MasterSettingKey(val value: String) {
    companion object {
        val MAX_BYTE_LENGTH = 512;
    }

    init {
        requireNotNull(value) { "MasterSettingKey must not be null." }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "MasterSettingKey must not exceed ${MAX_BYTE_LENGTH} bytes, actual size: ${StringUtil.byteSize(value)} bytes"
        }
        require(Constants.INVALID_SYMBOL.none { it in value }) {
            "MasterSettingKey must not contain forbidden characters: ${Constants.INVALID_SYMBOL.joinToString(", ")}"
        }
    }
}