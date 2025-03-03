package com.example.home.domain.value_object.master

import com.example.home.domain.value_object.shopping.ShoppingRemarks
import com.example.home.util.StringUtil

data class MasterSettingRemarks(val value: String) {
    companion object {
        val MAX_BYTE_LENGTH = 1024;
    }

    init {
        require(StringUtil.byteSize(value) <= ShoppingRemarks.MAX_BYTE_LENGTH) {
            "SettingRemarks must not exceed ${ShoppingRemarks.MAX_BYTE_LENGTH} bytes, actual size: ${
                StringUtil.byteSize(
                    value
                )
            } bytes"
        }
    }
}