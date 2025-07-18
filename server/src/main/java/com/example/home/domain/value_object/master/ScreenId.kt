package com.example.home.domain.value_object.master

import com.example.home.domain.value_object.shopping.ShoppingRemarks
import com.example.home.util.StringUtil

data class ScreenId(val value: String) {
    companion object {
        val MAX_BYTE_LENGTH = 256
    }

    init {
        requireNotNull(value) { "ScreenId must not be null." }
        require(StringUtil.byteSize(value) <= ShoppingRemarks.MAX_BYTE_LENGTH) {
            "ScreenId must not exceed ${ShoppingRemarks.MAX_BYTE_LENGTH} bytes, actual size: ${
                StringUtil.byteSize(
                    value
                )
            } bytes"
        }
    }
}