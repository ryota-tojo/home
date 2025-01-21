package com.example.home.domain.value_object.notice

import com.example.home.util.StringUtil

data class NoticeContent(val value: String) {
    companion object {
        val MAX_BYTE_LENGTH = 65535;
    }

    init {
        requireNotNull(value) { "NoticeContent must not be null." }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "NoticeContent must not exceed ${MAX_BYTE_LENGTH} bytes, actual size: ${StringUtil.byteSize(value)} bytes"
        }
    }
}
