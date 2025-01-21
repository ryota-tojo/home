package com.example.home.domain.value_object.notice

import com.example.home.util.StringUtil

data class NoticeTitle(val value : String){
    companion object {
        val MAX_BYTE_LENGTH = 1024;
    }

    init {
        requireNotNull(value) { "NoticeTitle must not be null." }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "NoticeTitle must not exceed ${MAX_BYTE_LENGTH} bytes, actual size: ${StringUtil.byteSize(value)} bytes"
        }
    }
}
