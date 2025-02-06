package com.example.home.domain.value_object.category

import com.example.home.util.StringUtil

data class CategoryName(val value: String) {
    companion object {
        val MAX_BYTE_LENGTH = 64
    }

    init {
        requireNotNull(value) { "CategoryName must not be empty" }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "CategoryName must not exceed ${MAX_BYTE_LENGTH} bytes, actual size: ${StringUtil.byteSize(value)} bytes"
        }
    }
}
