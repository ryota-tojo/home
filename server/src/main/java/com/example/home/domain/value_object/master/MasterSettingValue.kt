package com.example.home.domain.value_object.master

import com.example.home.util.StringUtil

data class MasterSettingValue(val value: String) {
    companion object {
        val MAX_BYTE_LENGTH = 256
    }

    init {
        requireNotNull(value) { "MasterSettingValue must not be null." }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "MasterSettingValue must not exceed ${MAX_BYTE_LENGTH} bytes, actual size: ${StringUtil.byteSize(value)} bytes"
        }
    }
}