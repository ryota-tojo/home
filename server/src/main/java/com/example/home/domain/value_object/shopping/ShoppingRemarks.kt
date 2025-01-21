package com.example.home.domain.value_object.shopping

import com.example.home.domain.value_object.Constants
import com.example.home.util.StringUtil

data class ShoppingRemarks(val value:String){
    companion object {
        val MAX_BYTE_LENGTH = 1024;
    }

    init {
        requireNotNull(value) { "ShoppingRemarks must not be empty" }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "ShoppingRemarks must not exceed $MAX_BYTE_LENGTH bytes, actual size: ${StringUtil.byteSize(value)} bytes"
        }
        require(Constants.INVALID_SYMBOL.none { it in value }) {
            "ShoppingRemarks must not contain forbidden characters: ${Constants.INVALID_SYMBOL.joinToString(", ")}"
        }
        require(Constants.INVALID_WORD.none { it in value }) {
            "ShoppingRemarks must not contain forbidden characters: ${Constants.INVALID_WORD.joinToString(", ")}"
        }
    }
}
